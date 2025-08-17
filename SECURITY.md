# Security Policy

Thank you for helping keep this project and its users safe.

This document explains how to report a vulnerability, what to expect from us, and how we handle security fixes and disclosures.


## Supported Versions

We follow a pragmatic policy focused on the most recent releases:

- The latest release on the default branch receives security fixes.
- If multiple supported release lines exist, only the latest patch of the latest minor is actively supported.
- Older versions may receive critical fixes at the maintainers’ discretion, but users are strongly encouraged to upgrade to the latest release.

If you rely on a specific older version, consider pinning and testing upgrades regularly.


## Reporting a Vulnerability (Preferred: GitHub Security Advisories)

Please report security issues privately. Do not create public GitHub issues for potential vulnerabilities.

- Use the repository’s Security tab and click “Report a vulnerability” to open a private GitHub Security Advisory (GHSA):
  https://docs.github.com/en/code-security/security-advisories/repository-security-advisories/creating-a-repository-security-advisory
- If the Security tab is unavailable for you, you may contact the maintainers via a private channel if one is listed in the repository (e.g., in the README). As a last resort, open a minimal public issue requesting a secure contact method without including sensitive details.

When reporting, please include (as applicable):
- A clear description of the issue and potential impact
- A minimal reproducible example or steps to reproduce
- Affected versions/commit(s)
- Possible mitigations or workarounds
- Any relevant environment details (OS, JDK, configuration)

We welcome reports from everyone, including researchers and users.


## Our Security Response Process

We aim to follow these timelines for privately reported issues:
- Acknowledgement: within 48 hours
- Initial assessment and triage: within 72 hours
- Fix development: typically within 7–14 days for high/critical issues
- Coordinated release: we will coordinate an advisory, a fixed release, and public disclosure once a patch or mitigation is available

These are goals, not guarantees; complex issues may require more time. We will keep reporters updated through the advisory thread.


## Coordinated Disclosure

- Please keep details private until an official fix and advisory are published.
- We prefer coordinated disclosure with a joint timeline; we’ll credit reporters in the advisory release notes unless anonymity is requested.
- If an issue is found to be non-exploitable or out of scope, we will explain our reasoning.


## Scope

In scope for this project:
- Vulnerabilities in the project’s code (Java/Kotlin/Groovy) and build logic within this repository
- Configuration flaws in example code or scripts that materially reduce security when used as documented
- Supply-chain risks specific to this repository (e.g., malicious code introduced here)

Out of scope:
- Vulnerabilities exclusively in third-party dependencies (please report upstream); we will still triage and update dependencies when feasible
- Denial-of-service by resource exhaustion from unrealistic inputs or environments
- Social engineering, phishing, or physical attacks
- Spam, non-security bugs, or feature requests (use regular issue tracker)


## Handling Dependency Vulnerabilities

- We monitor and update dependencies regularly. When a dependency CVE affects this project:
  - We assess impact and exploitable paths.
  - We upgrade to a safe version or apply mitigations.
  - We may publish a security advisory if the impact to users is significant.

If you identify an affected dependency and a safe version, please include that information in your report.


## Security Hardening Recommendations for Users

- Always use the latest stable release of this project and a supported JDK (e.g., JDK 21).
- Run applications with the least privileges required; avoid running as root/Administrator.
- Review configuration and environment variables for secrets; never commit secrets to source control.
- Validate and sanitize all external inputs in your integration code.
- Keep your build toolchain (Gradle wrapper, plugins) up to date.
- Enable CI checks (tests, linters) and review dependency updates promptly.


## Credits

We appreciate responsible disclosures. Unless you request otherwise, we will credit reporters in the release notes/advisory.


## Questions

If you have general questions about this policy (not a security report), please open a regular GitHub issue for discussion.
