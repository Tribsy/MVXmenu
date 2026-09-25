# Transition Specification

## Duration Tokens

| Token | Duration | Easing | Use Case |
|-------|----------|--------|----------|
| `MOTION_MICRO` | 80ms | `ease-out` | Hover state, micro-interactions |
| `MOTION_SNAP` | 120ms | `ease-out` | Focus, click, toggle, button press |
| `MOTION_SLIDE` | 200ms | `ease-out` | Panel slide, drawer, sidebar |
| `MOTION_FADE` | 200ms | `ease-in-out` | Cross-fade, modal, tooltip |
| `MOTION_SPRING` | 300ms | `spring(0.8, 0.3)` | Detail panel, settings transition |

## Easing Functions

```java
// ease-out: cubic-bezier(0.25, 0.46, 0.45, 0.94)
float easeOut(float t) { return 1 - (1 - t) * (1 - t); }

// ease-in-out: cubic-bezier(0.42, 0, 0.58, 1)
float easeInOut(float t) { return t < 0.5 ? 2 * t * t : 1 - Math.pow(-2 * t + 2, 2) / 2; }

// spring: custom physics
float spring(float t, float stiffness, float damping) {
    // Simplified spring for UI
    return (float)(1 - Math.exp(-stiffness * t) * Math.cos(damping * t));
}
```

## Transition Map

| Transition | From State | To State | Duration | Token |
|------------|------------|----------|----------|-------|
| Button hover | Default | Hover | 80ms | MICRO |
| Button press | Hover | Active | 0ms | — (instant) |
| Button focus | Default | Focus | 120ms | SNAP |
| Toggle | Off | On | 120ms | SNAP |
| Slider drag | — | — | 0ms | — (live) |
| Dropdown open | Closed | Open | 120ms | SNAP |
| Module card hover | Default | Hover | 80ms | MICRO |
| Module detail open | Grid | Detail | 200ms | SLIDE |
| Module detail close | Detail | Grid | 200ms | SLIDE |
| Settings panel tab | Tab A | Tab B | 200ms | FADE |
| Sidebar category | Category A | Category B | 120ms | SNAP |
| Tooltip show | Hidden | Visible | 80ms | MICRO |
| Tooltip hide | Visible | Hidden | 80ms | MICRO |
| Window open | Closed | Open | 300ms | SPRING |
| Window close | Open | Closed | 200ms | SLIDE |

## Animation Progress in Widgets

Each widget tracks `hoverProgress` and `focusProgress` (0.0–1.0):
```java
// In render():
float hoverProgress = Math.min(1.0f, hoverTimer / MOTION_SNAP);
int bgColor = lerpColor(BG_1, BG_2, hoverProgress);
```

## Performance Notes

- All animations CPU-side (progress calculation)
- No GPU animation — VertexConsumer re-tessellates each frame
- Target: <5ms render at scale 2 with 11 modules visible
- Cache tessellated buffers for static rounded rects