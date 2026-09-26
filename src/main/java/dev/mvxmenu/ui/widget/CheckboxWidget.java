package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import dev.mvxmenu.theme.FontRenderer;
import dev.mvxmenu.util.MathUtil;
import net.minecraft.client.gui.DrawContext;

public class CheckboxWidget implements MvxmenuWidget, NarratableWidget {

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private String label;
    private boolean checked;
    private boolean visible = true;
    private boolean hovered;
    private boolean focused;

    // Animation
    private float hoverProgress = 0f;
    private float focusProgress = 0f;
    private float checkProgress = 0f;

    public CheckboxWidget(String id, String label, boolean initial) {
        this.id = id;
        this.label = label;
        this.checked = initial;
        this.checkProgress = initial ? 1f : 0f;
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
        if (bounds == null || !bounds.contains((int) x, (int) y)) return false;
        checked = !checked;
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if ((keyCode == 257 || keyCode == 32) && focused) { // Enter or Space
            checked = !checked;
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
        checkProgress = MathUtil.lerp(checkProgress, checked ? 1f : 0f, delta * 15f);

        int boxSize = Math.min(bounds.width, bounds.height);
        int boxX = bounds.x;
        int boxY = bounds.y + (bounds.height - boxSize) / 2;

        // Box background
        int boxColor = MathUtil.lerpColor(MvxmenuTheme.BD_2, MvxmenuTheme.AC, checkProgress);
        if (!checked) {
            boxColor = MathUtil.lerpColor(MvxmenuTheme.BD_2, MvxmenuTheme.BD_1, hoverProgress);
        }

        RoundedRectRenderer.render(context, boxX, boxY, boxSize, boxSize, MvxmenuTheme.R_INPUT, boxColor);

        // Border
        int borderColor = checked ? MvxmenuTheme.AC : MathUtil.lerpColor(MvxmenuTheme.BD_1, MvxmenuTheme.AC, hoverProgress);
        RoundedRectRenderer.renderBorder(context, boxX, boxY, boxSize, boxSize,
                MvxmenuTheme.R_INPUT, 1, borderColor, boxColor);

        // Checkmark (animated)
        if (checkProgress > 0.01f) {
            int checkColor = MvxmenuTheme.TX_0;
            int cx = boxX + boxSize / 2;
            int cy = boxY + boxSize / 2;
            int checkSize = (int) (boxSize * 0.5f * checkProgress);

            // Draw checkmark as two lines
            int x1 = cx - checkSize / 2;
            int y1 = cy;
            int x2 = cx - checkSize / 6;
            int y2 = cy + checkSize / 3;
            int x3 = cx + checkSize / 2;
            int y3 = cy - checkSize / 3;

            // Line 1
            context.fill(x1, y1, x2, y1 + 2, checkColor);
            context.fill(x2 - 1, y1 - 1, x2 + 1, y2 + 1, checkColor);
            // Line 2
            context.fill(x2 - 1, y2 - 1, x3 + 1, y2 + 1, checkColor);
            context.fill(x3 - 1, y3 - 1, x3 + 1, y3 + 1, checkColor);
        }

        // Label
        if (label != null && !label.isEmpty()) {
            FontRenderer.drawTextSimple(context, label, boxX + boxSize + 8, bounds.y + (bounds.height - 10) / 2, MvxmenuTheme.TX_0, true, "ui");
        }

        // Focus ring
        if (focused) {
            int ringAlpha = (int) (255 * focusProgress);
            int ringColor = (MvxmenuTheme.AC & 0x00FFFFFF) | (ringAlpha << 24);
            RoundedRectRenderer.renderBorder(context,
                    boxX - 2, boxY - 2,
                    boxSize + 4, boxSize + 4,
                    MvxmenuTheme.R_INPUT + 2, 2, ringColor, boxColor);
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