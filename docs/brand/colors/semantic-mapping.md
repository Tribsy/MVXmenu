# Semantic Color Mapping

> Maps semantic roles to color tokens from `palette.md`. Used by widget rendering logic.

---

## Component State Mapping

| Component | State | Background | Border | Text/Icon |
|-----------|-------|------------|--------|-----------|
| **Button (Primary)** | Default | `AC` | `AC` | `AC_FG` |
| | Hover/Focus | `AC` (brighter) | `AC` | `AC_FG` |
| | Disabled | `BG_4` | `BD_1` | `TX_2` |
| **Button (Ghost)** | Default | `BG_0` | `TX_2` | `TX_0` |
| | Hover/Focus | `BG_2` | `TX_2` | `TX_0` |
| | Disabled | `BG_1` | `BD_0` | `TX_2` |
| **Button (Danger)** | Default | `DANGER` | `DANGER` | `TX_0` |
| | Hover/Focus | `DANGER` (brighter) | `DANGER` | `TX_0` |
| **Button (Accent)** | Default | `PURPLE` | `PURPLE` | `TX_0` |
| | Hover/Focus | `PURPLE` (brighter) | `PURPLE` | `TX_0` |
| **Button (Default)** | Default | `BG_1` | `BD_1` | `TX_0` |
| | Hover/Focus | `BG_2` | `BD_2` | `TX_0` |
| **Module Card** | Enabled | `BG_0` | `AC` (top/left) | `TX_0`/`TX_1` |
| | Disabled | `BG_1` | `BD_1` | `TX_2` |
| | Hover/Focus | `BG_2` | `AC` | `TX_0`/`TX_1` |
| **Sidebar Category** | Active | `AC_DIM` | `AC` (left bar) | `TX_0`/`AC` |
| | Hover | `BG_2` | `AC_DIM` (top) | `TX_1` |
| | Default | `BG_1` | `BD_1` (bottom) | `TX_2` |
| **Toggle** | On | `AC` (track) | — | — |
| | Off | `BG_4` (track) | `BD_2` | — |
| **Slider** | Track | `BD_1` | — | — |
| | Fill | `AC` | — | — |
| | Thumb | `TX_0` | `AC` (ring) | — |
| **Dropdown** | Default | `BG_3` | `BD_1` | `TX_0` |
| | Hover/Focus | `BG_3` | `AC` | `TX_0` |
| | Open | `BG_3` | `AC` | `TX_0` |
| **Input/TextField** | Default | `BG_3` | `BD_1` | `TX_0` |
| | Focus | `BG_3` | `AC` | `TX_0` |
| | Error | `BG_3` | `DANGER` | `TX_0` |
| **Status Badge** | Success | `SUCCESS_BG` | — | `SUCCESS` |
| | Warning | `WARNING_BG` | — | `WARNING` |
| | Danger | `DANGER_BG` | — | `DANGER` |
| | Info | `INFO_BG` | — | `INFO` |
| | Neutral | `BD_1` @ 20% | — | `TX_1` |

---

## Layout Regions

| Region | Background | Border/Stroke |
|--------|------------|---------------|
| **Window** | `BG_0` | `AC` (2px top) |
| **Header** | `BG_2` | `BD_1` (bottom 1px) |
| **Sidebar** | `BG_1` | — |
| **Content Panel** | `BG_0` | `R_PANEL` radius, `SHADOW_PANEL` top highlight |
| **Footer** | `BG_2` | `BD_1` (top 1px) |
| **Module Grid Card** | `BG_0`/`BG_1`/`BG_2` | `AC` top bar (2px), status bar (4px left) |

---

## Elevation Shadows

| Level | Color | Offset Y | Blur | Usage |
|-------|-------|----------|------|-------|
| `SHADOW_SM` | `0x40000000` | 1px | 3px | Tooltips, dropdowns |
| `SHADOW_MD` | `0x60000000` | 4px | 12px | Cards, panels |
| `SHADOW_LG` | `0x80000000` | 8px | 24px | Modals, overlays |
| `SHADOW_PANEL` | `0x08FFFFFF` | 0px | 0px | Top highlight on panels (inner glow) |

---

## Focus Ring

| State | Color | Width | Offset |
|-------|-------|-------|--------|
| Standard | `AC` | 2px | 2px |
| Glow | `AC_GLOW` | 4px | 4px (behind standard) |
| High Contrast | `HC_BORDER` | 3px | 2px |

---

## Animation Color Interpolation

All state transitions use `lerpColor(from, to, progress)` with `MOTION_SNAP` (120ms) duration:

```java
// Example: Button hover
bgColor = lerpColor(BG_1, BG_2, hoverProgress);
borderColor = lerpColor(BD_1, AC, hoverProgress);
```