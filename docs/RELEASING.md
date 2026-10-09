# Building and publishing Semesta

Repository: https://github.com/LeoAristocrat/Semesta

The first Semesta release is **1.0.0** (`VERSION`). The local repository uses `main`
and the confirmed repository URL as `origin`. Preparing the project does not push
commits, publish a GitHub Release or upload signing secrets.

## Keep the signing key

This machine's private signing files are:

- `.signing/semesta-release.jks`, alias `semesta`.
- `local.properties`, which contains the generated passwords.

Both are excluded from Git and restricted to the current Windows account. Copy
both to secure backup storage before relying on this release. Do not put them in
the source repository, a release attachment, chat or issue. Keep the same key for
subsequent APK updates. See Android's [app signing guidance](https://developer.android.com/studio/publish/app-signing).
The key uses RSA 4096 / SHA256withRSA with a 10,000-day certificate validity.

A new checkout can copy `local.properties.example` to `local.properties`, then
configure the existing private key. `RELEASE_STORE_FILE`, `RELEASE_STORE_PASSWORD`,
`RELEASE_KEY_ALIAS` and `RELEASE_KEY_PASSWORD` are supported environment alternatives.
Local properties take precedence; do not leave stale signing entries there.

## Local build

Install JDK 21 and Android SDK platform 36. The wrapper is pinned to Gradle 8.14.5
and its official distribution checksum. From the project root in PowerShell:

```powershell
$releaseVersion = (Get-Content VERSION -Raw).Trim()
./gradlew.bat assembleRelease bundleRelease testReleaseUnitTest lintRelease verifyDesignTokens "-PversionName=$releaseVersion" --max-workers=1
```

The APK is under `app/build/outputs/apk/release/`; the bundle is under
`app/build/outputs/bundle/release/`. Keep `app/build/outputs/mapping/release/mapping.txt`
with each release to decode R8 crash traces. Assembly uses shrinking/minification
and never sends an APK automatically. An explicit public version, matching release
notes and real signing configuration are required for APK and bundle builds.

Verify the APK with the SDK's [apksigner](https://developer.android.com/tools/apksigner):

```powershell
& "$env:ANDROID_HOME/build-tools/36.0.0/apksigner.bat" verify --verbose --print-certs "app/build/outputs/apk/release/Semesta-$releaseVersion.apk"
```

The signed release cannot overwrite the previous Semesta debug APK: the certificates
differ. Export a **complete local ZIP backup**, uninstall the debug app, install the
release and restore the backup. Do not uninstall a real installation before verifying
that its backup includes notes, recordings and other attachments. Later releases
signed with this same release key can update it normally.

## Push the prepared source

The remote already exists. Review `git status`, then push when ready:

```powershell
git push -u origin main
git push origin v1.0.0
```

GitHub authentication is handled by your Git client. No credential is embedded in
the remote URL. APKs, bundles, mapping files, signing files, local configuration,
emulators, logs, design prototypes, visual-review screenshots and one-time migration
scripts are excluded from source commits. The prototypes and screenshots remain
available locally in `designs/` and `docs/visual-review/`.

## GitHub Actions

`Android checks` runs on pushes to `main` and pull requests. It builds the debug
APK, runs unit tests, lint and design-token verification, and uploads reports.
It has read-only repository permissions and requires no signing secrets.

`Signed Android build` runs only through **Run workflow**, on `main`, using the
`release-signing` environment. Configure that environment and its secrets before
running it:

| Secret | Value |
|---|---|
| `SEMESTA_KEYSTORE_BASE64` | Base64 encoding of the existing private keystore |
| `RELEASE_STORE_PASSWORD` | Existing store password from local.properties |
| `RELEASE_KEY_ALIAS` | `semesta` |
| `RELEASE_KEY_PASSWORD` | Existing key password from local.properties |

Use GitHub's secure secret settings; do not commit a Base64 key file. The workflow
restores the key only in the runner's temporary directory, excludes it from uploaded
artifacts and deletes it in cleanup. It uploads the signed APK, bundle, R8 mapping,
signature verification and checksums. It does **not** publish a GitHub Release.
It also checks that the APK uses the committed public certificate fingerprint,
preventing an accidentally substituted CI key from producing an incompatible update.
The workflows have been checked locally; their first hosted run happens after push.

## Publish a release separately

Create a GitHub Release for `v1.0.0` using the corresponding section in `CHANGELOG.md`.
Attach the signed `Semesta-1.0.0.apk` and its checksum. The updater needs that actual
release asset; an empty repository or a pushed tag alone provides no download.
Store the bundle and mapping securely for Play Console/crash analysis as needed.

Google authentication and cloud backup still require the owner's Firebase/OAuth
configuration and certificate fingerprints. Signed packaging does not configure
those services. See [configuration](CONFIGURATION.md). Google Play distribution
also requires completing the applicable Play Console setup and policy checks.

For a later release, update `VERSION` and matching sections in `CHANGELOG.md` and
`CHANGELOG.es.md`, build and verify, then create the release commit and annotated tag.
Keep third-party font and library notices. No blanket open-source license for
first-party code has been selected; choose one explicitly if granting reuse rights.
