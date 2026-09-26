# Icon Specification

## Technical Requirements

### Viewport & Coordinate System
- **Canvas**: 16×16 units
- **Origin**: Top-left (0,0)
- **Normalized**: All vertex coordinates stored as 0–1 floats (divide by 16)
- **Center**: (8, 8) → (0.5, 0.5) normalized

### Stroke Properties
- **Width**: 1.5 units (constant, not scaled with icon size)
- **Cap**: Round (`stroke-linecap="round"`)
- **Join**: Round (`stroke-linejoin="round"`)
- **Miter Limit**: 4 (default)

### Rendering Pipeline
1. SVG path parsed → normalized float[][] array in `IconVertexData.java`
2. At render: `MatrixStack.scale(size/16, size/16, 1)`
3. `VertexConsumer` draws line strip with geometry shader for round caps/joins
4. Or tessellated to triangles for filled rendering (future)

### Supported SVG Elements
- `<path d="..." />` — Primary, single path per icon
- `<g>` — Groups (flattened during generation)
- **Not supported**: `<rect>`, `<circle>`, `<polygon>`, `<use>`, gradients, filters

---

## Icon Definitions (Path Data)

### SHIELD
```svg
<path d="M8 2 L3 4 L3 8 C3 11 5 13 8 14 C11 13 13 11 13 8 L13 4 Z" stroke-width="1.5" fill="none"/>
```

### CROSSHAIR
```svg
<path d="M8 1 V3 M8 13 V15 M1 8 H3 M13 8 H15 M7.5 7.5 H8.5 V8.5 H7.5 Z" stroke-width="1.5" fill="none"/>
```

### EYE
```svg
<path d="M8 2 C11.3 2 14 5.5 14 9 C14 12.5 11.3 16 8 16 C4.7 16 2 12.5 2 9 C2 5.5 4.7 2 8 2 Z M8 5 C6.3 5 5 6.3 5 8 C5 9.7 6.3 11 8 11 C9.7 11 11 9.7 11 8 C11 6.3 9.7 5 8 5 Z" stroke-width="1.5" fill="none"/>
```

### ZAP
```svg
<path d="M8 1 L6 7 L7 7 L5 15 M8 15 L10 9 L9 9 L11 1" stroke-width="1.5" fill="none"/>
```

### KEY
```svg
<path d="M4 6 C4 4.9 4.9 4 6 4 H10 C11.1 4 12 4.9 12 6 V10 C12 11.1 11.1 12 10 12 H6 C4.9 12 4 11.1 4 10 V6 Z M9 10 H11 V12 H9 V10 Z" stroke-width="1.5" fill="none"/>
```

### SLIDERS
```svg
<path d="M2 4 H14 M4 8 V12 M8 2 V14 M12 6 V10" stroke-width="1.5" fill="none"/>
```

### LAYERS
```svg
<path d="M2 2 H14 M2 8 H14 M2 14 H14" stroke-width="1.5" fill="none"/>
```

### TERMINAL
```svg
<path d="M2 2 H14 V14 H2 Z M5 5 H11 M5 9 H9 M5 13 H11" stroke-width="1.5" fill="none"/>
```

### RADAR
```svg
<path d="M8 2 V14 M2 8 H14 M8 8 C8 5.8 9.8 4 12 4 C14.2 4 16 5.8 16 8" stroke-width="1.5" fill="none"/>
```

### LOCK
```svg
<path d="M5 6 V4 C5 2.9 5.9 2 7 2 H9 C10.1 2 11 2.9 11 4 V6 M5 6 H11 V12 H5 V6 Z" stroke-width="1.5" fill="none"/>
```

### CLOCK
```svg
<path d="M8 2 C11.3 2 14 4.7 14 8 C14 11.3 11.3 14 8 14 C4.7 14 2 11.3 2 8 C2 4.7 4.7 2 8 2 Z M8 8 V4 M8 8 L10 10" stroke-width="1.5" fill="none"/>
```

### CPU
```svg
<path d="M6 2 V14 M10 2 V14 M2 6 H14 M2 10 H14" stroke-width="1.5" fill="none"/>
```

### MAP
```svg
<path d="M6 3 V13 M10 5 V11 M3 8 H13" stroke-width="1.5" fill="none"/>
```

### GRID
```svg
<path d="M4 2 V14 M12 2 V14 M2 8 H14" stroke-width="1.5" fill="none"/>
```

### BELL
```svg
<path d="M6 4 C6 2.9 6.9 2 8 2 C9.1 2 10 2.9 10 4 C10 5.5 8.5 7 8 8 C7.5 7 6 5.5 6 4 Z M8 1 V2" stroke-width="1.5" fill="none"/>
```

### DATABASE
```svg
<path d="M2 4 C2 2.9 3.9 2 6 2 H10 C12.1 2 14 2.9 14 4 V12 C14 13.1 12.1 14 10 14 H6 C3.9 14 2 13.1 2 12 V4 Z M2 8 C2 6.9 3.9 6 6 6 H10 C12.1 6 14 6.9 14 8" stroke-width="1.5" fill="none"/>
```

---

## Generation Script Notes

The Python script (`scripts/generate_icon_vertices.py`):
1. Parses each SVG's `<path d="..."/>`
2. Extracts path commands (M, L, C, Z)
3. Converts to polyline segments (flattens curves to line segments)
4. Normalizes all coordinates to 0–1 (divide by 16)
5. Outputs `float[][]` arrays for `IconVertexData.java`

### Curve Flattening
Cubic Bézier curves (`C`) are flattened to 8 line segments each for rendering accuracy.

### Output Format
```java
public static final float[][] ICON_NAME = {
    {x1/16f, y1/16f}, {x2/16f, y2/16f}, ...
};
```