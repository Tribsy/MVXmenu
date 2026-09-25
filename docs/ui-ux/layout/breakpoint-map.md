# Breakpoint Map

## GUI Scale → Screen Dimensions

| GUI Scale | Scaled Width (1280) | Scaled Height (800) | Sidebar | Content Cols | Notes |
|-----------|---------------------|---------------------|---------|--------------|-------|
| 1 | 1280 | 800 | 260 | 4 | Reference |
| 2 | 640 | 400 | 260 | 2 | Standard |
| 3 | 427 | 267 | 200 | 1 | Compressed sidebar |
| 4 | 320 | 200 | 180 | 1 | Minimum |

## Responsive Behavior

### Sidebar
```java
int sidebarWidth() {
    int scaledWidth = getScaledWidth();
    if (scaledWidth >= 640) return 260;
    if (scaledWidth >= 427) return 200;
    return 180;
}
```

### Content Columns
```java
int moduleGridColumns() {
    int contentW = contentWidth();
    return Math.max(1, Math.min(4, (contentW + 8) / (200 + 8)));
}
```

### Typography Scaling
```java
int effectiveSize(int baseSize) {
    return Math.max(8, baseSize / guiScale); // Minimum 8px readable
}
```

### Panel Radius
- Scale 1–2: R_PANEL (12px)
- Scale 3–4: R_PANEL / 2 (6px) — smaller radius on tiny screens

## Breakpoint Tokens (Generated)

```java
public static final int BP_XL = 1024;
public static final int BP_LG = 768;
public static final int BP_MD = 512;
public static final int BP_SM = 320;
```