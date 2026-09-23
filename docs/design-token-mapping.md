# Design Token Mapping: Figma Export → Java Constants

> Extracted from `Minecraft In-Game Menu UI/src/index.css` and `src/App.tsx` DesignSystemPage.
> Target: Minecraft 26.2 (Fabric, Java 25, Mojang mappings).

---

## How to read this document

Each CSS custom property is mapped to a Java constant in a planned `MvxmenuTheme` class. The Java naming follows `CATEGORY_INDEX` convention (e.g., `BG_0` for `--bg-0`). Values are in ARGB int format for Minecraft `DrawContext` rendering.

---

## Background Tokens

| CSS Name | CSS Value | Java Constant | ARGB Int | Usage |
|---|---|---|---|---|
| `--bg-0` | `#060606` | `BG_0` | `0xFF060606` | App root background |
| `--bg-1` | `#0E0E0E` | `BG_1` | `0xFF0E0E0E` | Shell body |
| `--bg-2` | `#161616` | `BG_2` | `0xFF161616` | Panel surface |
| `--bg-3` | `#1E1E1E` | `BG_3` | `0xFF1E1E1E` | Sidebar |
| `--bg-4` | `#272727` | `BG_4` | `0xFF272727` | Input / card |
| `--bg-5` | `#303030` | `BG_5` | `0xFF303030` | Hover |

---

## Border Tokens

| CSS Name | CSS Value | Java Constant | ARGB Int | Usage |
|---|---|---|---|---|
| `--bd-0` | `#181818` | `BD_0` | `0xFF181818` | Hairline |
| `--bd-1` | `#242424` | `BD_1` | `0xFF242424` | Default |
| `--bd-2` | `#333333` | `BD_2` | `0xFF333333` | Emphasis |
| `--bd-3` | `#444444` | `BD_3` | `0xFF444444` | Focus |

---

## Text Tokens

| CSS Name | CSS Value | Java Constant | ARGB Int | Usage |
|---|---|---|---|---|
| `--tx-0` | `#EFEFEF` | `TX_0` | `0xFFEFEFEF` | Primary |
| `--tx-1` | `#A0A0A0` | `TX_1` | `0xFFA0A0A0` | Secondary |
| `--tx-2` | `#5C5C5C` | `TX_2` | `0xFF5C5C5C` | Tertiary |
| `--tx-3` | `#333333` | `TX_3` | `0xFF333333` | Disabled |

---

## Accent & Semantic Tokens

| CSS Name | CSS Value | Java Constant | ARGB Int | Usage |
|---|---|---|---|---|
| `--ac` | `#4ADE80` | `AC` | `0xFF4ADE80` | Primary accent (emerald) |
| `--success` | `#4ADE80` | `SUCCESS` | `0xFF4ADE80` | Success / enabled |
| `--warning` | `#FCD34D` | `WARNING` | `0xFFFCD34D` | Warning / caution |
| `--danger` | `#F87171` | `DANGER` | `0xFFF87171` | Error / destructive |
| `--info` | `#60A5FA` | `INFO` | `0xFF60A5FA` | Info / neutral status |
| `--purple` | `#A78BFA` | `PURPLE` | `0xFFA78BFA` | Special / premium |
| `--orange` | `#FB923C` | `ORANGE` | `0xFFFB923C` | Alert / override |

### Semantic Background Overlays

| CSS Name | CSS Value | Java Constant | ARGB Int | Usage |
|---|---|---|---|---|
| `--ac-dim` | `rgba(74,222,128,0.07)` | `AC_DIM` | `0x124ADE80` | Accent dim overlay |
| `--ac-border` | `rgba(74,222,128,0.22)` | `AC_BORDER` | `0x384ADE80` | Accent border |
| `--ac-glow` | `0 0 12px rgba(74,222,128,0.18)` | `AC_GLOW` | (render effect) | Accent glow (custom render) |
| `--success-bg` | `rgba(74,222,128,0.07)` | `SUCCESS_BG` | `0x124ADE80` | Success bg overlay |
| `--warning-bg` | `rgba(252,211,77,0.07)` | `WARNING_BG` | `0x12FCD34D` | Warning bg overlay |
| `--danger-bg` | `rgba(248,113,113,0.07)` | `DANGER_BG` | `0x12F87171` | Danger bg overlay |
| `--info-bg` | `rgba(96,165,250,0.07)` | `INFO_BG` | `0x1260A5FA` | Info bg overlay |
| `--ac-fg` | `#052E16` | `AC_FG` | `0xFF052E16` | Accent foreground text |

---

## Typography Mapping

### Font Families

| CSS Variable | Font | Minecraft Equivalent | Notes |
|---|---|---|---|
| `--font-ui` | `'JetBrains Mono', monospace` | Resource pack font or default | Primary UI font; requires resource pack for in-game |
| `--font-body` | `'Inter', sans-serif` | Resource pack font or default | Prose text; same resource pack |
| `--font-pixel` | `'Press Start 2P', monospace` | Resource pack font or default | Brand mark only; very small sizes only |

### Type Scale

| Role | CSS Size | CSS Tracking | CSS Weight | Minecraft Size | Mapping Rule |
|---|---|---|---|---|---|
| HERO | 28px | 0.25em | 700 | 14pt (scaled) | Title/brand only, 1 per view |
| DISPLAY | 20px | 0.20em | 700 | 10pt (scaled) | Category headings |
| HEADING | 16px | 0.15em | 700 | 8pt (scaled) | Panel titles, card headings |
| SUBHEADING | 13px | 0.08em | 600 | 6.5pt (scaled) | Secondary headings, breadcrumbs |
| DEFAULT | 12px | 0.02em | 400 | 6pt (scaled) | Default UI text |
| BODY | 11px | 0.04em | 400 | 5.5pt (scaled) | Descriptions, values |
| LABEL | 10px | 0.12em | 500 | 5pt (scaled) | Field labels, dividers |
| MICRO | 9px | 0.15em | 400 | 4.5pt (scaled) | Metadata, footers |

> **Note:** Minecraft's text rendering uses GUI scale (1-4). At GUI scale 2, 1 Minecraft "pixel" = 0.5 screen pixels. The CSS pixel values should be divided by 2 for Minecraft coordinate space, then rendered with `DrawContext.drawText()`. Font size in Minecraft is measured in "pixels" (1pt ≈ 1px at default GUI scale).

### Typography Rules (from Figma)

| Rule | Java Implementation |
|---|---|
| Never mix tracking values within one text element | Enforce per-style; no tracking constants shared across styles |
| Use tabular-nums for numeric values | Pass `TextVisibility` / use `VertexConsumer` with `TextLayer` using `TextModel` |
| All caps labels require tracking ≥ 0.10em | Validation in theme constants |
| Line-height: 1.0–1.2 for headings; 1.4–1.6 for body | Store as float constant per role; multiply by font size |

---

## Radius Tokens

| CSS Name | CSS Value | Java Constant | Usage | Minecraft Equivalent |
|---|---|---|---|---|
| `--r-1` | 2px | `R_1 = 2` | Inputs, buttons, micro UI | Pixel-perfect sharp rectangles (no rounding in Minecraft) |
| `--r-2` | 3px | `R_2 = 3` | Tags, badges | Sharp rectangles |
| `--r-3` | 4px | `R_3 = 4` | Cards, panels, shell window | Sharp rectangles |
| `--r-4` | 6px | `R_4 = 6` | Modals (reserved) | Sharp rectangles |

> **Important:** Minecraft's `DrawContext.fill()` draws sharp rectangles. There is no native rounded rectangle API. Options: (1) Use sharp rectangles and document the radius as a reference for future shader-based rounding, (2) Implement custom rounded rect rendering via `VertexConsumer` with a path drawn manually.

---

## Spacing Tokens

| CSS Name | CSS Value | Java Constant | Minecraft Coords (÷2 at scale 2) |
|---|---|---|---|
| `--sp-1` | 4px | `SP_1 = 4` | 2 |
| `--sp-2` | 8px | `SP_2 = 8` | 4 |
| `--sp-3` | 12px | `SP_3 = 12` | 6 |
| `--sp-4` | 16px | `SP_4 = 16` | 8 |
| `--sp-6` | 24px | `SP_6 = 24` | 12 |
| `--sp-8` | 32px | `SP_8 = 32` | 16 |
| `--sp-12` | 48px | `SP_12 = 48` | 24 |

---

## Shadow/Elevation Tokens

| CSS Name | CSS Value | Java Constant | Minecraft Approach |
|---|---|---|---|
| `--shadow-sm` | `0 1px 3px rgba(0,0,0,0.4)` | `SHADOW_SM` | Render semi-transparent black rect below element (1px offset, 3px height) |
| `--shadow-md` | `0 4px 12px rgba(0,0,0,0.5)` | `SHADOW_MD` | Larger offset rect with gradient fade |
| `--shadow-lg` | `0 8px 24px rgba(0,0,0,0.7)` | `SHADOW_LG` | Full backing panel with darker alpha |
| `--shadow-panel` | `inset 0 1px 0 rgba(255,255,255,0.03)` | `SHADOW_PANEL` | Top-edge highlight line (1px white at 3% alpha) |

---

## Motion & Transition Tokens

| Name | Duration | Easing | Use Case | Minecraft Implementation |
|---|---|---|---|---|
| Micro | 80ms | ease | Toggle, checkbox, icon color | Interpolate color via `alpha` over frames |
| Snap | 120ms | ease | Button states, border colors | Interpolate color/alpha over frames |
| Slide | 200ms | ease-out | View transitions, slide-in panels | Interpolate X offset via `lerp` over frames |
| Fade | 200ms | ease | Page / tab content transitions | Interpolate alpha over frames |

> **Constraint:** Minecraft has no CSS transition system. Animations must be implemented manually in `render()` using delta time and interpolation (lerp/ease-out). All transitions affect color, alpha, or offset only — never layout properties (width, height, padding).

---

## Icon System Mapping

| Icon Name | Figma Usage | Minecraft Render Strategy | Priority |
|---|---|---|---|
| Shield | Protection / anti-cheat | 16×16 polygon via `DrawContext.fill()` | High |
| Crosshair | Targeting / aim | Circle + cross lines via `DrawContext.fill()` | High |
| Eye | Visibility / ESP | Eye shape via path fill | High |
| Zap | Speed / performance | Lightning bolt path via fill | High |
| Key | Keybind config | Key shape via circle + line | High |
| Sliders | Parameter panels | Three rect + three slider knobs | High |
| Layers | Module priority | Stacked diamond paths | High |
| Terminal | Debug / console | Rect + chevron + line | High |
| Radar | Detection / awareness | Circle + inner circle + line + dot | High |
| Lock | Restricted / disabled | Rect + arc + dot | High |
| Clock | Tick rate / timing | Circle + hands via fill | High |
| Cpu | Performance metrics | Rect + pins via fill | Medium |
| Map | Minimap / coordinates | Map shape + grid lines | Medium |
| Grid | Module grid view | Four squares via fill | Medium |
| Bell | Notification / alert | Bell shape via path | Medium |
| Database | Config storage | Cylinder via ellipse + curves | Medium |

> **Icon Rendering Spec:** All icons are 16×16 viewport, 1.5px stroke, round caps, `currentColor`. Render at multiples of 4 (12, 16, 20, 24). In Minecraft: render at 12, 24, 36, 48 pixels (corresponding to GUI scale 1-2 for sidebar items).

---

## Figma Screen Layout → Minecraft Coordinates

| Figma Element | Figma Value | Minecraft Coords (GUI scale 2) | Minecraft Coords (GUI scale 1) |
|---|---|---|---|
| Window width | 1200px max | 600 | 1200 |
| Window height | 80% viewport | 80% viewport | 80% viewport |
| Sidebar width | 180px | 90 | 180 |
| Header height | 48px | 24 | 48 |
| Footer height | 32px | 16 | 32 |
| Padding | 16px (SP_4) | 8 | 16 |
| Card radius | --r-3 = 4px | 2 | 4 |
| Border | 1px | 0.5 (use 1px min) | 1px |
| Font scale | CSS px ÷ 2 | CSS px ÷ 2 ÷ 2 | CSS px ÷ 2 |

> **Note on GUI scale:** Minecraft GUI scale (1, 2, 3, 4) determines the ratio between screen pixels and GUI coordinates. At scale 2, 1 GUI coordinate = 0.5 screen pixels. The Figma design at 1200px wide at GUI scale 2 gives 600 Minecraft GUI coordinates, which is appropriate.

---

## Colors Used in Components (from App.tsx)

| Component | Primary Color | Secondary Color | State Colors |
|---|---|---|---|
| Toggle | `bg-green-950/40` → `SUCCESS_BG` | `bg-green-900` → `ac-dim` | ON: `bg-green-500` → `SUCCESS`; OFF: `bg-neutral-900` → `BG_1` |
| Button (default) | `bg-neutral-900` → `BG_1` | `border-neutral-700` → `BD_2` | Hover: `bg-neutral-800` → `BG_4`; Active: `bg-neutral-800` → `BG_4` |
| Button (primary) | `bg-neutral-200` → `0xFFDCDCDC` | `border-neutral-200` → same | Hover: `bg-white` → `0xFFFFFFFF` |
| Button (danger) | `bg-red-950/30` → `0x4D7A1616` | `border-red-900` → `0xFF7A1616` | Hover: `bg-red-900/50` → `0x807A1616` |
| Button (accent) | `bg-green-950/60` → `0x99052E16` | `border-green-900/70` → `0xB3052E16` | Hover: `bg-green-950` → `0xFF052E16` |
| Module card (enabled) | `border-green-900/50` → `0x80052E16` | `bg-green-500` dot → `SUCCESS` | Hover: `border-neutral-600` → `0xFF666666` |
| Module card (disabled) | `opacity-50` → alpha 0.5 | `bg-neutral-800` → `BG_4` | No hover |
| Module card (hover) | `border-neutral-600` → `0xFF666666` | — | Cursor: pointer |
| Slider track | `bg-neutral-900` → `BG_1` | `border-neutral-800` → `BD_1` | Fill: `bg-neutral-300` → `0xFFDCDCDC` |
| Dropdown | `bg-neutral-900` → `BG_1` | `border-neutral-700` → `BD_2` | Hover: `border-neutral-500` → `BD_3` |
| Checkbox (checked) | `bg-neutral-300` → `0xFFDCDCDC` | `border-neutral-300` → same | Unchecked: `bg-neutral-900` → `BG_1` |
| Keybind | `bg-neutral-900` → `BG_1` | `border-neutral-700` → `BD_2` | Hover: `border-neutral-400` → `0xFFCCCCCC` |
| Status badge (active) | `text-green-400` → `0xFF4ADE80` | `bg-green-950/40` → `SUCCESS_BG` | — |
| Status badge (error) | `text-red-400` → `0xFFF87171` | `bg-red-950/40` → `DANGER_BG` | — |
| Status badge (warning) | `text-yellow-400` → `0xFFFFF176` | `bg-yellow-950/40` → `WARNING_BG` | — |
| Status badge (info) | `text-blue-400` → `0xFF60A5FA` | `bg-blue-950/40` → `INFO_BG` | — |
| Status badge (locked) | `text-purple-400` → `0xFFA78BFA` | `bg-purple-950/40` → `0x12A78BFA` | — |

---

## Generated Constants Summary

Total Java constants to generate in `MvxmenuTheme.java`:

- **6** background color constants (`BG_0` - `BG_5`)
- **4** border color constants (`BD_0` - `BD_3`)
- **4** text color constants (`TX_0` - `TX_3`)
- **7** accent/semantic color constants (`AC`, `SUCCESS`, `WARNING`, `DANGER`, `INFO`, `PURPLE`, `ORANGE`)
- **4** semantic background constants (`SUCCESS_BG`, `WARNING_BG`, `DANGER_BG`, `INFO_BG`)
- **2** accent misc constants (`AC_DIM`, `AC_BORDER`, `AC_FG`, `AC_GLOW`)
- **4** radius constants (`R_1` - `R_4`)
- **7** spacing constants (`SP_1`, `SP_2`, `SP_3`, `SP_4`, `SP_6`, `SP_8`, `SP_12`)
- **4** shadow constants (`SHADOW_SM`, `SHADOW_MD`, `SHADOW_LG`, `SHADOW_PANEL`)
- **4** motion duration constants (`MOTION_MICRO`, `MOTION_SNAP`, `MOTION_SLIDE`, `MOTION_FADE`)
- **8** type scale constants (`TYPE_HERO`, `TYPE_DISPLAY`, ..., `TYPE_MICRO`)
- **3** font constants (`FONT_UI`, `FONT_BODY`, `FONT_PIXEL`)

**Total: ~51 primary constants** in the theme class.
