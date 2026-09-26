package dev.mvxmenu.theme;

import net.minecraft.client.gui.DrawContext;

/**
 * Renders rounded rectangles using multiple fill calls.
 * Compatible with Minecraft 1.21.4 without requiring Tessellator API changes.
 */
public final class RoundedRectRenderer {

    private RoundedRectRenderer() {}

    /**
     * Renders a filled rounded rectangle using multiple rectangles.
     */
    public static void render(DrawContext context, int x, int y, int width, int height,
                              int radius, int color) {
        if (width <= 0 || height <= 0) return;
        radius = Math.min(radius, Math.min(width, height) / 2);
        if (radius <= 0) {
            context.fill(x, y, x + width, y + height, color);
            return;
        }

        // Center rectangle
        context.fill(x + radius, y + radius, x + width - radius, y + height - radius, color);

        // Top and bottom bars
        context.fill(x + radius, y, x + width - radius, y + radius, color);
        context.fill(x + radius, y + height - radius, x + width - radius, y + height, color);

        // Left and right bars
        context.fill(x, y + radius, x + radius, y + height - radius, color);
        context.fill(x + width - radius, y + radius, x + width, y + height - radius, color);

        // Four corners - draw as small filled squares (approximation)
        // For a better approximation, we draw quarter circles using multiple lines
        drawCorner(context, x + radius, y + radius, radius, color, 0);       // Top-left
        drawCorner(context, x + width - radius, y + radius, radius, color, 1); // Top-right
        drawCorner(context, x + width - radius, y + height - radius, radius, color, 2); // Bottom-right
        drawCorner(context, x + radius, y + height - radius, radius, color, 3); // Bottom-left
    }

    private static void drawCorner(DrawContext context, int cx, int cy, int radius, int color, int quadrant) {
        // Draw corner as a series of horizontal lines to approximate quarter circle
        // quadrant: 0=TL, 1=TR, 2=BR, 3=BL
        for (int i = 0; i < radius; i++) {
            int dx = (int) Math.sqrt(radius * radius - i * i);
            int x1, x2, y;
            switch (quadrant) {
                case 0: // Top-left
                    x1 = cx - dx;
                    x2 = cx;
                    y = cy - i;
                    break;
                case 1: // Top-right
                    x1 = cx;
                    x2 = cx + dx;
                    y = cy - i;
                    break;
                case 2: // Bottom-right
                    x1 = cx;
                    x2 = cx + dx;
                    y = cy + i;
                    break;
                case 3: // Bottom-left
                    x1 = cx - dx;
                    x2 = cx;
                    y = cy + i;
                    break;
                default:
                    return;
            }
            context.fill(x1, y, x2, y + 1, color);
        }
    }

    /**
     * Renders a rounded rectangle border (stroke).
     */
    public static void renderBorder(DrawContext context, int x, int y, int width, int height,
                                     int radius, int strokeWidth, int strokeColor, int fillColor) {
        render(context, x, y, width, height, radius, strokeColor);
        int innerRadius = Math.max(0, radius - strokeWidth);
        render(context, x + strokeWidth, y + strokeWidth,
               width - strokeWidth * 2, height - strokeWidth * 2,
               innerRadius, fillColor);
    }

    /**
     * Renders a rounded rectangle with a top accent bar.
     */
    public static void renderWithTopAccent(DrawContext context, int x, int y, int width, int height,
                                            int radius, int bgColor, int accentColor, int accentHeight) {
        render(context, x, y, width, height, radius, bgColor);
        if (accentHeight > 0) {
            context.fill(x, y, x + width, y + accentHeight, accentColor);
        }
    }

    /**
     * Renders a left status bar on a rounded rectangle.
     */
    public static void renderWithLeftStatusBar(DrawContext context, int x, int y, int width, int height,
                                                int radius, int barWidth, int barColor) {
        render(context, x, y, barWidth, height, radius, barColor);
    }
}