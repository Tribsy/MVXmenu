# Icon Catalog

> 16 icons mapped to module categories. Source SVGs in `docs/brand/icons/svg/`. Generated vertex data in `IconVertexData.java`.

---

## Category → Icon Mapping

| Category | Icon | SVG File | Description |
|----------|------|----------|-------------|
| COMBAT | `CROSSHAIR` | `crosshair.svg` | Targeting, aim assist |
| MOVEMENT | `ZAP` | `zap.svg` | Speed, flight, movement |
| PLAYER | `EYE` | `eye.svg` | ESP, player tracking |
| RENDER | `LAYERS` | `layers.svg` | Visual effects, chams |
| WORLD | `MAP` | `map.svg` | World manipulation, XRay |
| EXPLOIT | `SHIELD` | `shield.svg` | Protection, anti-cheat bypass |
| MISC | `SLIDERS` | `sliders.svg` | Utility, settings |

---

## System Icons (UI)

| Icon | SVG File | Usage |
|------|----------|-------|
| `KEY` | `key.svg` | Keybinds, security |
| `LOCK` | `lock.svg` | Locked/disabled modules |
| `CLOCK` | `clock.svg` | Timer, system settings |
| `CPU` | `cpu.svg` | Performance, tick rate |
| `RADAR` | `radar.svg` | Radar, proximity |
| `TERMINAL` | `terminal.svg` | Console, debug |
| `BELL` | `bell.svg` | Notifications, alerts |
| `DATABASE` | `database.svg` | Data, config, storage |
| `GRID` | `grid.svg` | Grid, layout, inventory |

---

## Icon Specifications

- **Viewport**: 16×16 units (normalized 0–1 in vertex data)
- **Stroke Width**: 1.5 units (normalized = 0.09375)
- **Stroke Cap**: Round
- **Stroke Join**: Round
- **Fill**: None (outlined only)
- **Color**: Applied at render time via `MvxmenuTheme` constants
- **Standard Sizes**: 12px, 16px, 20px, 24px (scaled from 16×16 viewport)

---

## SVG Source Requirements

Each SVG must:
1. Have `viewBox="0 0 16 16"`
2. Use only `<path>` elements with stroke (no fill)
3. Stroke-width="1.5"
4. Stroke-linecap="round", stroke-linejoin="round"
5. Single path per icon (or combined into one)

---

## Generation Pipeline

```
docs/brand/icons/svg/*.svg
    → scripts/generate_icon_vertices.py
    → src/main/java/dev/mvxmenu/theme/IconVertexData.java
```

The script parses SVG path `d` attributes, normalizes coordinates to 0–1, and outputs `float[][]` arrays for `IconVertexData.renderIcon()`.

---

## Rendering

```java
// In widget render():
MvxmenuIcons icon = MvxmenuIcons.fromCategoryName(module.getCategory().getDisplayName());
icon.render(context, x, y, 24, MvxmenuTheme.AC);

// Internally calls:
IconVertexData.renderIcon(context, IconVertexData.CROSSHAIR, x, y, 24, MvxmenuTheme.AC, 1.5f);
```