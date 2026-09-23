package dev.mvxmenu.ui.layout;

import dev.mvxmenu.config.MvxmenuUiConfig;
import dev.mvxmenu.ui.screen.MvxmenuScreen;

public class MvxmenuLayout {

    private final MvxmenuUiConfig config;
    private final MvxmenuUiConfig.LayoutConfig layout;

    public MvxmenuLayout() {
        this.config = MvxmenuUiConfig.get();
        this.layout = config.getLayout();
    }

    public int sidebarWidth() {
        return layout.sidebarWidth;
    }

    public int headerHeight() {
        return layout.headerHeight;
    }

    public int footerHeight() {
        return layout.footerHeight;
    }

    public int padding() {
        return layout.padding;
    }

    public int screenWidth() {
        return layout.screenWidth;
    }

    public int screenHeight() {
        return layout.screenHeight;
    }

    public int contentX() {
        return sidebarWidth() + padding();
    }

    public int contentY() {
        return headerHeight() + padding();
    }

    public int contentWidth() {
        return screenWidth() - sidebarWidth() - (padding() * 2);
    }

    public int contentHeight() {
        return screenHeight() - headerHeight() - footerHeight() - (padding() * 2);
    }

    public int sidebarX() {
        return 0;
    }

    public int sidebarY() {
        return 0;
    }

    public int sidebarContentWidth() {
        return sidebarWidth();
    }

    public int sidebarContentHeight() {
        return screenHeight() - headerHeight() - footerHeight();
    }

    public int headerX() {
        return 0;
    }

    public int headerY() {
        return 0;
    }

    public int headerWidth() {
        return screenWidth();
    }

    public int footerX() {
        return 0;
    }

    public int footerY() {
        return screenHeight() - footerHeight();
    }

    public int footerWidth() {
        return screenWidth();
    }

    public int slotSize() {
        return layout.slotSize;
    }

    public int slotSpacing() {
        return layout.slotSpacing;
    }

    public int slotX(int column) {
        return contentX() + (column * (slotSize() + slotSpacing()));
    }

    public int slotY(int row) {
        return contentY() + (row * (slotSize() + slotSpacing()));
    }

    public int columnCount() {
        return Math.max(1, (contentWidth() + slotSpacing()) / (slotSize() + slotSpacing()));
    }

    public void recalculate() {
        // Config is live-referenced, no recalculation needed unless config changes
    }

    public void calculate(MvxmenuScreen screen) {
        // Config-driven layout, no manual calculation needed
    }

    public static final class Bounds {
        public final int x;
        public final int y;
        public final int width;
        public final int height;

        public Bounds(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean contains(int px, int py) {
            return px >= x && px < x + width && py >= y && py < y + height;
        }

        public int centerX() {
            return x + width / 2;
        }

        public int centerY() {
            return y + height / 2;
        }
    }
}
