# Official logo integration — 8 October 2026

Visual-review screenshots are retained locally under `docs/visual-review/` and excluded from Git. Capture paths below identify local evidence; a fresh clone does not contain those images.

Semesta now uses the supplied blue/white graduation-cap and S-ribbon artwork.
The original is retained byte for byte in [branding](../branding/README.md).
No new artwork was generated. The previous orbital S vector was removed from
production resources, Compose drawing code and the design reference.

The shared `SemestaLogoMark` covers the dashboard, adaptive rail, welcome screen,
setup completion/transition, Compose launch screen and About. It uses an untinted
image, keeps the aspect ratio and exposes the localized app-name description.
Android launcher, system splash and notification resources retain their existing
resource names, so notification and startup callers continue to work.
The monochrome launcher layer and status icon use the supplied mark's alpha
silhouette; Android applies their tint. Theme accents remain user-selectable.

## Verification

- `assembleDebug`, `assembleDebugAndroidTest`, `verifyDesignTokens`: passed.
- `testDebugUnitTest`: 580 tests, zero failures/errors.
- Phone instrumentation: launch smoke, dashboard semantics and acceptance;
  three tests passed, 66.866 seconds.
- Tablet instrumentation: the same acceptance flow passed at 1920×1200,
  density 320; one test, 31.926 seconds. This also checks navigation, fee
  validation/payment persistence, light/dark appearance and activity recreation.
- baoyu design-system checker: clean. Regenerated the self-contained review page;
  the served HTML and logo asset returned HTTP 200. The Codex browser preview
  tool failed to initialize; native Android screenshots are the visual evidence.
- Inspected the actual installed launcher, system splash, onboarding replay,
  dashboard in both appearances, tablet rail and About. Inspected launcher mask
  variants and a tinted notification silhouette. The notification-shade preview
  was posted by Android's shell with the prepared icon; it does not certify real
  scheduled reminder delivery.
- Original file SHA-256 matches the supplied file. APK contains the new color
  artwork and separate monochrome resources. No old vector path remains in the
  production source or current design HTML.

Full lint was not rerun for this focused logo update. The preceding complete
redesign lint result is recorded in [validation](VALIDATION.md).
During manual display-size recreation the emulator produced an input-focus ANR;
the interrupted/blank captures were excluded. The subsequent tablet acceptance
run passed. The local diagnostic remains in `semesta-logo-anr.log`.
This review does not establish physical-device performance.

## Native visual evidence

| Surface | Capture |
|---|---|
| Launcher | Installed icon (`visual-review/logo/launcher.png`) |
| System splash | Actual launch (`visual-review/logo/system-splash.png`) |
| Onboarding | Walkthrough replay (`visual-review/logo/onboarding-dark.png`) |
| Dashboard | Light (`visual-review/logo/dashboard-light.png`), dark (`visual-review/logo/dashboard-dark.png`) |
| Tablet shell | Light (`visual-review/logo/tablet-light.png`), dark rail (`visual-review/logo/tablet-dark.png`) |
| About | Dark (`visual-review/logo/about-dark.png`) |
| Monochrome | Notification tint preview (`visual-review/logo/notification-icon.png`) |
| Asset sizing | Masks and small sizes (`visual-review/logo/android-asset-review.png`) |

Earlier redesign captures in `visual-review` remain historical evidence, rather
than being altered to simulate the new logo. Current logo captures are above.

## Updated artifact

- `app/build/outputs/apk/debug/Semesta-0.0.0-dev.local-debug.apk`
- Size: 41,260,839 bytes
- SHA-256: `a97e4b7a9d0da5e602e10fa71d8fa60988b36a50c4a92e0cd7ad4d17d1aa463c`
- Standard Android debug signing; this replaces the previous development APK.

Local logs: `semesta-logo-build.log`, `semesta-logo-native.log`,
`semesta-logo-tablet.log`. Existing backend/release configuration requirements
in [configuration](CONFIGURATION.md) still apply.
