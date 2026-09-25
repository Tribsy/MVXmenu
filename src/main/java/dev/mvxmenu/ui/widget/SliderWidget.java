package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import dev.mvxmenu.theme.FontRenderer;
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

    // Animation progress
    private float hoverProgress = 0f;
    private float focusProgress = 0f;
    private float fillProgress = 0f;

    public SliderWidget(String id, String label, int min, int max, int initial) {
        this.id = id;
        this.label = label;
        this.min = min;
        this.max = max;
        this.value = clamp(initial, min, max);
        this.fillProgress = (float)(value - min) / (float)(max - min);
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
        if (!focused) return false;
        boolean changed = false;
        if (keyCode == 263) { // Left arrow
            value = clamp(value - (max - min) / 100, min, max);
            changed = true;
        } else if (keyCode == 262) { // Right arrow
            value = clamp(value + (max - min) / 100, min, max);
            changed = true;
        } else if (keyCode == 265) { // Up arrow
            value = clamp(value + (max - min) / 20, min, max);
            changed = true;
        } else if (keyCode == 264) { // Down arrow
            value = clamp(value - (max - min) / 20, min, max);
            changed = true;
        }
        return changed;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (bounds == null) return;

        boolean hovered = isHovered(mouseX, mouseY) || dragging;
        hoverProgress = lerp(hoverProgress, hovered ? 1f : 0f, delta * 10f);
        focusProgress = lerp(focusProgress, focused ? 1f : 0f, delta * 10f);

        // Animate fill
        float targetFill = (float)(value - min) / (float)(max - min);
        fillProgress = lerp(fillProgress, targetFill, delta * 15f);

        int trackHeight = 6;
        int trackY = bounds.y + (bounds.height - trackHeight) / 2;
        int trackWidth = bounds.width;
        int thumbSize = 16;
        int thumbY = trackY + (trackHeight - thumbSize) / 2;

        // Track background
        RoundedRectRenderer.render(context, bounds.x, trackY, trackWidth, trackHeight, MvxmenuTheme.R_INPUT, MvxmenuTheme.BD_1);

        // Fill (purple)
        int fillWidth = (int) (trackWidth * fillProgress);
        if (fillWidth > 0) {
            RoundedRectRenderer.render(context, bounds.x, trackY, fillWidth, trackHeight, MvxmenuTheme.R_INPUT, MvxmenuTheme.AC);
        }

        // Thumb
        int thumbX = bounds.x + fillWidth - thumbSize / 2;
        thumbX = Math.max(bounds.x - thumbSize / 2, Math.min(bounds.x + trackWidth - thumbSize / 2, thumbX));

        // Thumb ring
        RoundedRectRenderer.render(context, thumbX, thumbY, thumbSize, thumbSize, MvxmenuTheme.R_BADGE, MvxmenuTheme.TX_0);
        // Inner accent dot
        RoundedRectRenderer.render(context, thumbX + 4, thumbY + 4, 8, 8, MvxmenuTheme.R_BADGE, MvxmenuTheme.AC);

        // Tooltip value above thumb when hovering
        if (hovered || dragging) {
            String valueText = label + ": " + value + (id.contains("opacity") || id.contains("speed") || id.contains("rounding") ? "%" : "");
            net.minecraft.client.font.TextRenderer tr = net.minecraft.client.MinecraftClient.getInstance().textRenderer;
            if (tr != null) {
                int textWidth = tr.getWidth(valueText);
                int tooltipX = thumbX + thumbSize / 2 - textWidth / 2;
                int tooltipY = thumbY - 18;
                // Background
                context.fill(tooltipX - 4, tooltipY - 2, tooltipX + textWidth + 4, tooltipY + 12, 0xDD000000);
                FontRenderer.drawTextSimple(context, valueText, tooltipX, tooltipY, MvxmenuTheme.TX_0, true, "ui");
            }
        }

        // Focus ring
        if (focused) {
            int ringAlpha = (int) (255 * focusProgress);
            int ringColor = (MvxmenuTheme.AC & 0x00FFFFFF) | (ringAlpha << 24);
            RoundedRectRenderer.renderBorder(context,
                    bounds.x - 2, trackY - 2,
                    trackWidth + 4, trackHeight + 4,
                    MvxmenuTheme.R_INPUT + 2, 2, ringColor, MvxmenuTheme.BD_1);
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return label + ": " + value;
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
        int relX = mouseX - bounds.x;
        float pct = Math.max(0.0f, Math.min(1.0f, (float) relX / (float) bounds.width));
        value = clamp(Math.round(min + (max - min) * pct), min, max);
    }

    private int clamp(int v, int lo, int hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * Math.min(1f, t);
    }

    private int lerpColor(int from, int to, float t) {
        t = Math.min(1f, Math.max(0f, t));
        int r = (int) ((((from >> 16) & 0xFF) * (1 - t)) + (((to >> 16) & 0xFF) * t));
        int g = (int) ((((from >> 8) & 0xFF) * (1 - t)) + (((to >> 8) & 0xFF) * t));
        int b = (int) (((from & 0xFF) * (1 - t)) + ((to & 0xFF) * t));
        int a = (int) ((((from >> 24) & 0xFF) * (1 - t)) + (((to >> 24) & 0xFF) * t));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}