package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.gui.DrawContext;

public class CheckboxWidget implements MvxmenuWidget, NarratableWidget {

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private String label;
    private boolean checked;
    private boolean visible = true;
    private boolean hovered;
    private boolean focused;

    public CheckboxWidget(String id, String label, boolean initial) {
        this.id = id;
        this.label = label;
        this.checked = initial;
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
        checked = !checked;
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
        int boxColor = checked ? MvxmenuTheme.AC : (hovered || focused ? MvxmenuTheme.BD_1 : MvxmenuTheme.BD_2);
        context.fill(bounds.x, bounds.y, bounds.x + 12, bounds.y + 12, boxColor);
        if (checked) {
            context.fill(bounds.x + 3, bounds.y + 5, bounds.x + 6, bounds.y + 8, MvxmenuTheme.TX_0);
            context.fill(bounds.x + 6, bounds.y + 3, bounds.x + 9, bounds.y + 6, MvxmenuTheme.TX_0);
        }
        if (focused) {
            FocusRing.render(context, bounds.x - 1, bounds.y - 1, 14, 14, MvxmenuTheme.AC);
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return label + ": " + (checked ? "ON" : "OFF");
    }

    @Override
    public Type getType() {
        return Type.CHECKBOX;
    }

    @Override
    public String getNarrationText() {
        return "Checkbox " + label + " " + (checked ? "checked" : "unchecked");
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getLabel() {
        return label;
    }
}