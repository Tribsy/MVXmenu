# MVXmenu Color Palette

> **Source of Truth**: All color values defined here generate `MvxmenuTheme.java` via `scripts/generate_theme.py`.

---

## Primary Accent (Purple)

| Token | Hex | ARGB | Usage |
|-------|-----|------|-------|
| `AC` | `#8B5CF6` | `0xFF8B5CF6` | Primary brand accent — buttons, focus rings, active states |
| `AC_DIM` | `#8B5CF6` @ 7% | `0x128B5CF6` | Hover backgrounds, subtle fills |
| `AC_BORDER` | `#8B5CF6` @ 22% | `0x388B5CF6` | Borders for accent components |
| `AC_FG` | `#1A0D2E` | `0xFF1A0D2E` | Text on accent backgrounds (white would be too harsh) |
| `AC_GLOW` | `#8B5CF6` @ 18% | `0x2E8B5CF6` | Focus ring glow, elevation highlights |
| `PURPLE` | `#A78BFA` | `0xFFA78BFA` | Light purple variant — secondary accents, badges |
| `PURPLE_BG` | `#A78BFA` @ 10% | `0x1AA78BFA` | Light purple backgrounds |

---

## Semantic Colors (Preserved from Legacy)

| Token | Hex | ARGB | Usage |
|-------|-----|------|-------|
| `SUCCESS` | `#4ADE80` | `0xFF4ADE80` | Enabled states, success messages (legacy green) |
| `WARNING` | `#FCD34D` | `0xFFFCD34D` | Warnings, pending states |
| `DANGER` | `#F87171` | `0xFFF87171` | Destructive actions, errors, disabled modules |
| `INFO` | `#60A5FA` | `0xFF60A5FA` | Informational, links |

---

## Semantic Backgrounds (10% Opacity)

| Token | ARGB | Usage |
|-------|------|-------|
| `SUCCESS_BG` | `0x1A4ADE80` | Success toast backgrounds |
| `WARNING_BG` | `0x1AFCD34D` | Warning toast backgrounds |
| `DANGER_BG` | `0x1AF87171` | Danger toast backgrounds |
| `INFO_BG` | `0x1A60A5FA` | Info toast backgrounds |

---

## Neutral Backgrounds

| Token | Hex | ARGB | Usage |
|-------|-----|------|-------|
| `BG_0` | `#060606` | `0xFF060606` | Window background, content area |
| `BG_1` | `#0E0E0E` | `0xFF0E0E0E` | Sidebar, elevated panels |
| `BG_2` | `#161616` | `0xFF161616` | Header, footer, hovered cards |
| `BG_3` | `#1E1E1E` | `0xFF1E1E1E` | Input backgrounds |
| `BG_4` | `#272727` | `0xFF272727` | Disabled backgrounds |
| `BG_5` | `#303030` | `0xFF303030` | Highest elevation neutral |

---

## Neutral Borders

| Token | Hex | ARGB | Usage |
|-------|-----|------|-------|
| `BD_0` | `#181818` | `0xFF181818` | Hairline borders |
| `BD_1` | `#242424` | `0xFF242424` | Standard borders |
| `BD_2` | `#333333` | `0xFF333333` | Emphasized borders |
| `BD_3` | `#444444` | `0xFF444444` | Focus borders (non-accent) |

---

## Text Colors

| Token | Hex | ARGB | Usage |
|-------|-----|------|-------|
| `TX_0` | `#EFEFEF` | `0xFFEFEFEF` | Primary text (headings, labels) |
| `TX_1` | `#A0A0A0` | `0xFFA0A0A0` | Secondary text (descriptions, metadata) |
| `TX_2` | `#5C5C5C` | `0xFF5C5C5C` | Muted text (placeholders, disabled) |
| `TX_3` | `#333333` | `0xFF333333` | Text on light backgrounds |

---

## High Contrast Mode

| Token | ARGB | Usage |
|-------|------|-------|
| `HC_BORDER` | `0xFFFFFFFF` | All borders in HC mode |
| `HC_TEXT` | `0xFFFFFFFF` | All text in HC mode |
| `HC_BG` | `0xFF000000` | All backgrounds in HC mode |

---

## Generation Notes

- Run `python scripts/generate_theme.py` to regenerate `MvxmenuTheme.java`
- All ARGB values are in `0xAARRGGBB` format
- Opacity values: 10% = `0x1A`, 7% = `0x12`, 18% = `0x2E`, 22% = `0x38`