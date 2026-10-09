"""Prepare Android assets from Semesta's supplied logo; never redraw the artwork.

Run: python scripts/prepare_brand_assets.py
Requires Pillow. The original is retained byte for byte in branding/.
"""

from pathlib import Path
from PIL import Image

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "branding/semesta-logo-original.png"
RES = ROOT / "app/src/main/res"
DENSITIES = {"mdpi": 1, "hdpi": 1.5, "xhdpi": 2, "xxhdpi": 3, "xxxhdpi": 4}


def framed(art, canvas_size, art_size):
    canvas = Image.new("RGBA", (canvas_size, canvas_size))
    fitted = art.copy()
    fitted.thumbnail((art_size, art_size), Image.Resampling.LANCZOS)
    canvas.alpha_composite(fitted, ((canvas_size - fitted.width) // 2,
                                  (canvas_size - fitted.height) // 2))
    return canvas


def save(image, path):
    path.parent.mkdir(parents=True, exist_ok=True)
    image.save(path, optimize=True)


def main():
    original = Image.open(SOURCE).convert("RGBA")
    # The source has near-invisible alpha specks far outside the actual mark.
    # Crop the transparent margin only; retain the original pixels within it.
    bounds = original.getchannel("A").point(lambda a: 255 if a > 8 else 0).getbbox()
    art = original.crop(bounds)
    # Android tints monochrome/status icons. Preserve the silhouette and holes,
    # not the glossy RGB shading (which must never be used as an alpha mask).
    silhouette = Image.new("RGBA", art.size, "white")
    silhouette.putalpha(art.getchannel("A").point(lambda a: 255 if a >= 128 else 0))

    save(framed(art, 768, 736), RES / "drawable-nodpi/semesta_logo.png")
    save(framed(art, 512, 490), ROOT / "designs/semesta/assets/semesta-logo.png")
    # Adaptive layers are 108dp. Artwork stays inside the central safe circle.
    # System splash is 288dp with a 192dp safe circle (no icon background).
    for density, scale in DENSITIES.items():
        folder = RES / f"drawable-{density}"
        save(framed(art, round(108 * scale), round(60 * scale)),
             folder / "ic_launcher_foreground.png")
        save(framed(silhouette, round(108 * scale), round(60 * scale)),
             folder / "ic_launcher_monochrome.png")
        save(framed(art, round(288 * scale), round(180 * scale)),
             folder / "semesta_symbol.png")
        save(framed(silhouette, round(24 * scale), round(22 * scale)),
             folder / "ic_stat_semesta.png")

    store = Image.new("RGBA", (512, 512), "#F7F8FC")
    store.alpha_composite(framed(art, 512, 420))
    save(store.convert("RGB"), ROOT / "branding/semesta-store-icon.png")


if __name__ == "__main__":
    main()
