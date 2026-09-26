package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.gui.DrawContext;

public class ToggleWidget implements MvxmenuWidget, NarratableWidget {

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private boolean enabled;
    private boolean visible = true;
    private boolean hovered;
    private boolean focused;

    public ToggleWidget(String id, boolean initial) {
        this.id = id;
        this.enabled = initial;
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
        enabled = !enabled;
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
        int trackColor = enabled ? MvxmenuTheme.AC : (hovered || focused ? MvxmenuTheme.BD_1 : MvxmenuTheme.BD_2);
        context.fill(bounds.x, bounds.y + bounds.height / 2 - 6, bounds.x + bounds.width, bounds.y + bounds.height / 2 + 6, trackColor);
        int knobX = enabled ? bounds.x + bounds.width - 14 : bounds.x + 2;
        context.fill(knobX, bounds.y + 2, knobX + 10, bounds.y + bounds.height - 2, MvxmenuTheme.TX_0);
        if (focused) {
            FocusRing.render(context, bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2, MvxmenuTheme.AC);
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return enabled ? "Enabled" : "Disabled";
    }

    @Override
    public Type getType() {
        return Type.TOGGLE;
    }

    @Override
    public String getNarrationText() {
        return "Toggle " + (enabled ? "on" : "off");
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

    public boolean getEnabled() {
        return enabled;
    }
}