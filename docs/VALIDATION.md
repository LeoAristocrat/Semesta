# Semesta validation — 8 October 2026

## English-only cleanup — 9 October 2026

The current debug source passed assembly, design-token verification and **584 unit
tests with zero failures, errors or skips**. Full Android lint passed with zero
errors; 412 existing warning/informational findings remain. The final follow-up
changed only four onboarding text fragments and their regression test, and reran
assembly and the full unit suite. Android resources now preserve spaces around
highlighted words in the name, module explanation and completion sentences.

Native review on the isolated Android 36.1 phone emulator completed English
onboarding for a synthetic B.Tech/B.E. Computer Science profile with a 10-point
scale and editable 30/70 assessment weights. The accessibility formats group was
inspected in light and dark modes: clock, date format and INR currency remain;
there is no language selector. Captures remain local in
`docs/visual-review/english-only/` and are excluded from Git.

Compatibility tests cover a Spanish device configuration, legacy saved language
choices, background resource resolution, and restored currency/date/accessibility
preferences. The shipped changelog and its fallback asset are identical English
published notes. Font authors and the complete OFL text were retained when moving
`FUENTES.md` to `FONTS.md`. Spanish documents/resources and all GitHub workflow files
are removed. Releases are manual; see [release instructions](RELEASING.md).

The original signed 1.0.0 APK and bundle have not been rebuilt or retagged. Their
historical verification remains in [release validation](RELEASE_VALIDATION.md).


Visual-review screenshots are retained locally under `docs/visual-review/` and excluded from Git. Capture paths below identify local evidence; a fresh clone does not contain those images.

The official-logo update is documented in [logo integration](LOGO_INTEGRATION.md), with current native captures and verification. Older screen captures below are historical evidence of the redesign before the supplied official logo.

This is a native Compose debug build using the Semesta identity and India-first configuration. Review data was created through tests and manual review only on an isolated Android 36.1 emulator. It is absent from the shipped app. No production credentials or release repository were fabricated.

## Completed gates

| Gate | Result |
|---|---|
| assembleDebug, assembleDebugAndroidTest | Passed |
| testDebugUnitTest | 580 tests; 0 failures; 0 errors |
| lintDebug (before official-logo update) | 0 errors; 418 warnings; 35 hints |
| verifyDesignTokens | Passed |
| baoyu design-system checker | Clean |
| Native instrumentation | 3 phone tests passed on the official-logo APK; acceptance test also passed at tablet size |

The combined build completed successfully. Logs are local, ignored files: semesta-delivery-validation.log and semesta-complete-native.log for the original redesign; semesta-logo-build.log, semesta-logo-native.log and semesta-logo-tablet.log for the supplied logo update. The logo build reran assembly, 580 unit tests and design token checks; full lint was not rerun for this focused asset update. Generated JVM XML reports and lint HTML/XML remain under app/build. Warnings remain and should not be interpreted as a warning-free release.

Native tests: SemestaLaunchSmokeTest, HomeDailyFocusPanelTest and SemestaAcceptanceTest. They exercise launch/primary navigation, dashboard semantics, real repository fixtures, a rejected overpayment, a saved cumulative payment, light/dark appearance and activity recreation. After recreation the fee payment, attendance target, task and note remain intact. Existing JVM suites cover calculations, Room migrations/repositories, backup/archives, notes/attachments, tasks, themes, navigation, preferences and update logic. IndiaStudentSupportTest adds INR lakh/crore formatting and large totals, explicit 10-point grades, attendance recovery/buffer boundaries, fee validation and JSON round trips.

## Native visual review

Actual emulator screenshots were inspected, rather than treating compilation as visual approval. Default phone: 720×1600 at density 320 (360dp wide), small phone: 640×1280 at font scale 1.3 (320dp wide), landscape tablet: 1920×1200 at density 320 (960dp wide). Onboarding was also reviewed at 320dp with font scale 1.3, including its scrollable content and fixed primary action. An earlier review covered a 420dp phone at font scale 1.3.

| Area | Evidence |
|---|---|
| Splash / onboarding | Splash (`visual-review/clean-check.png`), welcome (`visual-review/onboarding.png`), larger text (`visual-review/onboarding-small-large-text.png`), study areas (`visual-review/onboarding-study-areas.png`) |
| Dashboard | Light (`visual-review/final-native/dashboard-light.png`), dark (`visual-review/final-native/dashboard-dark.png`) |
| Subjects / grade empty state | Light (`visual-review/final-native/study-light.png`), dark (`visual-review/final-native/study-dark.png`) |
| Timetable / attendance catch-up | Schedule (`visual-review/final-native/schedule-light.png`) |
| Tasks / deadline sheet | List (`visual-review/tasks-dark.png`), sheet (`visual-review/task-detail-dark.png`), creation (`visual-review/task-form-dark.png`) |
| Notes / keyboard | Pinned note (`visual-review/notes-dark.png`), editor (`visual-review/note-editor-dark.png`) |
| Expenses / empty chart | INR overview (`visual-review/final-native/expenses-light.png`), creation (`visual-review/expense-form-dark.png`) |
| Fees / partial payments / validation | Light (`visual-review/final-native/fees-light.png`), dark (`visual-review/fees-dark.png`), editor (`visual-review/fee-editor-dark.png`), settled validation (`visual-review/fee-validation-dark.png`) |
| Settings / academic rules | Settings (`visual-review/final-native/settings-light.png`), 10-point scale (`visual-review/grading-dark.png`), attendance (`visual-review/attendance-settings-dark.png`) |
| Appearance / identity | 37 themes (`visual-review/themes-dark.png`), About (`visual-review/about-dark.png`), account (`visual-review/account-dark.png`) |
| Responsive shell | Small phone with larger text (`visual-review/small-large-text-dark.png`), tablet (`visual-review/tablet-dark.png`), tablet metrics (`visual-review/tablet-modules-dark.png`) |
| Subject form | Creation (`visual-review/subject-form-dark.png`) |

Review iterations corrected a split legacy brand in the theme preview, narrow dashboard date columns, navigation labels wrapping at larger text sizes, attendance guidance, task deadline strings that bypassed localization, broken words in narrow timetable cells, fabricated-looking bars in an empty expense chart, and a fee focus-clearing call that targeted the parent screen instead of the dialog. Timetable accessibility exposes full subject, room and time even when its visible name is abbreviated. Physical taps verified that the fee editor resizes around the keyboard, Save is reachable, overpayments are rejected, and Save dismisses the keyboard. Test captures may precede the IME's final animation frame; settled manual validation is linked above.

The headless emulator initially had cold DEX/System UI startup ANRs and an ADB server version conflict. Package compilation and an isolated ADB server resolved the review environment. Screenshots containing OS error dialogs were excluded and deleted. This run is not a physical-device performance benchmark.

## Preservation and identity

All 41 source files under data/local match the pre-migration snapshot after normalizing only package/class identity. Room schema remains 25. Bundled OFL and Apache notices match byte for byte; third-party attribution remains. Production DEX contains no BancoDePruebas, NoteSamples or TaskAttachmentSamples. Student features and existing records were not removed by deleting those generators.

Application/namespace, application class, launcher/notification resources, shortcuts, developer/support identity and exports use Semesta. The intentionally retained database/preference/channel/ZIP names and legacy theme alias are documented in [identity migration](IDENTITY_MIGRATION.md). A new application ID requires importing an existing complete ZIP backup; it cannot read another app's private sandbox.

## Artifact

- APK: app/build/outputs/apk/debug/Semesta-0.0.0-dev.local-debug.apk
- Size: 41,260,839 bytes
- SHA-256: `a97e4b7a9d0da5e602e10fa71d8fa60988b36a50c4a92e0cd7ad4d17d1aa463c`
- Signing: standard Android debug key. This is an installable development artifact, not a production-signed release.

## Remaining external verification

Owner configuration is still required for Firebase/Google OAuth, Firestore rules, the actual release repository and production signing. See [configuration](CONFIGURATION.md). Remote sign-in/cloud restore, real scheduled notification delivery, release download/install, system file/media imports on representative devices and physical-device performance have not been certified by this run. The existing cloud JSON backup excludes binary attachments; complete local ZIP backups include them. Hosted room collaboration and AI are not represented as live services.

Indian programme suggestions and academic defaults remain editable. They do not constitute a comprehensive approval directory, an official fee quote, a marks-to-CGPA conversion or certification of exam eligibility. See [India-first rules](INDIA_EDUCATION.md).
