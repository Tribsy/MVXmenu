package dev.mvxmenu.ui.screen;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.widget.MvxmenuWidget;
import dev.mvxmenu.theme.FontRenderer;
import dev.mvxmenu.theme.MvxmenuIcons;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

/**
 * Header widget - brand logo, title, status badges.
 * Height: 48px, BG_2 background, purple top border (2px).
 */
public class HeaderWidget implements MvxmenuWidget {

    private final String id = "header";
    private MvxmenuLayout.Bounds bounds;
    private final MvxmenuLayout layout;
    private float hoverProgress = 0f;
    private boolean visible = true;

    public HeaderWidget(MvxmenuLayout layout) {
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

        boolean hovered = isHovered(mouseX, mouseY);
        hoverProgress = lerp(hoverProgress, hovered ? 1f : 0f, delta * 10f);

        // Background with rounded corners (rx=19 to match window)
        RoundedRectRenderer.render(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_WINDOW, MvxmenuTheme.BG_2);

        // Purple top border (2px)
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + 2, MvxmenuTheme.AC);

        // Bottom border
        context.fill(bounds.x, bounds.y + bounds.height - 1,
                bounds.x + bounds.width, bounds.y + bounds.height, MvxmenuTheme.BD_1);

        // Brand logo (shield icon) + title
        int logoX = bounds.x + 12;
        int logoY = bounds.y + (bounds.height - 24) / 2;
        MvxmenuIcons.SHIELD.render(context, logoX, logoY, 24, MvxmenuTheme.AC);

        // Title text
        int titleX = logoX + 24 + 8;
        int titleY = bounds.y + (bounds.height - 12) / 2;
        FontRenderer.drawTextSimple(context, "MVX // HUD", titleX, titleY, MvxmenuTheme.TX_0, true, "ui");
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return "MVXmenu Header";
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

    private float lerp(float a, float b, float t) {
        return a + (b - a) * Math.min(1f, t);
    }
}