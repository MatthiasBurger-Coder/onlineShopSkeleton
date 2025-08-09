#!/usr/bin/env python3
import json
import os
import subprocess
import csv

BASE_DIR = os.path.abspath(os.path.join(os.path.dirname(__file__), ".."))
BUILD_DIR = os.path.join(BASE_DIR, "build", "complexity")
JSON_FILE = os.path.join(BUILD_DIR, "lizard.json")
SARIF_FILE = os.path.join(BUILD_DIR, "lizard.sarif.json")

def run_lizard():
    os.makedirs(BUILD_DIR, exist_ok=True)
    # Run lizard with CSV output
    cmd = [
        "lizard", "-l", "java",
        "-x", "**/build/**", "-x", "**/out/**", "-x", "**/generated/**",
        "-C", "0", "-CSV"  # disable threshold filtering, request CSV output
    ]
    proc = subprocess.Popen(cmd, cwd=BASE_DIR, stdout=subprocess.PIPE, stderr=subprocess.PIPE, text=True)
    reader = csv.DictReader(proc.stdout)
    rows = list(reader)
    ret = proc.wait()
    if ret != 0:
        stderr = proc.stderr.read() if proc.stderr else ""
        raise RuntimeError(f"lizard exited with code {ret}. stderr:\n{stderr}")
    with open(JSON_FILE, "w", encoding="utf-8") as f:
        json.dump({"function_list": rows}, f, indent=2)

def json_to_sarif():
    with open(JSON_FILE, "r", encoding="utf-8") as f:
        data = json.load(f)
    funcs = data.get("function_list", [])
    results = []
    for fn in funcs:
        # Extract file and line robustly
        file = fn.get("filename") or fn.get("file") or ""
        line_val = fn.get("start") or fn.get("line")
        if (not file or not line_val):
            loc = fn.get("location") or fn.get("Location")
            if loc:
                # Expect something like "path:line"; split from right to be safe
                if isinstance(loc, str) and ":" in loc:
                    parts = loc.rsplit(":", 1)
                    if len(parts) == 2:
                        file = file or parts[0]
                        line_val = line_val or parts[1]
        # Defaults if still missing
        file = file or "unknown"
        try:
            line = int(str(line_val)) if line_val is not None and str(line_val).strip() != "" else 1
        except ValueError:
            line = 1
        # Extract metrics
        ccn = fn.get("CCN") or fn.get("ccn")
        nloc = fn.get("NLOC") or fn.get("nloc")
        if not ccn:
            continue
        try:
            ccn_num = int(str(ccn).strip())
        except ValueError:
            ccn_num = None
        try:
            nloc_num = int(str(nloc).strip()) if nloc is not None else None
        except ValueError:
            nloc_num = None
        msg = f"Cyclomatic complexity = {ccn_num if ccn_num is not None else ccn}" + (f" (NLOC {nloc_num})" if nloc_num is not None else (f" (NLOC {nloc})" if nloc else ""))
        results.append({
            "ruleId": "lizard.ccn",
            "level": "note",
            "message": {"text": msg},
            "locations": [{
                "physicalLocation": {
                    "artifactLocation": {"uri": file},
                    "region": {"startLine": line}
                }
            }]
        })
    sarif = {
        "version": "2.1.0",
        "$schema": "https://json.schemastore.org/sarif-2.1.0.json",
        "runs": [{
            "tool": {
                "driver": {
                    "name": "lizard",
                    "rules": [{
                        "id": "lizard.ccn",
                        "name": "Cyclomatic complexity",
                        "shortDescription": {"text": "Cyclomatic complexity per function"},
                        "fullDescription": {"text": "Informational CCN results generated from lizard CSV output."},
                        "properties": {"problem.severity": "note"}
                    }]
                }
            },
            "results": results
        }]
    }
    with open(SARIF_FILE, "w", encoding="utf-8") as f:
        json.dump(sarif, f, indent=2)
    print(f"Wrote SARIF with {len(results)} results to {SARIF_FILE}")

if __name__ == "__main__":
    run_lizard()
    json_to_sarif()
