# Type Scale

> All sizes in **Minecraft GUI pixels** at GUI scale 1. At scale 2, divide by 2.

## JetBrains Mono (UI Font)

| Role | Token | Size | Weight | Tracking | Line Height | Use Case |
|------|-------|------|--------|----------|-------------|----------|
| Hero | `TYPE_HERO` | 28 | Bold | -0.5px | 1.0 | Brand logo "MVX // HUD" |
| Display | `TYPE_DISPLAY` | 20 | Medium | 0 | 1.1 | Category headings |
| Heading | `TYPE_HEADING` | 16 | Medium | 0 | 1.2 | Panel titles, section headers |
| Subheading | `TYPE_SUBHEADING` | 13 | Regular | 0 | 1.3 | Sub-sections, card subtitles |
| Default | `TYPE_DEFAULT` | 12 | Regular | 0 | 1.4 | Body text, buttons, labels |
| Body | `TYPE_BODY` | 11 | Regular | 0 | 1.5 | Descriptions, help text |
| Label | `TYPE_LABEL` | 10 | Medium | +0.5px | 1.0 | **ALL CAPS** — UI labels, badges |
| Micro | `TYPE_MICRO` | 9 | Regular | 0 | 1.0 | Tooltips, metadata, version |

---

## Inter (Body Font)

| Role | Token | Size | Weight | Tracking | Line Height | Use Case |
|------|-------|------|--------|----------|-------------|----------|
| Body Large | `TYPE_BODY_LG` | 13 | Regular | 0 | 1.6 | Settings descriptions |
| Body | `TYPE_BODY` | 11 | Regular | 0 | 1.6 | Module descriptions, help |
| Body Small | `TYPE_BODY_SM` | 10 | Regular | 0 | 1.5 | Fine print, footnotes |

---

## Press Start 2P (Pixel Font)

| Role | Token | Size | Use Case |
|------|-------|------|----------|
| Hero | `TYPE_PIXEL_HERO` | 16 | Version badge, splash |

---

## Font Constants (Generated in MvxmenuTheme.java)

```java
public static final String FONT_UI = "mvxmenu:jetbrains_mono";
public static final String FONT_BODY = "mvxmenu:inter";
public static final String FONT_PIXEL = "mvxmenu:press_start_2p";

public static final int TYPE_HERO = 28;
public static final int TYPE_DISPLAY = 20;
public static final int TYPE_HEADING = 16;
public static final int TYPE_SUBHEADING = 13;
public static final int TYPE_DEFAULT = 12;
public static final int TYPE_BODY = 11;
public static final int TYPE_LABEL = 10;
public static final int TYPE_MICRO = 9;
```

---

## Responsive Scaling

| GUI Scale | Effective Size (Hero) | Effective Size (Default) |
|-----------|----------------------|--------------------------|
| 1 | 28px | 12px |
| 2 | 14px | 6px |
| 3 | 9px | 4px |
| 4 | 7px | 3px |

**Note**: Below GUI scale 2, text becomes hard to read. Consider minimum scale 2 for accessibility.