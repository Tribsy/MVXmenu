package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import net.minecraft.client.gui.DrawContext;

public class FocusRing {

    private static final int FOCUS_THICKNESS = 2;
    private static final int FOCUS_OFFSET = 2;
    private static final int GLOW_OFFSET = 4;
    private static final int GLOW_THICKNESS = 4;

    /**
     * Renders a focus ring with glow effect.
     */
    public static void render(DrawContext context, int x, int y, int width, int height, int color) {
        // Inner focus ring (solid)
        context.fill(x, y, x + width, y + FOCUS_THICKNESS, color);
        context.fill(x, y + height - FOCUS_THICKNESS, x + width, y + height, color);
        context.fill(x, y, x + FOCUS_THICKNESS, y + height, color);
        context.fill(x + width - FOCUS_THICKNESS, y, x + width, y + height, color);
    }

    /**
     * Renders a rounded focus ring with glow (AC_GLOW).
     */
    public static void renderRounded(DrawContext context, int x, int y, int width, int height, int radius, int color) {
        int glowColor = MvxmenuTheme.AC_GLOW;
        int ringColor = color;

        // Outer glow (larger radius, lower alpha)
        RoundedRectRenderer.renderBorder(context, x - GLOW_OFFSET, y - GLOW_OFFSET,
                width + GLOW_OFFSET * 2, height + GLOW_OFFSET * 2,
                radius + GLOW_OFFSET, GLOW_THICKNESS, glowColor, 0);

        // Inner focus ring
        RoundedRectRenderer.renderBorder(context, x - FOCUS_OFFSET, y - FOCUS_OFFSET,
                width + FOCUS_OFFSET * 2, height + FOCUS_OFFSET * 2,
                radius + FOCUS_OFFSET, FOCUS_THICKNESS, ringColor, 0);
    }

    /**
     * Renders a simple rectangular focus ring (for backward compatibility).
     */
    public static void renderRect(DrawContext context, int x, int y, int width, int height, int color) {
        render(context, x, y, width, height, color);
    }

    public static MvxmenuLayout.Bounds getFocusBounds(MvxmenuLayout.Bounds widgetBounds) {
        return new MvxmenuLayout.Bounds(
                widgetBounds.x - FOCUS_OFFSET,
                widgetBounds.y - FOCUS_OFFSET,
                widgetBounds.width + (FOCUS_OFFSET * 2),
                widgetBounds.height + (FOCUS_OFFSET * 2)
        );
    }

    public static MvxmenuLayout.Bounds getGlowBounds(MvxmenuLayout.Bounds widgetBounds) {
        return new MvxmenuLayout.Bounds(
                widgetBounds.x - GLOW_OFFSET,
                widgetBounds.y - GLOW_OFFSET,
                widgetBounds.width + (GLOW_OFFSET * 2),
                widgetBounds.height + (GLOW_OFFSET * 2)
        );
    }
}