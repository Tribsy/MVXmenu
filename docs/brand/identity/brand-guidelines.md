# MVXmenu Brand Guidelines

## Logo

**Primary**: `docs/brand/identity/logo.svg` — Extracted from `ModMenu_Shell.svg` path line 5
**Dark**: `logo-dark.svg` — For light backgrounds
**Monochrome**: `logo-monochrome.svg` — Single color, for watermarks/print

### Clear Space
Minimum clear space = **16px (SP_4)** on all sides. No other elements may enter this zone.

### Minimum Size
- Digital: **32px height** (icon), **120px width** (wordmark)
- Print: **10mm height**

### Don'ts
- ❌ Don't stretch, skew, or rotate
- ❌ Don't apply drop shadows, glows, or outlines
- ❌ Don't place on busy backgrounds without clear space
- ❌ Don't recolor — use provided variants only
- ❌ Don't use the icon without "MVX // HUD" wordmark in primary contexts

---

## Color Usage

**Primary Accent**: `#8B5CF6` (Purple) — Use for primary actions, focus, active states
**Secondary Accent**: `#A78BFA` (Light Purple) — Use for hover states, badges
**Legacy Green**: `#4ADE80` — Preserved ONLY for success states, enabled module indicators

### Gradient (If Needed)
`linear-gradient(135deg, #8B5CF6 0%, #A78BFA 100%)` — For premium highlights only

---

## Typography

**UI Font**: JetBrains Mono — All interface text, buttons, labels, code
**Body Font**: Inter — Descriptions, longer form text, settings explanations
**Pixel Font**: Press Start 2P — Retro/brand moments only (hero, version badge)

See `docs/brand/typography/type-scale.md` for full scale.

---

## Icon System

**Viewport**: 16×16 units
**Stroke**: 1.5px (normalized = 1.5/16 = 0.09375)
**Caps**: Round
**Joins**: Round
**Style**: Outlined (stroke only), no fills

Source SVGs: `docs/brand/icons/svg/*.svg`
Generated vertex data: `IconVertexData.java`

---

## Spacing Scale

Base unit = 4px (SP_1). All spacing is multiples of 4px.

| Token | Value | Use Case |
|-------|-------|----------|
| SP_1 | 4px | Micro gaps, icon padding |
| SP_2 | 8px | Standard gaps, card padding |
| SP_3 | 12px | Component internal spacing |
| SP_4 | 16px | **Base unit** — panel padding, sidebar margins |
| SP_6 | 24px | Section gaps |
| SP_8 | 32px | Major section breaks |
| SP_12 | 48px | Page-level spacing |
| SP_16 | 64px | Hero/brand spacing |

---

## Radius Scale

| Token | Value | Use Case |
|-------|-------|----------|
| R_WINDOW | 19px | Outer window chrome |
| R_PANEL | 12px | Content panels, module detail, settings |
| R_CARD | 12px | Module cards |
| R_BUTTON | 8px | Buttons (7.5→8 for pixel alignment) |
| R_INPUT | 6px | Text fields, dropdowns |
| R_BADGE | 999px | Pill-shaped badges |

---

## Motion

| Token | Duration | Easing | Use Case |
|-------|----------|--------|----------|
| MOTION_MICRO | 80ms | ease-out | Hover state changes |
| MOTION_SNAP | 120ms | ease-out | Focus, click, toggle |
| MOTION_SLIDE | 200ms | ease-out | Panel slides, drawer |
| MOTION_FADE | 200ms | ease-in-out | Cross-fades, modals |
| MOTION_SPRING | 300ms | spring(0.8, 0.3) | Panel transitions, detail view |

---

## Elevation

| Level | Shadow | Highlight |
|-------|--------|-----------|
| SM | `SHADOW_SM` | — |
| MD | `SHADOW_MD` | — |
| LG | `SHADOW_LG` | — |
| PANEL | — | `SHADOW_PANEL` (top inner) |

---

## Responsive Breakpoints (GUI Scale)

| GUI Scale | Screen Width (GUI px) | Sidebar | Content Columns |
|-----------|----------------------|---------|-----------------|
| 1 | 1280 | 260 | 4 |
| 2 | 640 | 260 | 2 |
| 3 | 427 | 200* | 1 |
| 4 | 320 | 180* | 1 |

*Sidebar compresses below 640 GUI px; content stacks to single column.