package dev.mvxmenu.theme;

import net.minecraft.client.gui.DrawContext;

/**
 * Icon renderer using geometric shapes (fallback for Minecraft 1.21.4).
 * Replaces the old geometric fill() based rendering with new theme colors.
 * TODO: Replace with VertexConsumer SVG path rendering when API is available.
 */
public enum MvxmenuIcons {
    SHIELD,
    CROSSHAIR,
    EYE,
    ZAP,
    KEY,
    SLIDERS,
    LAYERS,
    TERMINAL,
    RADAR,
    LOCK,
    CLOCK,
    CPU,
    MAP,
    GRID,
    BELL,
    DATABASE;

    /**
     * Renders the icon at the specified position and size using geometric shapes.
     */
    public void render(DrawContext context, int x, int y, int size, int color) {
        int s = size / 4;
        switch (this) {
            case SHIELD:
                context.fill(x + s, y, x + 3 * s, y + s, color);
                context.fill(x + s, y + s, x + s, y + 3 * s, color);
                context.fill(x + 3 * s, y + s, x + 3 * s + 1, y + 3 * s, color);
                context.fill(x + s, y + 3 * s, x + 3 * s, y + 4 * s, color);
                break;
            case CROSSHAIR:
                context.fill(x + 2 * s - 1, y, x + 2 * s + 1, y + 4 * s, color);
                context.fill(x, y + 2 * s - 1, x + 4 * s, y + 2 * s + 1, color);
                break;
            case EYE:
                context.fill(x + s, y + s, x + 3 * s, y + 2 * s, color);
                context.fill(x + 2 * s - 1, y, x + 2 * s + 1, y + 4 * s, color);
                break;
            case ZAP:
                context.fill(x + s, y, x + 2 * s, y + 4 * s, color);
                context.fill(x + 2 * s, y + s, x + 3 * s, y + 2 * s, color);
                context.fill(x, y + 2 * s, x + s, y + 3 * s, color);
                break;
            case KEY:
                context.fill(x + s, y + s, x + 2 * s, y + 2 * s, color);
                context.fill(x + 2 * s, y + 2 * s, x + 3 * s, y + 3 * s, color);
                break;
            case SLIDERS:
                context.fill(x, y + s, x + 4 * s, y + s + 1, color);
                context.fill(x + s, y + 2 * s, x + s + 1, y + 3 * s, color);
                context.fill(x + 2 * s, y, x + 2 * s + 1, y + 4 * s, color);
                context.fill(x + 3 * s, y + s, x + 3 * s + 1, y + 2 * s, color);
                break;
            case LAYERS:
                context.fill(x, y, x + 4 * s, y + 1, color);
                context.fill(x, y + 2 * s, x + 4 * s, y + 2 * s + 1, color);
                context.fill(x, y + 4 * s - 1, x + 4 * s, y + 4 * s, color);
                break;
            case TERMINAL:
                context.fill(x, y, x + 4 * s, y + 4 * s, color);
                context.fill(x + s, y + s, x + 3 * s, y + 2 * s, MvxmenuTheme.BG_0);
                break;
            case RADAR:
                context.fill(x + 2 * s - 1, y + 2 * s - 1, x + 2 * s + 1, y + 2 * s + 1, color);
                context.fill(x + 2 * s, y, x + 2 * s + 1, y + 4 * s, color);
                context.fill(x, y + 2 * s, x + 4 * s, y + 2 * s + 1, color);
                break;
            case LOCK:
                context.fill(x + s, y, x + 3 * s, y + s, color);
                context.fill(x + s, y + s, x + 3 * s, y + 3 * s, color);
                break;
            case CLOCK:
                context.fill(x + 2 * s - 1, y + 2 * s - 1, x + 2 * s + 1, y + 2 * s + 1, color);
                context.fill(x + 2 * s, y, x + 2 * s + 1, y + 3 * s, color);
                context.fill(x, y + 2 * s, x + 4 * s, y + 2 * s + 1, color);
                break;
            case CPU:
                context.fill(x + s, y, x + 3 * s, y + 4 * s, color);
                context.fill(x, y + s, x + s, y + 3 * s, color);
                context.fill(x + 3 * s, y + s, x + 4 * s, y + 3 * s, color);
                break;
            case MAP:
                context.fill(x + s, y + 1, x + 2 * s, y + 4 * s - 1, color);
                context.fill(x + 2 * s, y + 2 * s, x + 3 * s, y + 3 * s, color);
                break;
            case GRID:
                context.fill(x + s, y, x + s + 1, y + 4 * s, color);
                context.fill(x + 3 * s, y, x + 3 * s + 1, y + 4 * s, color);
                context.fill(x, y + 2 * s, x + 4 * s, y + 2 * s + 1, color);
                break;
            case BELL:
                context.fill(x + s, y + s, x + 3 * s, y + 3 * s, color);
                context.fill(x + 2 * s, y, x + 2 * s + 1, y + s, color);
                break;
            case DATABASE:
                context.fill(x, y, x + 4 * s, y + 1, color);
                context.fill(x, y + 2 * s, x + 4 * s, y + 2 * s + 1, color);
                context.fill(x, y + 4 * s - 1, x + 4 * s, y + 4 * s, color);
                break;
        }
    }

    public static MvxmenuIcons fromCategoryName(String category) {
        return switch (category) {
            case "COMBAT" -> CROSSHAIR;
            case "MOVEMENT" -> ZAP;
            case "PLAYER" -> EYE;
            case "RENDER" -> LAYERS;
            case "WORLD" -> MAP;
            case "EXPLOIT" -> SHIELD;
            case "MISC" -> SLIDERS;
            default -> SHIELD;
        };
    }

    public static int renderSize(int guiScale) {
        return switch (guiScale) {
            case 1 -> 24;
            case 2 -> 12;
            default -> 24;
        };
    }
}