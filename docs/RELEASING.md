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

The remote already exists. Review and commit local changes, then push when ready:

```powershell
git config user.name "LeoAristocrat"
git config user.email "sayeemlaskar786@gmail.com"
git status
git add -A
git commit -m "chore: make Semesta English-only and remove GitHub Actions"
git push origin main
```

GitHub authentication is handled by your Git client. No credential is embedded in
the remote URL. APKs, bundles, mapping files, signing files, local configuration,
emulators, logs, design prototypes, visual-review screenshots and one-time migration
scripts are excluded from source commits. The prototypes and screenshots remain
available locally in `designs/` and `docs/visual-review/`.

## Manual releases only

There are no GitHub Actions workflows in this repository. Builds, checks and release
uploads are performed manually. No signing secrets need to be uploaded to GitHub.
To disable the platform feature as well, open the repository's Settings > Actions >
General, select Disable actions under Actions permissions, and save.

## Publish a release separately

For each new release, update `VERSION` and add its English notes to `CHANGELOG.md`. The version name determines an increasing Android
version code automatically; keep any explicit override higher than the published code.
Build and verify with the existing signing key, commit the changes, create a new annotated tag, and push that tag manually. Do not
move a tag or replace artifacts for an already published release.

In GitHub, open **Releases > Draft a new release**, choose the new tag and paste the
corresponding English notes from `CHANGELOG.md`. Attach the matching signed APK and
its checksum, then click **Publish release** when ready. The updater needs that actual
release asset; an empty repository or a pushed tag alone provides no download.
Store the bundle and mapping securely for Play Console/crash analysis as needed.

Google authentication and cloud backup still require the owner's Firebase/OAuth
configuration and certificate fingerprints. Signed packaging does not configure
those services. See [configuration](CONFIGURATION.md). Google Play distribution
also requires completing the applicable Play Console setup and policy checks.

For a later release, update `VERSION` and matching sections in `CHANGELOG.md`, build and verify, then create the release commit and annotated tag.
Keep third-party font and library notices. No blanket open-source license for
first-party code has been selected; choose one explicitly if granting reuse rights.
