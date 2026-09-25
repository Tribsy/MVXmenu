package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import net.minecraft.client.gui.DrawContext;

public class ToggleWidget implements MvxmenuWidget, NarratableWidget {

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private boolean enabled;
    private boolean visible = true;
    private boolean hovered;
    private boolean focused;

    // Animation progress
    private float hoverProgress = 0f;
    private float focusProgress = 0f;
    private float thumbProgress = 0f; // 0 = left (off), 1 = right (on)

    public ToggleWidget(String id, boolean initial) {
        this.id = id;
        this.enabled = initial;
        this.thumbProgress = initial ? 1f : 0f;
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
        this.thumbProgress = enabled ? 1f : 0f;
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
        if ((keyCode == 257 || keyCode == 32) && focused) { // Enter or Space
            enabled = !enabled;
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        hovered = isHovered(mouseX, mouseY);
        if (bounds == null) return;

        // Animate thumb position
        float targetThumb = enabled ? 1f : 0f;
        thumbProgress = lerp(thumbProgress, targetThumb, delta * 15f); // Fast animation
        hoverProgress = lerp(hoverProgress, hovered ? 1f : 0f, delta * 10f);
        focusProgress = lerp(focusProgress, focused ? 1f : 0f, delta * 10f);

        int trackHeight = 20;
        int trackY = bounds.y + (bounds.height - trackHeight) / 2;
        int trackWidth = bounds.width;
        int thumbSize = 16;
        int thumbY = trackY + (trackHeight - thumbSize) / 2;

        // Track color - purple when enabled, neutral when disabled
        int trackColor;
        if (enabled) {
            trackColor = MvxmenuTheme.AC;
        } else {
            trackColor = lerpColor(MvxmenuTheme.BD_2, MvxmenuTheme.BD_1, hoverProgress);
        }

        // Track background (rounded)
        RoundedRectRenderer.render(context, bounds.x, trackY, trackWidth, trackHeight, MvxmenuTheme.R_BADGE, trackColor);

        // Thumb position
        int thumbX = bounds.x + 2 + (int) ((trackWidth - thumbSize - 4) * thumbProgress);

        // Thumb (white with subtle shadow)
        RoundedRectRenderer.render(context, thumbX, thumbY, thumbSize, thumbSize, MvxmenuTheme.R_BADGE, MvxmenuTheme.TX_0);

        // Focus ring
        if (focused) {
            int ringAlpha = (int) (255 * focusProgress);
            int ringColor = (MvxmenuTheme.AC & 0x00FFFFFF) | (ringAlpha << 24);
            RoundedRectRenderer.renderBorder(context,
                    bounds.x - 2, trackY - 2,
                    trackWidth + 4, trackHeight + 4,
                    MvxmenuTheme.R_BADGE + 2, 2, ringColor, trackColor);
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

    public boolean getEnabled() {
        return enabled;
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