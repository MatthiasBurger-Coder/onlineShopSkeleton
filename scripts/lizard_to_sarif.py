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
        "lizard", "-l", "java", "-x", "**/build/**", "-x", "**/out/**", "-x", "**/generated/**",
        "-C", "0", "-w"  # disable threshold filtering, wide output
    ]
    proc = subprocess.Popen(cmd, cwd=BASE_DIR, stdout=subprocess.PIPE, text=True)
    reader = csv.DictReader(proc.stdout)
    rows = list(reader)
    proc.wait()
    with open(JSON_FILE, "w", encoding="utf-8") as f:
        json.dump({"function_list": rows}, f, indent=2)

def json_to_sarif():
    with open(JSON_FILE, "r", encoding="utf-8") as f:
        data = json.load(f)
    funcs = data.get("function_list", [])
    results = []
    for fn in funcs:
        file = fn.get("filename") or fn.get("file") or "unknown"
        line = int(fn.get("start", 1))
        ccn = fn.get("CCN") or fn.get("ccn")
        nloc = fn.get("NLOC") or fn.get("nloc")
        if not ccn:
            continue
        msg = f"Cyclomatic complexity = {ccn}" + (f" (NLOC {nloc})" if nloc else "")
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
