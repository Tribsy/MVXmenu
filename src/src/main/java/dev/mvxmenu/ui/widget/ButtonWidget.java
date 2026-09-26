package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.gui.DrawContext;

public class ButtonWidget implements MvxmenuWidget, NarratableWidget {

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private ButtonVariant variant;
    private String text;
    private boolean enabled = true;
    private boolean active;
    private boolean visible = true;
    private boolean hovered;
    private boolean focused;

    public ButtonWidget(String id, ButtonVariant variant, String text) {
        this.id = id;
        this.variant = variant;
        this.text = text;
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
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
        if (!enabled) return false;
        active = true;
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        hovered = isHovered(mouseX, mouseY) && enabled;
        if (bounds == null) return;
        
        int bgColor = (hovered || focused) ? variant.bgColor : MvxmenuTheme.BG_1;
        int borderColor = variant.bgColor;

        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, bgColor);
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + 2, borderColor);
        if (focused) {
            FocusRing.render(context, bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2, MvxmenuTheme.AC);
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return text;
    }

    @Override
    public Type getType() {
        return Type.BUTTON;
    }

    @Override
    public String getNarrationText() {
        return "Button " + text + (enabled ? "" : " disabled") + (active ? " active" : "");
    }

    @Override
    public Priority getNarrationPriority() {
        return Priority.NORMAL;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public ButtonVariant getVariant() {
        return variant;
    }

    public String getText() {
        return text;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
