# Semesta identity and data migration

The application package, namespace, source packages, explicit shortcut targets, application class, theme, notification art, launcher art, exports, share text, support destinations and first-party developer information use Semesta / `com.leoaristocrat.semesta`.

The former developer’s first-party website, release repository and Telegram support references have been removed. The new repository is deliberately unconfigured until its actual name is supplied. Required font notices, library attribution and third-party palette names remain.

## Intentional compatibility identifiers

These identifiers are internal data or OS contracts, not displayed product identity:

| Identifier | Reason retained |
|---|---|
| `unistack.db` | Database storage name; Room schema and migration chain remain unchanged |
| `unistack-copia.json` | Data member accepted in complete ZIP backups from the previous app |
| Former default theme ID alias | Existing appearance preferences resolve to the Semesta default |
| Notification/history/launch/backup/update SharedPreferences names | Existing storage and scheduling contracts |
| Notification channel IDs and update worker name | Preserve channel and scheduling behavior without duplicate jobs |
| Legacy note sheet preference name | Retain the existing note import path |

Compatibility tests explicitly exercise the former ZIP entry and theme ID. Source migration tooling also contains the original identity as input to the migration. None is a user-facing old product reference.

## Preservation evidence

A pre-migration source snapshot is retained locally as `migration-snapshot.zip` and is ignored. Comparison of all 41 source files under `data/local` found no change beyond source package/class identity. This includes entities, DAOs and Room migrations. Both bundled OFL and Apache license files match the snapshot byte for byte.

Changing application ID gives Semesta a new Android sandbox. Import a complete backup exported from the previous installation; Semesta cannot automatically read another application’s private database. Verify restored records and attachments before removing the previous installation.
