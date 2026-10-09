# Semesta project audit

Audit date: 7 October 2026. Source snapshot: `migration-snapshot.zip` (local, excludes secrets, skills and generated outputs).

## Foundation

One Android application module, Kotlin, Jetpack Compose, feature-first packages, Hilt, StateFlow ViewModels and lifecycle-aware collection. Entry points: MainActivity, application class, RootNavGraph (setup vs main), MainNavGraph. Preserve route contracts and module access guards.

Room schema version 25 stores subjects, weighted grades, task subtasks/attachments, expenses, schedules, occurrences, attendance, agenda, semesters/breaks, notes/attachments, academic works and work rooms. Existing migrations and entity contracts are retained. Preferences DataStore stores profile, language, accessibility, appearance, dashboard order and motion. Preserve stored keys, enum IDs and archival formats; renaming code does not require renaming stored data.

## Feature inventory / disposition

| Area | Preserve | Redesign / improve |
|---|---|---|
| Setup | profile, academic scales/cuts, modules, permissions, replay | identity, visual hierarchy, welcome and completion |
| Home | priority engine, timeline, semester lifecycle, quick creation, configurable modules | command center, metrics strip, responsive grouping |
| Academic | subjects, assessments, honest grade floor/ceiling, history, task links | compact rows, typography, shared forms and states |
| Tasks | filters, sorting, checklists, attachments, grading status | list hierarchy, editor consistency |
| Schedule | weekly timetable, agenda, attendance, catch-up | contextual class emphasis, responsive shell |
| Notes | markdown, search, grouping, checklist, recording, photos/files, reminders | list/editor surfaces and empty states |
| Expenses | budgets, insights, categories, CSV | typography, metrics and presentation |
| Terms | closing/undo, next term, prior grades, bulletin/PDF/images | preserve calculations and archives, rebrand exports |
| Workspaces/templates | local rooms, invitations/QR, files, academic exports | retain all routes, update invitation identity |
| Settings | account, notifications, data, accessibility, motion, module controls | common hierarchy, discoverable themes and personalization |
| Support | calculator, resources, help, changelog, OSS licenses, crash reporting | About, first-party support via canonical email |
| Updates | release parsing, channels, worker, download, package/signature verification | configurable owner repository; clear unconfigured state |

## Integrations and configuration

Firebase Auth + Credential Manager / Google ID; Firestore cloud backup wraps the same local JSON archive. No google-services.json or OAuth client configuration is present. Cloud backups exclude binary attachments (local ZIP includes them). Work rooms currently use local persistence; QR invitations are not a hosted collaboration service. AI/labs contain preview/planned functionality; do not claim live AI or collaboration.

GitHub update repository and Gradle publisher reference a former first-party repository. Replace with explicit `semestaRepository` configuration; no guessed repository. Existing download checks verify package identity and signing certificates and must remain.

Gradle release signing had development fallback credentials; debug used that release key. Separate normal Android debug signing and explicit release signing. Remove automatic Telegram sending from ordinary release assembly. Keep explicit distribution tasks available when configured.

Permissions: Internet, notifications, install packages, download without system notification, boot, exact alarms, vibration, audio. Preserve just-in-time permission flows. Shortcuts use explicit target package; reminders use internal pending intents and route extras. Room invitations emit QR scheme links but no external routing receiver is declared; do not imply those are working join links.

## Identity / compatibility

Migrate package declarations, namespace, application ID, application/theme/entry-point/component class names, ProGuard rules, manifest, shortcuts, notification icon, share text, exports, About, first-party URLs and documentation. Preserve required bundled font OFL notices, Apache license and generated AboutLibraries attribution.

Changing application ID creates a separate Android sandbox. Existing users must export a complete local backup from the previous installation and import it into Semesta, including attachments. Automatic access to another app's private data is unavailable. Keep the database filename, preference storage names and old ZIP data member as internal compatibility identifiers; document any remaining legacy strings.

English resources are the only shipped language. Retain resource IDs and localization architecture; normalize legacy language preferences to English. Do not translate data or change dates/grade scales silently.

## Fragile areas / validation

Pinned Material 3 Expressive alpha APIs are used broadly: keep dependency versions during redesign. Daemon configuration was tied to an unavailable JetBrains JDK download; use installed JDK 21. Baseline build initially failed before compilation on that download. No device was connected at audit start; check local AVD availability for visual/device validation. Existing test suites cover Room migrations/repositories, calculations, tasks, notes, preferences, backup, navigation, localization, update/version logic and device flows.

## Execution checklist

- [x] Foundation / identity / integration audit
- [x] Native design tokens and shared components
- [x] Package / first-party identity migration
- [x] Adaptive shell and command center
- [x] Major feature screen refinements
- [x] Themes and customization
- [x] Onboarding, About, support and backend configuration (external credentials remain owner-supplied)
- [x] Build, tests, native visual validation and final audit — see [validation](VALIDATION.md); owner service/release configuration and physical-device checks remain explicit
