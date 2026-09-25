# Grid System

## 12-Column Grid

- **Container**: Content area (window width - sidebar - padding × 2)
- **Columns**: 12 equal-width columns
- **Gutter**: 8px (SP_2) between columns
- **Margins**: 16px (SP_4) on left/right of content area

### Breakpoints

| Breakpoint | GUI Width | Columns | Sidebar | Use Case |
|------------|-----------|---------|---------|----------|
| XL | ≥1024 | 12 | 260 | Desktop scale 1 |
| LG | 768–1023 | 8 | 260 | Desktop scale 1.5 |
| MD | 512–767 | 6 | 200 | Scale 2 |
| SM | <512 | 4 | 180 | Scale 3–4 |

### Module Grid Columns

Responsive column count based on content width:
```java
int columns = Math.max(1, Math.min(4, (contentWidth + 8) / (200 + 8)));
// 200px card width + 8px gap
```

---

## Spacing Scale

Base unit = 4px (SP_1). All values are multiples of 4.

| Token | Value | Rem | Use Case |
|-------|-------|-----|----------|
| SP_1 | 4px | 0.25rem | Icon padding, micro gaps |
| SP_2 | 8px | 0.5rem | Standard component gaps |
| SP_3 | 12px | 0.75rem | Internal component spacing |
| SP_4 | 16px | 1rem | **Base** — panel padding, margins |
| SP_6 | 24px | 1.5rem | Section gaps |
| SP_8 | 32px | 2rem | Major section breaks |
| SP_12 | 48px | 3rem | Page-level spacing |
| SP_16 | 64px | 4rem | Hero/brand spacing |

---

## Layout Dimensions (from ModMenu_Shell.svg)

| Element | Dimension | Token |
|---------|-----------|-------|
| Window Width | 1280px | — |
| Window Height | 800px | — |
| Window Radius | 19px | `R_WINDOW` |
| Sidebar Width | 260px | `SIDEBAR_WIDTH` |
| Header Height | 48px | `HEADER_HEIGHT` |
| Footer Height | 32px | `FOOTER_HEIGHT` |
| Panel Radius | 12px | `R_PANEL` |
| Card Radius | 12px | `R_CARD` |
| Button Radius | 8px | `R_BUTTON` |
| Input Radius | 6px | `R_INPUT` |
| Card Width | 200px | — |
| Card Height | 80px | — |
| Card Gap | 8px | `SP_2` |
| Panel Padding | 16px | `SP_4` |

---

## Responsive Rules

1. **Sidebar**: Fixed 260px down to 640 GUI px, then compresses to 200px, then 180px
2. **Header/Footer**: Fixed height, full width
3. **Content Panel**: Fills remaining space, `R_PANEL` radius
4. **Module Grid**: 1–4 columns based on content width
5. **Cards**: Fixed 200×80, scale down only if < 200px available
6. **Typography**: Scales with GUI scale (divide by scale factor)