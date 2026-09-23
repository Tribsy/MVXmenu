package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.gui.DrawContext;

public class SliderWidget implements MvxmenuWidget, NarratableWidget {

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private String label;
    private int value;
    private int min;
    private int max;
    private boolean visible = true;
    private boolean dragging;
    private boolean focused;

    public SliderWidget(String id, String label, int min, int max, int initial) {
        this.id = id;
        this.label = label;
        this.min = min;
        this.max = max;
        this.value = clamp(initial, min, max);
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
        dragging = true;
        updateValueFromMouse((int) x);
        return true;
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        dragging = false;
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (bounds == null) return;
        context.fill(bounds.x, bounds.y + bounds.height / 2 - 3, bounds.x + bounds.width, bounds.y + bounds.height / 2 + 3, MvxmenuTheme.BD_2);
        int fillWidth = (int) ((float) (bounds.width - 12) * ((float) value / (float) max));
        context.fill(bounds.x + 6, bounds.y + bounds.height / 2 - 2, bounds.x + 6 + fillWidth, bounds.y + bounds.height / 2 + 2, MvxmenuTheme.AC);
        int knobX = bounds.x + 6 + fillWidth - 5;
        context.fill(knobX, bounds.y + 2, knobX + 10, bounds.y + bounds.height - 2, MvxmenuTheme.TX_0);
        if (focused) {
            FocusRing.render(context, bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2, MvxmenuTheme.AC);
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return label + ": " + value + "%";
    }

    @Override
    public Type getType() {
        return Type.SLIDER;
    }

    @Override
    public String getNarrationText() {
        return label + " slider at " + value + " percent";
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = clamp(value, min, max);
    }

    public String getLabel() {
        return label;
    }

    private void updateValueFromMouse(int mouseX) {
        if (bounds == null) return;
        int trackWidth = bounds.width - 12;
        int relX = mouseX - bounds.x - 6;
        float pct = Math.max(0.0f, Math.min(1.0f, (float) relX / (float) trackWidth));
        value = clamp(Math.round(min + (max - min) * pct), min, max);
    }

    private int clamp(int v, int lo, int hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}