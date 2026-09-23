# Icon System Porting Specification

> Extracted from `Minecraft In-Game Menu UI/src/App.tsx` Icon catalog (lines 16-141).
> Target: Minecraft 26.2 `DrawContext` rendering (no SVG, no CSS).

---

## Design Rules

- **Viewport:** 16×16 SVG units
- **Stroke:** 1.5px (at 16px scale → will be 1px at Minecraft render)
- **Line caps:** Round
- **Line joins:** Round
- **Fill:** `currentColor` exclusively (no hardcoded colors)
- **Render sizes:** Always multiples of 4: 12, 16, 20, 24
- **Color:** Passed as ARGB int via `DrawContext`

---

## Minecraft Rendering Approach

Minecraft has no SVG or path rendering for UI. Options in order of preference:

1. **`DrawContext.fill(MatrixStack, int x, int y, int x2, int y2)`** — fill rectangles only
2. **Custom `VertexConsumer` path** — draw arbitrary polygons via `VertexConsumer` with `DrawContext.getMatrices()`
3. **Pre-rendered texture atlas** — bake icons to a 256×256 texture, render via `DrawContext.drawTexture()`
4. **Unicode character rendering** — use single Unicode glyphs as fallbacks (e.g., ⚔ for crosshair)

**Recommended:** Option 2 (custom vertex paths) for primary icons, Option 3 (texture atlas) for production quality. Option 4 as immediate fallback during early milestones.

---

## Icon Catalog

### 1. Shield — Protection / Anti-Cheat
- **Shape:** Shield polygon (5-point), check mark inside
- **Coordinates:**
  - Shield outline: (8,2) → (3,4) → (3,8) → (8,14) → (13,11) → (13,8) → (8,2)
  - Check: (5.5,8) → (7.3,9.8) → (10.5,6)

### 2. Crosshair — Targeting / Aim-Assist
- **Shape:** Circle center (8,8) r=3, cross lines through center, dot at center
- **Coordinates:**
  - Circle: center (8,8), radius 3
  - Lines: (8,1)-(8,3), (8,13)-(8,15), (1,8)-(3,8), (13,8)-(15,8)
  - Center dot: r=1 at (8,8)

### 3. Eye — Visibility / ESP
- **Shape:** Eye outline (almond), inner circle
- **Coordinates:**
  - Outline: (1.5,8) → curve through (3,4.5), (8,3), (13,5) → (14.5,8) → (13,11) → (8,13) → (3,11) → (1.5,8)
  - Inner circle: center (8,8), r=2

### 4. Zap — Speed / Performance
- **Shape:** Lightning bolt
- **Coordinates:**
  - (9,1.5) → (4,9) → (8.5,9) → (7,14.5) → (12,7) → (7.5,7) → (9,1.5)

### 5. Key — Keybind Configuration
- **Shape:** Circle + diagonal line + teeth
- **Coordinates:**
  - Circle: center (5.5,6.5), r=3
  - Stem: (8,8.5) → (13.5,14)
  - Teeth: (11,11.5)→(12.5,13), (12.5,10)→(14,11.5)

### 6. Sliders — Parameter Panels
- **Shape:** 3 horizontal lines + 3 slider knobs
- **Coordinates:**
  - Lines: (2,4)-(14,4), (2,8)-(14,8), (2,12)-(14,12)
  - Knobs: (4,2.5)→(6.5,5.5), (9,6.5)→(11.5,9.5), (5.5,10.5)→(8,13.5)

### 7. Layers — Module Priority
- **Shape:** 3 stacked diamond shapes
- **Coordinates:**
  - Top: (8,2)→(2,5.5)→(8,9)→(14,5.5)→(8,2)
  - Middle: (2,9)→(8,12.5)→(14,9)
  - Bottom: (2,12)→(8,15.5)→(14,12) at 0.45 opacity

### 8. Terminal — Debug / Console
- **Shape:** Terminal window + chevron + prompt line
- **Coordinates:**
  - Window: (1.5,2.5)→(14.5,13.5) rect, r=1.5
  - Chevron: (4,6)→(7,9)→(4,12)
  - Line: (8.5,12)→(12,12)

### 9. Radar — Detection / Awareness
- **Shape:** Outer circle, inner circle, diagonal line, 2 dots
- **Coordinates:**
  - Outer: center (8,8), r=6.5
  - Inner: center (8,8), r=3.5 at 0.45 opacity
  - Line: (8,8)→(12,4)
  - Dots: (11,5) r=1, (8,8) r=1

### 10. Lock — Restricted / Disabled
- **Shape:** Lock body + arc shackle + keyhole
- **Coordinates:**
  - Body: (3.5,7.5)→(12.5,14.5) rect, r=1.5
  - Shackle: (5,7.5)→(5,5) arc r=3 → (11,5)→(11,7.5)
  - Keyhole: (8,11) r=1

### 11. Clock — Tick Rate / Timing
- **Shape:** Clock face + hands
- **Coordinates:**
  - Face: center (8,8), r=6.5
  - Hour hand: (8,4.5)→(8,8)
  - Minute hand: (8,8)→(10.5,10.5)

### 12. Cpu — Performance Metrics
- **Shape:** CPU square + pins
- **Coordinates:**
  - Body: (4.5,4.5)→(11.5,11.5) rect, r=1
  - Top pins: (6,4.5)→(6,2), (8,4.5)→(8,2), (10,4.5)→(10,2)
  - Bottom pins: (6,11.5)→(6,14), (8,11.5)→(8,14), (10,11.5)→(10,14)
  - Left pins: (4.5,6)→(2,6), (4.5,8)→(2,8), (4.5,10)→(2,10)
  - Right pins: (11.5,6)→(14,6), (11.5,8)→(14,8), (11.5,10)→(14,10)

### 13. Map — Minimap / Coordinates
- **Shape:** Map outline with fold lines
- **Coordinates:**
  - Outline: (1.5,3.5)→(5.5,5)→(10.5,3)→(14.5,4.5)→(14.5,12.5)→(10.5,11)→(5.5,13)→(1.5,10.5)→(1.5,3.5)
  - Fold lines: (5.5,5)→(5.5,13), (10.5,3)→(10.5,11)

### 14. Grid — Module Grid View Toggle
- **Shape:** 2×2 grid of squares
- **Coordinates:**
  - Top-left: (2,2)→(7,7), r=1
  - Top-right: (9,2)→(14,7), r=1
  - Bottom-left: (2,9)→(7,14), r=1
  - Bottom-right: (9,9)→(14,14), r=1

### 15. Bell — Notification / Alert
- **Shape:** Bell + clapper dot
- **Coordinates:**
  - Bell: (8,2.5)→(4,6.5)→(4,10)→(12,10)→(12,6.5)→(8,2.5) arc with dome top
  - Clapper: (6.5,12) arc (small semicircle)

### 16. Database — Config Storage / Profiles
- **Shape:** Cylinder top + body lines
- **Coordinates:**
  - Top ellipse: center (8,4.5), rx=5.5, ry=2
  - Body top: (2.5,4.5)→(2.5,8)→(13.5,8)→(13.5,4.5) curves
  - Body bottom: (2.5,8)→(2.5,11.5)→(13.5,11.5)→(13.5,8) curves

---

## Size Scale Mapping

| CSS Render | Minecraft GUI Scale 1 | Minecraft GUI Scale 2 | Minecraft GUI Scale 3 | Minecraft GUI Scale 4 |
|---|---|---|---|---|
| 12px | 12 GUI px | 6 GUI px | 4 GUI px | 3 GUI px |
| 16px | 16 GUI px | 8 GUI px | ~5 GUI px | 4 GUI px |
| 20px | 20 GUI px | 10 GUI px | ~7 GUI px | 5 GUI px |
| 24px | 24 GUI px | 12 GUI px | 8 GUI px | 6 GUI px |

> **Target render sizes in Minecraft:** 24px (sidebar icons at GUI scale 1), 12px (sidebar icons at GUI scale 2). Use 24px as primary, 12px as minimum.

---

## Stub Enum: MvxmenuIcons.java

```java
package dev.mvxmenu.theme;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

/**
 * Custom icon system for MVXmenu HUD elements.
 * All icons render as vector paths via DrawContext/VertexConsumer.
 * Viewport: 16×16 SVG units. Stroke: 1.5px, round caps/joins.
 */
public enum MvxmenuIcons {
    /** Shield — protection / anti-cheat category */
    SHIELD,
    /** Crosshair — targeting / aim-assist modules */
    CROSSHAIR,
    /** Eye — visibility / render / ESP modules */
    EYE,
    /** Zap — speed / performance / sprint modules */
    ZAP,
    /** Key — keybind configuration */
    KEY,
    /** Sliders — parameter / configuration panels */
    SLIDERS,
    /** Layers — stacked modules / priority order */
    LAYERS,
    /** Terminal — debug / console / logging */
    TERMINAL,
    /** Radar — detection / awareness modules */
    RADAR,
    /** Lock — restricted / disabled state */
    LOCK,
    /** Clock — tick rate / timing / cooldown */
    CLOCK,
    /** Cpu — performance metrics / frame rate */
    CPU,
    /** Map — minimap / coordinates */
    MAP,
    /** Grid — module grid view toggle */
    GRID,
    /** Bell — notification / alert */
    BELL,
    /** Database — config storage / profile data */
    DATABASE;

    /**
     * Render this icon at the specified size.
     *
     * @param matrices  MatrixStack for DrawContext
     * @param x         X coordinate (GUI space)
     * @param y         Y coordinate (GUI space)
     * @param size      Render size in pixels (must be multiple of 4: 12, 16, 20, 24)
     * @param color     ARGB color (uses all channels for tint)
     */
    public void render(MatrixStack matrices, int x, int y, int size, int color) {
        // TODO: Implement in Milestone 13 (Icon System)
        // Primary approach: VertexConsumer path via DrawContext
        // Fallback: texture atlas lookup
    }
}
```

---

## Implementation Plan for Icons

| Milestone | Task | Approach |
|---|---|---|
| 1 (current) | Document all 16 icon geometries | ✅ This document |
| 2-3 | Fabric project scaffold | Prerequisite |
| 13 | Implement icon rendering | VertexConsumer paths (Option 2 above) |
| Post-17 | Production optimization | Pre-render to texture atlas (Option 3) |
