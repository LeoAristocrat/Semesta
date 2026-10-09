# Contributing to Semesta

Semesta is a native Android/Jetpack Compose application. Preserve its local-first
repositories, Room migration chain, backup compatibility and existing features.
Use centralized design tokens and shared components; put user-visible text in
localized resources and check light/dark appearance, larger text and narrow layouts.

Configure JDK 21 and Android SDK 36. Debug checks need no signing or Firebase secrets:

```sh
./gradlew testDebugUnitTest lintDebug verifyDesignTokens assembleDebug
```

Run relevant device tests and visual review for UI/navigation changes. Do not
commit credentials, private signing keys, Firebase/service-account configuration,
local machine paths or generated artifacts. Preserve bundled font notices and
the application's library attribution. See [release instructions](docs/RELEASING.md)
for ownership, signing and release builds.
