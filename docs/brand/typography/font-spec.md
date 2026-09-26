# Font Specification

## JetBrains Mono (UI Font)
- **Source**: `docs/brand/typography/resource-pack/JetBrainsMono-{Regular,Medium,Bold}.ttf`
- **License**: SIL Open Font License 1.1
- **Usage**: All UI text — buttons, labels, headings, code, module names
- **Weights**: Regular (400), Medium (500), Bold (700)
- **Features**: Monospace, programming ligatures, distinct glyphs

## Inter (Body Font)
- **Source**: `docs/brand/typography/resource-pack/Inter-{Regular,Medium,Bold}.ttf`
- **License**: SIL Open Font License 1.1
- **Usage**: Descriptions, settings explanations, longer form text
- **Weights**: Regular (400), Medium (500), Bold (700)
- **Features**: Variable font compatible, optimized for UI

## Press Start 2P (Pixel Font)
- **Source**: `docs/brand/typography/resource-pack/PressStart2P-Regular.ttf`
- **License**: SIL Open Font License 1.1
- **Usage**: Hero brand text, version badges, retro moments only
- **Weight**: Regular only
- **Features**: Bitmap-style, 8×8 grid

---

## Resource Pack Structure

```
assets/mvxmenu/font/
├── JetBrainsMono-Regular.ttf
├── JetBrainsMono-Medium.ttf
├── JetBrainsMono-Bold.ttf
├── Inter-Regular.ttf
├── Inter-Medium.ttf
├── Inter-Bold.ttf
├── PressStart2P-Regular.ttf
└── pack.mcmeta
```

### pack.mcmeta
```json
{
  "pack": {
    "pack_format": 15,
    "description": "MVXmenu Custom Fonts"
  }
}
```

### Font Provider Registration
Registered via `assets/mvxmenu/font/default.json` (see `FontRenderer.java`).