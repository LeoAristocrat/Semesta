# Semesta 1.0.1 signed release — 9 October 2026

The current English-only release is in the ignored local directory
`release-artifacts/1.0.1/`. Upload `Semesta-1.0.1.apk` and `SHA256SUMS.txt` manually
to the GitHub release tagged `v1.0.1`. Use `release-notes.md` for its description.
The AAB is available for Play Console; retain `mapping.txt` privately for crash
analysis. Commit and push the matching source before creating the release tag.

## Verified results

| Check | Result |
|---|---|
| Android identity | `com.leoaristocrat.semesta` |
| Version / version code | `1.0.1` / `10001999` |
| Release APK and AAB build | Passed; shrunk production artifacts |
| Release unit tests | 584 passed; zero failures, errors or skipped tests |
| Full release lint | Zero errors; 378 warnings and 35 hints |
| Design token verification | Passed |
| APK signature | Verified; existing release certificate |
| Bundle signature | Verified; same certificate as the APK |
| APK alignment | Passed `zipalign -c -P 16 4` |
| Debuggable flag | Absent |
| Embedded release notes | Both existing asset names contain identical English notes, including 1.0.1; no Unreleased section |
| Font license | Bundled OFL-1.1 notice unchanged from signed 1.0.0 |

The complete pipeline generated new artifacts, test results and lint reports.
The Windows PowerShell background runner initially treated an SDK compatibility
warning on native stderr as a failure and lost the remainder of its console log.
After correcting that reporting, the same pipeline confirmed the completed inputs
and outputs successfully in 38 seconds (85 tasks up to date, two executed).
No optimization or test checks were disabled. Final build log:
`semesta-release-1.0.1-validation.log`. Artifact verification details are retained
locally in `release-artifacts/1.0.1/verification.json` and the signature reports.

The English cleanup regression tests cover legacy Spanish/system preferences,
background English resources, backup preference preservation and Android resource
spacing in onboarding. Earlier native light/dark English review is documented in
[validation](VALIDATION.md). No additional emulator or physical-device smoke test
was performed for this packaging run. Matching certificates and the increasing
version code establish signing compatibility with 1.0.0; this run did not perform
a live installation upgrade. Firebase/OAuth configuration remains owner-supplied.

## SHA-256

```text
e30998ccb4d150baf5bccacb594ed0a157c88d47d1e7c3810a99c4f5f25b0b34  Semesta-1.0.1.apk
7f04d23621bc8d5fe4116554ebde39ee530c745734e21be65912c6b76e973dd5  Semesta-1.0.1.aab
```

Public certificate fingerprint:
`566074e6051080a2eacb843aff7804204e12a031c3da725a7f58180e4fdfee68`.
Signing secrets, artifacts, build logs, design prototypes and review screenshots
remain excluded from Git. No commit, tag, push or GitHub release was created here.
