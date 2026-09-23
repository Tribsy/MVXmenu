package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import net.minecraft.client.gui.DrawContext;

public class FocusRing {

    private static final int FOCUS_THICKNESS = 2;
    private static final int FOCUS_OFFSET = 1;

    public static void render(DrawContext context, int x, int y, int width, int height, int color) {
        context.fill(x, y, x + width, y + FOCUS_THICKNESS, color);
        context.fill(x, y + height - FOCUS_THICKNESS, x + width, y + height, color);
        context.fill(x, y, x + FOCUS_THICKNESS, y + height, color);
        context.fill(x + width - FOCUS_THICKNESS, y, x + width, y + height, color);
    }

    public static MvxmenuLayout.Bounds getFocusBounds(MvxmenuLayout.Bounds widgetBounds) {
        return new MvxmenuLayout.Bounds(
                widgetBounds.x - FOCUS_OFFSET,
                widgetBounds.y - FOCUS_OFFSET,
                widgetBounds.width + (FOCUS_OFFSET * 2),
                widgetBounds.height + (FOCUS_OFFSET * 2)
        );
    }
}
