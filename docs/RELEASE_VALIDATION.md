# Semesta 1.0.0 signed release — 9 October 2026

Visual-review screenshots are retained locally under `docs/visual-review/` and excluded from Git. Capture paths below identify local evidence; a fresh clone does not contain those images.

Release identity: `com.leoaristocrat.semesta`, version `1.0.0`, version code
`10000999`. The APK is minified/resource-shrunk and is not debuggable. Minimum
Android API 26, target API 35, compile API 36.

## Release gates

| Check | Result |
|---|---|
| APK / AAB assembly | Passed |
| Release JVM tests | 580 tests; zero failures or errors |
| Final full lint / design tokens | Passed; zero lint errors, 419 warnings, 35 hints |
| APK signature | Verified; APK Signature Scheme v2; dedicated RSA 4096 release key |
| APK alignment | Passed `zipalign -c -P 16 4` |
| Bundle signature | Verified; same certificate as the APK |
| Embedded release notes | English and Spanish 1.0.0 notes included |
| Bundled legal notices | Preserved byte for byte |
| GitHub workflows | YAML and Bash syntax checked; actions pinned to commit hashes |
| Source index audit | No private/generated paths or detected credentials; legal notices byte-preserved |

The first complete signed-release gate run passed. Visual review then found two
hardcoded Spanish assessment labels, which were replaced with localized resources.
Lint caught a missing explicit English entry during that correction; it was fixed.
The corrected artifact passed all final gates. The extra lint warning compared
with the earlier debug report recommends a newer Gradle version; it is advisory.

Local build log: `semesta-release-verified-validation.log`. Reports are under
`app/build/reports/` and `app/build/test-results/testReleaseUnitTest/` (ignored).
Existing compiler deprecation warnings remain. This is not a warning-free build.

## Native review

Review used the isolated `semesta_review` Android 36.1 emulator at 720×1600,
320 dpi. Its test records are absent from the APK. The production signing key
replaced only that emulator's disposable debug test installation.

- Completed native onboarding: Arjun, B.Tech / B.E. Computer Science, 10-point
  scale, internal 30% / exam 70%, semester, optional dates skipped.
- Android notification permission prompt opened and permission was granted.
- Opened dashboard, Study, Schedule, Expenses, fee ledger, About and theme settings.
- About showed the official logo, developer identity and version 1.0.0.
- Saved a fee of ₹7,750 with ₹4,000 paid; the ledger displayed ₹3,750 outstanding.
- Reviewed light and dark appearance with the official logo.
- Android accepted the corrected APK as an update signed with the same release key.
- After updating and cold-rebooting the emulator, the saved fee record, dark-mode
  preference and assessment split remained intact.
- The final shared assessment screen showed English “Other” and “allocated”.
- The final dashboard retained the student name. English release notes opened;
  Licenses showed the bundled typefaces and 177 library entries.

Actual captures: welcome (`visual-review/release-1.0.0/01-welcome.png`),
dashboard (`visual-review/release-1.0.0/02-dashboard-light.png`),
keyboard-aware fee form (`visual-review/release-1.0.0/03-fee-form.png`),
fees in light mode (`visual-review/release-1.0.0/04-fees-light.png`),
fees in dark mode (`visual-review/release-1.0.0/05-fees-dark.png`),
fee persistence after update and reboot (`visual-review/release-1.0.0/06-fees-after-release-update.png`),
corrected assessment labels (`visual-review/release-1.0.0/07-assessments-corrected.png`),
final About (`visual-review/release-1.0.0/08-about-final.png`),
release notes (`visual-review/release-1.0.0/09-release-notes.png`),
licenses (`visual-review/release-1.0.0/10-licenses.png`),
dashboard after update (`visual-review/release-1.0.0/11-dashboard-after-update.png`).

Emulator cold boots produced a System UI wait dialog; synthetic keyboard input
was slow under concurrent build load. A stale session after a long host pause was
recovered by reconnecting ADB and cold-booting the isolated AVD. These observations do not
establish physical-device performance. Earlier phone/tablet instrumentation and
broader feature evidence are recorded separately in [validation](VALIDATION.md)
and [logo integration](LOGO_INTEGRATION.md); those are debug-build checks.

## Artifacts

Signed artifacts and private crash mapping are in ignored `release-artifacts/1.0.0/`.
Keep the mapping with this exact release. `SHA256SUMS.txt` contains:

```text
ac49ba9dce51e704b12ed3e3a57d58692c5905e802c7798b2d5ba6ac9f367ed4  Semesta-1.0.0.apk
42064c131a593407f358147485fe6cedfd29ca01b4f6610e0a080e1179bba2fb  Semesta-1.0.0.aab
```

Public signing fingerprint: [release certificate](RELEASE_SIGNING.md).
Private `.signing/semesta-release.jks` and `local.properties` must be backed up
securely and never committed. Moving from the debug installation requires a
complete ZIP backup before uninstalling it; Android rejects different certificates.

## GitHub and external configuration

The confirmed public repository is `LeoAristocrat/Semesta`. Source preparation is
local: no commits/tags or release assets have been pushed, no GitHub Release has
been published, and no signing secrets have been uploaded. Hosted Actions runs
remain to be verified after push. See [release instructions](RELEASING.md).

Google authentication/cloud backup require owner-supplied Firebase/OAuth setup and
have not been verified against a live backend. Notification permission was tested;
timed delivery and OEM background restrictions were not validated here. Play
Console publication and its policy checks are separate from this signed build.
