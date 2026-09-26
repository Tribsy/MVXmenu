package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.MvxmenuIcons;
import net.minecraft.client.gui.DrawContext;

public class IconButtonWidget implements MvxmenuWidget, NarratableWidget {

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private String iconName;
    private boolean active;
    private boolean disabled;
    private boolean visible = true;
    private boolean hovered;
    private boolean focused;

    public IconButtonWidget(String id, String iconName) {
        this.id = id;
        this.iconName = iconName;
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
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        hovered = isHovered(mouseX, mouseY);
        if (bounds == null) return;
        int bgColor = active ? MvxmenuTheme.AC_DIM : (hovered || focused) ? MvxmenuTheme.BG_2 : MvxmenuTheme.BG_1;
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, bgColor);
        MvxmenuIcons icon = MvxmenuIcons.valueOf(iconName);
        if (icon != null) {
            icon.render(context, bounds.x + 4, bounds.y + 4, 24, getTintColor());
        }
        if (focused) {
            FocusRing.render(context, bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2, MvxmenuTheme.AC);
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return iconName;
    }

    @Override
    public Type getType() {
        return Type.ICON_BUTTON;
    }

    @Override
    public String getNarrationText() {
        return "Icon button " + iconName + (active ? " active" : " inactive") + (disabled ? " disabled" : "");
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

    public String getIconName() {
        return iconName;
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

    public int getTintColor() {
        if (disabled) return MvxmenuTheme.TX_2;
        if (active) return MvxmenuTheme.TX_0;
        if (hovered || focused) return MvxmenuTheme.TX_1;
        return MvxmenuTheme.TX_2;
    }
}