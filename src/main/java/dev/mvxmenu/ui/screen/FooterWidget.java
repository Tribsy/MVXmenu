package dev.mvxmenu.ui.screen;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.widget.MvxmenuWidget;
import dev.mvxmenu.theme.FontRenderer;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;

/**
 * Footer widget - version, performance stats, keyboard shortcuts.
 * Height: 32px, BG_2 background, BD_1 top border.
 */
public class FooterWidget implements MvxmenuWidget {

    private final String id = "footer";
    private MvxmenuLayout.Bounds bounds;
    private final MvxmenuLayout layout;
    private boolean visible = true;

    public FooterWidget(MvxmenuLayout layout) {
        this.layout = layout;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public MvxmenuLayout.Bounds getBounds() {
        return bounds;
    }

    @Override
    public void setBounds(MvxmenuLayout.Bounds bounds) {
        this.bounds = bounds;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void setEnabled(boolean enabled) {}

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isHovered(int mouseX, int mouseY) {
        return bounds != null && bounds.contains(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (bounds == null || !visible) return;

        // Background with rounded corners (rx=19 to match window)
        RoundedRectRenderer.render(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_WINDOW, MvxmenuTheme.BG_2);

        // Top border
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + 1, MvxmenuTheme.BD_1);

        int y = bounds.y + (bounds.height - 10) / 2;
        int x = bounds.x + 12;

        // Version
        String version = "v1.0.0-rebrand";
        FontRenderer.drawTextSimple(context, version, x, y, MvxmenuTheme.TX_2, true, "ui");
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return "Footer";
    }

    @Override
    public Type getType() {
        return Type.SCREEN;
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public void setFocused(boolean focused) {}
}