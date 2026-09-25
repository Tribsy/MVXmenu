package dev.mvxmenu.ui.layout;

import dev.mvxmenu.config.MvxmenuUiConfig;
import dev.mvxmenu.ui.screen.MvxmenuScreen;
import net.minecraft.client.MinecraftClient;

/**
 * Layout calculations driven by shell dimensions from ModMenu_Shell.svg.
 * Shell: 1280x800 at GUI scale 1, sidebar 260, header 48, footer 32, window radius 19.
 */
public class MvxmenuLayout {

    // Shell dimensions (at GUI scale 1)
    private static final int SHELL_WIDTH = 1280;
    private static final int SHELL_HEIGHT = 800;
    private static final int SIDEBAR_WIDTH = 260;
    private static final int HEADER_HEIGHT = 48;
    private static final int FOOTER_HEIGHT = 32;
    private static final int PADDING = 16;  // SP_4
    private static final int WINDOW_RADIUS = 19;

    private final MvxmenuUiConfig config;
    private final MvxmenuUiConfig.LayoutConfig layout;

    public MvxmenuLayout() {
        this.config = MvxmenuUiConfig.get();
        this.layout = config.getLayout();
    }

    public int sidebarWidth() {
        int scaledWidth = getScaledWidthSafe();
        if (scaledWidth >= 640) return SIDEBAR_WIDTH;
        if (scaledWidth >= 427) return 200;
        return 180;
    }

    public int headerHeight() {
        return HEADER_HEIGHT;
    }

    public int footerHeight() {
        return FOOTER_HEIGHT;
    }

    public int padding() {
        return PADDING;
    }

    public int screenWidth() {
        int scaledWidth = getScaledWidthSafe();
        return scaledWidth > 0 ? Math.min(SHELL_WIDTH, scaledWidth) : SHELL_WIDTH;
    }

    public int screenHeight() {
        int scaledHeight = getScaledHeightSafe();
        return scaledHeight > 0 ? Math.min(SHELL_HEIGHT, scaledHeight) : SHELL_HEIGHT;
    }

    private int getScaledWidthSafe() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.getWindow() == null) {
            return 0;
        }
        return client.getWindow().getScaledWidth();
    }

    private int getScaledHeightSafe() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.getWindow() == null) {
            return 0;
        }
        return client.getWindow().getScaledHeight();
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

    public int windowRadius() {
        return WINDOW_RADIUS;
    }

    // Responsive module grid columns
    public int moduleGridColumns() {
        int contentW = contentWidth();
        int cardWidth = 200;
        int gap = 8;
        return Math.max(1, Math.min(4, (contentW + gap) / (cardWidth + gap)));
    }

    public int moduleCardWidth() {
        return 200;
    }

    public int moduleCardHeight() {
        return 80;
    }

    public int moduleCardGap() {
        return 8;
    }

    public int slotSize() {
        return layout.slotSize;
    }

    public int slotSpacing() {
        return layout.slotSpacing;
    }

    public int slotX(int column) {
        return contentX() + (column * (moduleCardWidth() + moduleCardGap()));
    }

    public int slotY(int row) {
        return contentY() + (row * (moduleCardHeight() + moduleCardGap()));
    }

    public int columnCount() {
        return moduleGridColumns();
    }

    public void recalculate() {
        // Config is live-referenced
    }

    public void calculate(MvxmenuScreen screen) {
        // Config-driven layout
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