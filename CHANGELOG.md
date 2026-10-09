# Semesta changelog

## [Unreleased]

### Improvements
- Ship English-only app resources, dates, notifications and release notes; normalize legacy language preferences while preserving other backup data.
- Translate font documentation to FONTS.md and preserve all font authors and license text.
- Remove GitHub Actions workflows and document manual signed builds and GitHub Releases.

## [1.0.0] - 2026-10-09

### Features
- Introduce Semesta's official graduation-cap / S-ribbon identity, a native student command center, adaptive phone/tablet navigation and a consistent Compose design system.
- Organize subjects, weighted grades, tasks, timetable, attendance, notes with attachments, expenses and academic history in one local-first workspace.
- Add India-first programme suggestions, editable institutional grading and attendance rules, INR formatting, student fee plans, partial payments and due-date reminders.
- Personalize the dashboard and choose among 37 curated themes, light/dark/system appearance, dynamic colors, accents, density, typography and motion controls.
- Preserve complete local ZIP backups and required library/font attribution.

### Improvements
- Localize assessment setup labels instead of showing hardcoded Spanish text in English.
- Improve narrow layouts, larger-text navigation, deadline localization, attendance guidance, timetable labels and fee validation with reachable keyboard-aware actions.
- Remove the obsolete developer simulation panel from the student experience.

### Configuration
- Use `com.leoaristocrat.semesta` as the Android identity and `LeoAristocrat/Semesta` as the configured release repository.
- Google sign-in and cloud backup require the owner's Firebase/OAuth configuration. Hosted collaboration and AI previews are not live services.
- The new release signing key differs from the earlier debug APK. Export a complete local backup before moving from the debug installation or the previous application.
