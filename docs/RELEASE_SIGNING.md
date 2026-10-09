# Semesta release certificate

These certificate details are public and safe to commit. Private key material and
passwords are excluded from the repository.
The CI comparison value is [release-certificate.sha256](../branding/release-certificate.sha256).

| Field | Value |
|---|---|
| Owner | CN=Sayeem Sadik, OU=Leo Aristocrat, O=Semesta, C=IN |
| Algorithm | RSA 4096, SHA256withRSA |
| Alias | semesta |
| Certificate validity | 9 October 2026 – 24 February 2054 |
| SHA-1 | `CB:C1:DE:9E:C6:F3:0F:5C:29:88:62:F5:DB:42:F6:5A:40:B7:B9:B5` |
| SHA-256 | `56:60:74:E6:05:10:80:A2:EA:CB:84:3A:FF:78:04:20:4E:12:A0:31:C3:DA:72:5A:7F:58:18:0E:4F:DF:EE:68` |

This is a dedicated release certificate, separate from Android's standard debug
certificate. Keep the private keystore and its credentials in secure backup storage.
See [releasing](RELEASING.md).

For owner-configured Firebase/OAuth, register the release fingerprint for
`com.leoaristocrat.semesta`. If Google Play App Signing uses a different distribution
certificate, register that certificate as well. These fingerprints do not configure
Firebase or make cloud features available by themselves.
