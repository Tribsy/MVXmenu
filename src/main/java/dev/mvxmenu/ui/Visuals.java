package dev.mvxmenu.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;

public class Visuals {

    /**
     * Draws a high-fidelity rounded rectangle using a multi-segment approximation.
     * 
     * @param context The draw context
     * @param x Top-left X
     * @param y Top-left Y
     * @param width Width
     * @param height Height
     * @param radius Corner radius
     * @param color ARGB color
     */
    public static void drawRoundedRect(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        if (radius <= 0) {
            context.fill(x, y, x + width, y + height, color);
            return;
        }

        int r = Math.min(radius, Math.min(width / 2, height / 2));
        
        // Draw the inner cross (central rectangles) to fill the bulk of the area
        context.fill(x + r, y, x + width - r, y + height, color);
        context.fill(x, y + r, x + width, y + height - r, color);

        // Draw corners using a more dense 12-segment arc approximation
        drawArc(context, x + r, y + r, r, 180, 270, color); // Top-Left
        drawArc(context, x + width - r, y + r, r, 270, 360, color); // Top-Right
        drawArc(context, x + width - r, y + height - r, r, 0, 90, color); // Bottom-Right
        drawArc(context, x + r, y + height - r, r, 90, 180, color); // Bottom-Left
    }

    private static void drawArc(DrawContext context, int cx, int cy, int r, int startAngle, int endAngle, int color) {
        int segments = 12;
        for (int i = 0; i < segments; i++) {
            double angle1 = Math.toRadians(startAngle + (double)i / segments * (endAngle - startAngle));
            double angle2 = Math.toRadians(startAngle + (double)(i + 1) / segments * (endAngle - startAngle));
            
            int x1 = cx + (int)(r * Math.cos(angle1));
            int y1 = cy + (int)(r * Math.sin(angle1));
            int x2 = cx + (int)(r * Math.cos(angle2));
            int y2 = cy + (int)(r * Math.sin(angle2));
            
            // Use a slightly overlapped fill to avoid seams
            context.fill(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2) + 1, Math.max(y1, y2) + 1, color);
        }
    }
}
