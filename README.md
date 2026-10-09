# Semesta

Your studies, in focus.

Semesta is a native Android student productivity workspace, designed and developed by **Sayeem Sadik / Leo Aristocrat**. Built primarily for Indian students, it brings courses, weighted grades, tasks, timetable, attendance, notes, expenses and academic history into one local-first workspace.

[Portfolio](https://leoaristocrat.eu.cc/) · [Email](mailto:leoaristocratjr@gmail.com)

## Product

- A student command center prioritizes the next class, deadlines and academic status. Dashboard sections can be hidden and reordered.
- Course and grade tracking retains weighted periods, targets, floor/ceiling calculations, archived semesters and exports.
- Tasks retain subtasks, attachments, filters, grading status and reminders.
- Schedule and agenda retain attendance records, locations and catch-up flows.
- Notes retain markdown, checklists, course tags, photos, files, recording and reminders.
- Expenses use Indian rupees by default, with lakh/crore grouping, hostel and coaching categories, budgets, charts and CSV exports.
- Fees and instalments track personal tuition, examination, hostel/mess or other obligations, partial payments, due dates and deadline reminders.
- School streams, UG/PG, professional, diploma/ITI and exam-preparation programmes have India-first suggestions with free-text alternatives. Marks out of 100, 10-point grades, assessment weights and minimum attendance remain institution-specific.
- Local work rooms and academic templates remain available. Room invitations are local utilities; hosted collaboration and AI previews are not live services.
- Light, dark, system and AMOLED appearance, 37 curated themes, wallpaper colors on Android 12+, custom accents, background tones, surface styles, density, text scale and motion controls.
- Accessibility includes contrast controls, colorblind palettes, shape cues, reading fonts and reduced motion.

## Native architecture

One Android app module; Kotlin, Jetpack Compose / Material 3 Expressive, Hilt, StateFlow ViewModels, Room schema 25 and Preferences DataStore. Android 8.0+ (API 26); compile SDK 36. Identity: `com.leoaristocrat.semesta`.

The redesigned application uses the official graduation-cap / S-ribbon identity, Inter typography, neutral surfaces and semantic color. Phones use bottom navigation; wider windows use a navigation rail and bounded content, with a two-column command center on expanded windows. The native implementation remains the product. Design prototypes and visual-review screenshots are local development artifacts excluded from Git.

## Build and configuration

Install Android SDK 36 and a JDK 21. Set `sdk.dir` in an untracked `local.properties` when necessary, then run:

```powershell
./gradlew.bat testDebugUnitTest assembleDebug
./gradlew.bat connectedDebugAndroidTest
```

To build a signed release after configuring your private key:

```powershell
$releaseVersion = (Get-Content VERSION -Raw).Trim()
./gradlew.bat assembleRelease bundleRelease "-PversionName=$releaseVersion"
```

Debug uses the standard Android debug key. Release builds require explicitly configured production signing; assembly never sends an APK automatically. See [configuration](docs/CONFIGURATION.md) for signing, Firebase, Google sign-in and release ownership.

The configured release repository is [LeoAristocrat/Semesta](https://github.com/LeoAristocrat/Semesta). Update availability depends on releases actually being published there. First release version: `1.0.0`, recorded in `VERSION`. See [release and GitHub instructions](docs/RELEASING.md).

## Existing user migration

Moving from the earlier Semesta debug APK also requires a complete backup: its debug signing certificate differs from the new release certificate, so Android will reject installing the release over it. Export the backup, uninstall the debug app, install the signed release, then restore. Future release APKs must use the same release key.

The new application ID creates a separate Android data sandbox. Before moving, export a **complete local ZIP backup** from the previous installation. Install Semesta, import that ZIP through Data settings, and verify courses, grades, tasks, attendance, notes and attachments before removing the previous installation. JSON/cloud backups do not include binary attachments.

Database filenames, stored preference keys and the ZIP data member retain their legacy identifiers for compatibility. User data, enum IDs, Room migrations and backup contracts are preserved. These internal identifiers are not first-party product branding.

## Documentation and attribution

[Signed release verification](docs/RELEASE_VALIDATION.md) · [Validation](docs/VALIDATION.md) · [India-first product rules](docs/INDIA_EDUCATION.md) · [Project audit](docs/PROJECT_AUDIT.md) · [Configuration](docs/CONFIGURATION.md) · [Font attribution](FUENTES.md) · [Release notes](CHANGELOG.md)

Bundled fonts retain their license notices. Open-source library attribution remains available in the application’s Licenses screen. Third-party palettes retain their published names and attribution. First-party identity is Semesta / Sayeem Sadik / Leo Aristocrat; third-party work remains credited to its authors.

The supplied official logo is preserved unchanged in `branding/semesta-logo-original.png`. Android launcher, splash, notification and Compose assets are prepared by `scripts/prepare_brand_assets.py`; see [brand asset notes](branding/README.md).
