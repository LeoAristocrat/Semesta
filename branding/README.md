# Semesta official logo

The graduation-cap / S-ribbon logo was supplied and selected by the product owner.
`semesta-logo-original.png` is a byte-for-byte copy of that supplied RGBA PNG
(1254 × 1254 pixels). Its original SHA-256 is
`30139e09e1778efcea7540c090a6d6ea2acbf7c82da5caaa70f144ca091036d6`.
It has a transparent background; no replacement artwork was generated.

Run `python scripts/prepare_brand_assets.py` with Pillow to reproduce the assets.
Asset preparation removes the transparent outer margin, centers the visible mark,
and resamples it. It preserves the blue/white shading and aspect ratio. The crop
uses alpha > 8 to disregard nearly invisible stray pixels outside the visible mark;
the untouched original remains the authority.

- Compose: transparent 768px asset, no theme tint, accessible app-name description.
- Launcher: 108dp adaptive foreground with 60dp artwork and a neutral paper
  background, plus a separate monochrome alpha silhouette for themed icons.
- Android 12+ splash: 288dp image with 180dp artwork, the same image in light/dark.
- Notifications: white alpha silhouette at 24dp in all five density buckets.
  Android applies the notification tint; full-color artwork is unsuitable here.
- Local design specimen (excluded from Git): transparent asset in `designs/semesta/assets`.
- Distribution: `semesta-store-icon.png`, 512px opaque square with the official
  artwork on paper; it is an export asset, not a published store listing.

Sizing follows Android's [adaptive icon guidance](https://developer.android.com/develop/ui/compose/system/icon_design_adaptive)
and [splash screen guidance](https://developer.android.com/develop/ui/views/launch/splash-screen).
The logo keeps its original colors across user-selected themes. UI accent colors,
theme choices, user data and third-party notices are independent of the logo.
