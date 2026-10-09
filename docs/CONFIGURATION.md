# Semesta ownership and deployment configuration

The repository is `LeoAristocrat/Semesta`. A dedicated release key was generated locally for Semesta 1.0.0; its keystore and passwords are ignored by Git. Firebase configuration and an OAuth client are not supplied in this workspace. Local features work without an account. The following configuration is intentionally left explicit.

## Firebase and Google sign-in

Register the Android application `com.leoaristocrat.semesta` in the Firebase project you control. Add the SHA fingerprints of the actual debug/release signing keys, enable Google authentication, and download that app’s configuration to `app/google-services.json`. The Google Services Gradle plugin applies only when this file exists.

Set `googleWebClientId` in untracked `local.properties` to the corresponding **web/server OAuth client ID** used by Credential Manager. The app does not contain a fabricated ID. Test sign-in, cancellation, sign-out and restore with the configured project before distribution.

Cloud backup uses Firestore and the existing local JSON format. The current document path is `users/{uid}/backups/current`; access must be scoped to the authenticated account’s UID. Do not expose all backups through public rules. Binary attachments are not uploaded by this repository; use complete local ZIP backups for them. Migrating to a different Firebase project does not transfer existing Firebase users or cloud documents automatically. Preserve an offline complete backup first and migrate ownership/data using the service’s supported tools.

Configuration files and credentials belong in ignored local files. Do not commit private keys, service-account files, tokens or signing passwords. Client Firebase configuration is not a substitute for restrictive service rules.

## Update repository

The confirmed repository slug `LeoAristocrat/Semesta` is configured in `gradle.properties`. Override it when needed through one of:

- Gradle property `semestaRepository` (for example in local.properties).
- Environment variable `SEMESTA_REPOSITORY`.
- Command property `-PsemestaRepository=LeoAristocrat/<actual-repository>`.

The slug must belong to `LeoAristocrat`. The remote repository exists; publishing its APK releases is a separate action. The updater retains channel parsing, download handling, package identity and signing-certificate checks. Publish signed APK assets manually through GitHub Releases. This flow needs no repository token or GitHub Actions signing secrets. No old release infrastructure is used by default. Do not substitute a literal placeholder into a production build.

The new identity cannot install as an in-place update over a different package. After the first Semesta release, retain the same signing key for subsequent APK updates.

## Production signing

This machine has the four values in ignored `local.properties`. The private key is `.signing/semesta-release.jks` with alias `semesta`. Back up both files securely. A fresh checkout must configure the same key and these four values in ignored `local.properties`, or their environment equivalents:

| Property | Environment variable |
|---|---|
| releaseStoreFile | RELEASE_STORE_FILE |
| releaseStorePassword | RELEASE_STORE_PASSWORD |
| releaseKeyAlias | RELEASE_KEY_ALIAS |
| releaseKeyPassword | RELEASE_KEY_PASSWORD |

Release assembly and bundling reject missing signing configuration, default development credentials, an absent keystore, missing release notes or a missing explicit public version. Use `-PversionName=1.0.0` for this release. Debug builds use the normal debug key. The default local version is stable (`0.0.0-dev.local`); use `-PdevBuildLabel=<label>` for a distinct local artifact without changing release version ordering. Existing explicit distribution tasks remain available when configured; ordinary assembly does not invoke Telegram or publish externally.

## Backup compatibility

Room schema remains version 25 and its migration chain is preserved. The database filename and preference storage identifiers remain compatible with exported data. Complete ZIP archives continue to accept the legacy JSON entry name. New export filenames identify as Semesta.

New appearance fields are optional on read. Older backups gain safe defaults for dynamic color and surface appearance; existing theme selections remain valid, including the alias for the former default theme. Semesta ships English resources only. Legacy SYSTEM/SPANISH language preferences normalize to English without changing other saved settings. Serialized legacy identifiers remain readable for backup compatibility.

## Integrations requiring device verification

Verify configured Google authentication/Firestore against your project, notification permission and scheduled reminders on representative devices, APK downloads with a release signed by your key, and file/photo/audio imports through Android’s system pickers. These cannot be proven by a JVM build alone.

Manual build and release instructions are in [releasing](RELEASING.md). Signing certificate fingerprints are public; passwords and private key material must remain local.
