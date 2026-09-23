package dev.mvxmenu.ui.layout;

import dev.mvxmenu.ui.screen.MvxmenuScreen;

public class MvxmenuLayout {

    public static final int SIDEBAR_WIDTH = 180;
    public static final int HEADER_HEIGHT = 48;
    public static final int FOOTER_HEIGHT = 32;
    public static final int PADDING = 16;

    public static final int SCREEN_WIDTH = 1200;
    public static final int SCREEN_HEIGHT = 800;

    public static final int CONTENT_X = SIDEBAR_WIDTH + PADDING;
    public static final int CONTENT_Y = HEADER_HEIGHT + PADDING;
    public static final int CONTENT_WIDTH = SCREEN_WIDTH - SIDEBAR_WIDTH - (PADDING * 2);
    public static final int CONTENT_HEIGHT = SCREEN_HEIGHT - HEADER_HEIGHT - FOOTER_HEIGHT - (PADDING * 2);

    public static final int SIDEBAR_X = 0;
    public static final int SIDEBAR_Y = 0;
    public static final int SIDEBAR_CONTENT_WIDTH = SIDEBAR_WIDTH;
    public static final int SIDEBAR_CONTENT_HEIGHT = SCREEN_HEIGHT - HEADER_HEIGHT - FOOTER_HEIGHT;

    public static final int HEADER_X = 0;
    public static final int HEADER_Y = 0;
    public static final int HEADER_WIDTH = SCREEN_WIDTH;

    public static final int FOOTER_X = 0;
    public static final int FOOTER_Y = SCREEN_HEIGHT - FOOTER_HEIGHT;
    public static final int FOOTER_WIDTH = SCREEN_WIDTH;

    public static final int SLOT_SIZE = 72;
    public static final int SLOT_SPACING = 4;

    public static int sidebarStartX() {
        return SIDEBAR_X;
    }

    public static int sidebarStartY() {
        return HEADER_HEIGHT;
    }

    public static int sidebarWidth() {
        return SIDEBAR_WIDTH;
    }

    public static int sidebarHeight() {
        return SIDEBAR_CONTENT_HEIGHT;
    }

    public static int contentStartX() {
        return CONTENT_X;
    }

    public static int contentStartY() {
        return CONTENT_Y;
    }

    public static int contentWidth() {
        return CONTENT_WIDTH;
    }

    public static int contentHeight() {
        return CONTENT_HEIGHT;
    }

    public static int headerHeight() {
        return HEADER_HEIGHT;
    }

    public static int footerHeight() {
        return FOOTER_HEIGHT;
    }

    public static int padding() {
        return PADDING;
    }

    public static int slotWidth() {
        return SLOT_SIZE;
    }

    public static int slotHeight() {
        return SLOT_SIZE;
    }

    public static int slotX(int column) {
        return CONTENT_X + (column * (SLOT_SIZE + SLOT_SPACING));
    }

    public static int slotY(int row) {
        return CONTENT_Y + (row * (SLOT_SIZE + SLOT_SPACING));
    }

    public static int columnCount() {
        return Math.max(1, (CONTENT_WIDTH + SLOT_SPACING) / (SLOT_SIZE + SLOT_SPACING));
    }

    public void calculate(MvxmenuScreen screen) {
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
