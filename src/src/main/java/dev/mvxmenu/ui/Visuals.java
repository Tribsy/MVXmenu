package dev.mvxmenu.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import java.util.ArrayList;
import java.util.List;

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
        
        // Draw the inner cross (central rectangles)
        context.fill(x + r, y, x + width - r, y + height, color);
        context.fill(x, y + r, x + width, y + height - r, color);

        // Draw corners using a simple 8-segment arc approximation
        drawArc(context, x + r, y + r, r, 0, 90, color);           // Top-Left
        drawArc(context, x + width - r, y + r, r, 90, 180, color); // Top-Right
        drawArc(context, x + width - r, y + height - r, r, 180, 270, color); // Bottom-Right
        drawArc(context, x + r, y + height - r, r, 270, 360, color); // Bottom-Left
    }

    private static void drawArc(DrawContext context, int cx, int cy, int r, int startAngle, int endAngle, int color) {
        int segments = 8;
        for (int i = 0; i < segments; i++) {
            double angle1 = Math.toRadians(startAngle + (double)i / segments * (endAngle - startAngle));
            double angle2 = Math.toRadians(startAngle + (double)(i + 1) / segments * (endAngle - startAngle));
            
            int x1 = cx + (int)(r * Math.cos(angle1));
            int y1 = cy + (int)(r * Math.sin(angle1));
            int x2 = cx + (int)(r * Math.cos(angle2));
            int y2 = cy + (int)(r * Math.sin(angle2));
            
            // We use a thin fill for the arc segment
            context.fill(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2) + 1, Math.max(y1, y2) + 1, color);
        }
    }
}
