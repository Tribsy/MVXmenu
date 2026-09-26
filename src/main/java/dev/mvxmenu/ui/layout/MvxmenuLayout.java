package dev.mvxmenu.ui.layout;

import dev.mvxmenu.config.MvxmenuConfig;
import dev.mvxmenu.config.MvxmenuUiConfig;
import dev.mvxmenu.ui.screen.MvxmenuScreen;
import net.minecraft.client.MinecraftClient;

/**
 * Layout calculations driven by shell dimensions from ModMenu_Shell.svg.
 * Shell: 1280x800 at GUI scale 1, sidebar 260, header 48, footer 32, window radius 19.
 */
public class MvxmenuLayout {

    // Design resolution (at effective GUI scale 1) - loaded from UI config
    private static final int DEFAULT_DESIGN_WIDTH = 1280;
    private static final int DEFAULT_DESIGN_HEIGHT = 800;
    private static final int SIDEBAR_WIDTH = 260;
    private static final int HEADER_HEIGHT = 48;
    private static final int FOOTER_HEIGHT = 32;
    private static final int PADDING = 16;  // SP_4
    private static final int WINDOW_RADIUS = 19;

    private final MvxmenuUiConfig uiConfig;
    private final MvxmenuUiConfig.LayoutConfig layout;
    private MvxmenuConfig modConfig;

    public MvxmenuLayout() {
        this(null);
    }

    public MvxmenuLayout(MvxmenuConfig modConfig) {
        this.uiConfig = MvxmenuUiConfig.get();
        this.layout = uiConfig.getLayout();
        this.modConfig = modConfig;
    }

    public void setModConfig(MvxmenuConfig modConfig) {
        this.modConfig = modConfig;
    }

    /**
     * Computes the effective GUI scale:
     * - If mod's guiScale > 0: use that value
     * - If mod's guiScale == 0 (Auto): use Minecraft's native GUI scale
     * - Fallback: 1
     */
    private int getEffectiveGuiScale() {
        if (modConfig != null) {
            int modGuiScale = modConfig.getGuiScale();
            if (modGuiScale > 0) {
                return modGuiScale;
            }
            // Auto mode (0) - use Minecraft's native GUI scale
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && client.options != null) {
                int nativeScale = client.options.getGuiScale().getValue();
                if (nativeScale > 0) {
                    return nativeScale;
                }
                // Native is also Auto (0) - compute from window
                if (client.getWindow() != null) {
                    return client.getWindow().calculateScaleFactor(0, false);
                }
            }
        }
        return 1;
    }

    private int getDesignWidth() {
        return layout != null ? layout.screenWidth : DEFAULT_DESIGN_WIDTH;
    }

    private int getDesignHeight() {
        return layout != null ? layout.screenHeight : DEFAULT_DESIGN_HEIGHT;
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

    /**
     * HUD content area width in Minecraft's scaled coordinates.
     * Design width divided by effective GUI scale, capped at viewport.
     */
    public int screenWidth() {
        int effectiveScale = getEffectiveGuiScale();
        int designW = getDesignWidth();
        int scaledW = getScaledWidthSafe();
        return Math.min(designW / Math.max(1, effectiveScale), scaledW);
    }

    /**
     * HUD content area height in Minecraft's scaled coordinates.
     * Design height divided by effective GUI scale, capped at viewport.
     */
    public int screenHeight() {
        int effectiveScale = getEffectiveGuiScale();
        int designH = getDesignHeight();
        int scaledH = getScaledHeightSafe();
        return Math.min(designH / Math.max(1, effectiveScale), scaledH);
    }

    /**
     * Full viewport dimensions in scaled coordinates (for background fill).
     */
    public int viewportWidth() {
        int scaledWidth = getScaledWidthSafe();
        return scaledWidth > 0 ? scaledWidth : DEFAULT_DESIGN_WIDTH;
    }

    public int viewportHeight() {
        int scaledHeight = getScaledHeightSafe();
        return scaledHeight > 0 ? scaledHeight : DEFAULT_DESIGN_HEIGHT;
    }

    /**
     * Shell position - centered in viewport.
     */
    public int shellX() {
        return Math.max(0, (viewportWidth() - screenWidth()) / 2);
    }

    public int shellY() {
        return Math.max(0, (viewportHeight() - screenHeight()) / 2);
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
        return shellX() + sidebarWidth() + padding();
    }

    public int contentY() {
        return shellY() + headerHeight() + padding();
    }

    public int contentWidth() {
        return screenWidth() - sidebarWidth() - (padding() * 2);
    }

    public int contentHeight() {
        return screenHeight() - headerHeight() - footerHeight() - (padding() * 2);
    }

    public int sidebarX() {
        return shellX();
    }

    public int sidebarY() {
        return shellY();
    }

    public int sidebarContentWidth() {
        return sidebarWidth();
    }

    public int sidebarContentHeight() {
        return screenHeight() - headerHeight() - footerHeight();
    }

    public int headerX() {
        return shellX();
    }

    public int headerY() {
        return shellY();
    }

    public int headerWidth() {
        return screenWidth();
    }

    public int footerX() {
        return shellX();
    }

    public int footerY() {
        return shellY() + screenHeight() - footerHeight();
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