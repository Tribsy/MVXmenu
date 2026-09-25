package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.MvxmenuIcons;
import dev.mvxmenu.theme.RoundedRectRenderer;
import dev.mvxmenu.theme.FontRenderer;
import dev.mvxmenu.util.MathUtil;
import net.minecraft.client.gui.DrawContext;

public class IconButtonWidget implements MvxmenuWidget, NarratableWidget {

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private MvxmenuIcons icon;
    private boolean active;
    private boolean disabled;
    private boolean visible = true;
    private boolean hovered;
    private boolean focused;
    private String tooltip;

    // Animation
    private float hoverProgress = 0f;
    private float focusProgress = 0f;

    public IconButtonWidget(String id, MvxmenuIcons icon) {
        this.id = id;
        this.icon = icon;
    }

    public IconButtonWidget(String id, String iconName) {
        this.id = id;
        try {
            this.icon = MvxmenuIcons.valueOf(iconName);
        } catch (IllegalArgumentException e) {
            this.icon = MvxmenuIcons.SHIELD;
        }
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
        return !disabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.disabled = !enabled;
    }

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
        if (bounds == null || !bounds.contains((int) x, (int) y)) return false;
        if (disabled) return false;
        active = !active;
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if ((keyCode == 257 || keyCode == 32) && focused && !disabled) { // Enter or Space
            active = !active;
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        hovered = isHovered(mouseX, mouseY);
        if (bounds == null) return;

        hoverProgress = MathUtil.lerp(hoverProgress, hovered ? 1f : 0f, delta * 10f);
        focusProgress = MathUtil.lerp(focusProgress, focused ? 1f : 0f, delta * 10f);

        int bgColor;
        if (disabled) {
            bgColor = MvxmenuTheme.BG_1;
        } else if (active) {
            bgColor = MvxmenuTheme.AC_DIM;
        } else {
            bgColor = MathUtil.lerpColor(MvxmenuTheme.BG_1, MvxmenuTheme.BG_2, hoverProgress);
        }

        RoundedRectRenderer.render(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_BUTTON, bgColor);

        if (active || hovered) {
            int accentAlpha = active ? 255 : (int)(255 * hoverProgress);
            int accentColor = (MvxmenuTheme.AC & 0x00FFFFFF) | (accentAlpha << 24);
            context.fill(bounds.x + 2, bounds.y + 2, bounds.x + bounds.width - 2, bounds.y + 4, accentColor);
        }

        // Icon
        int iconSize = Math.min(bounds.width, bounds.height) - 8;
        int iconX = bounds.x + (bounds.width - iconSize) / 2;
        int iconY = bounds.y + (bounds.height - iconSize) / 2;
        int iconColor = getTintColor();
        icon.render(context, iconX, iconY, iconSize, iconColor);

        // Focus ring
        if (focused) {
            int ringAlpha = (int) (255 * focusProgress);
            int ringColor = (MvxmenuTheme.AC & 0x00FFFFFF) | (ringAlpha << 24);
            RoundedRectRenderer.renderBorder(context,
                    bounds.x - 2, bounds.y - 2,
                    bounds.width + 4, bounds.height + 4,
                    MvxmenuTheme.R_BUTTON + 2, 2, ringColor, bgColor);
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return tooltip != null ? tooltip : (icon != null ? icon.name() : id);
    }

    @Override
    public Type getType() {
        return Type.ICON_BUTTON;
    }

    @Override
    public String getNarrationText() {
        String name = icon != null ? icon.name() : id;
        return "Icon button " + name + (active ? " active" : " inactive") + (disabled ? " disabled" : "");
    }

    @Override
    public Priority getNarrationPriority() {
        return Priority.LOW;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public float getHoverProgress() {
        return hoverProgress;
    }

    @Override
    public void setHoverProgress(float progress) {
        this.hoverProgress = progress;
    }

    @Override
    public float getFocusProgress() {
        return focusProgress;
    }

    @Override
    public void setFocusProgress(float progress) {
        this.focusProgress = progress;
    }

    public MvxmenuIcons getIcon() {
        return icon;
    }

    public void setIcon(MvxmenuIcons icon) {
        this.icon = icon;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public int getTintColor() {
        if (disabled) return MvxmenuTheme.TX_2;
        if (active) return MvxmenuTheme.TX_0;
        if (hovered || focused) return MvxmenuTheme.TX_1;
        return MvxmenuTheme.TX_2;
    }
}