package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import dev.mvxmenu.util.MathUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public class ButtonWidget implements MvxmenuWidget, NarratableWidget {

    public enum Variant {
        DEFAULT, PRIMARY, GHOST, DANGER, ACCENT
    }

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private Variant variant;
    private String text;
    private boolean enabled = true;
    private boolean active;
    private boolean visible = true;
    private boolean hovered;
    private boolean focused;

    // Animation progress
    private float hoverProgress = 0f;
    private float focusProgress = 0f;

    public ButtonWidget(String id, Variant variant, String text) {
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
        if ((keyCode == 257 || keyCode == 32) && focused && enabled) { // Enter or Space
            active = true;
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        hovered = isHovered(mouseX, mouseY) && enabled;
        if (bounds == null) return;

        // Animate progress
        hoverProgress = MathUtil.lerp(hoverProgress, hovered ? 1f : 0f, delta * 10f);
        focusProgress = MathUtil.lerp(focusProgress, focused ? 1f : 0f, delta * 10f);

        int bgColor, borderColor, textColor;

        switch (variant) {
            case PRIMARY:   // Purple accent
                bgColor = MathUtil.lerpColor(MvxmenuTheme.BG_1, MvxmenuTheme.AC, hoverProgress);
                borderColor = MvxmenuTheme.AC;
                textColor = MvxmenuTheme.AC_FG;
                break;
            case GHOST:
                bgColor = MathUtil.lerpColor(MvxmenuTheme.BG_0, MvxmenuTheme.BG_2, hoverProgress);
                borderColor = MvxmenuTheme.TX_2;
                textColor = MvxmenuTheme.TX_0;
                break;
            case DANGER:
                bgColor = MathUtil.lerpColor(MvxmenuTheme.BG_1, MvxmenuTheme.DANGER, hoverProgress);
                borderColor = MvxmenuTheme.DANGER;
                textColor = MvxmenuTheme.TX_0;
                break;
            case ACCENT:
                bgColor = MathUtil.lerpColor(MvxmenuTheme.BG_1, MvxmenuTheme.PURPLE, hoverProgress);
                borderColor = MvxmenuTheme.PURPLE;
                textColor = MvxmenuTheme.TX_0;
                break;
            default: // DEFAULT
                bgColor = MathUtil.lerpColor(MvxmenuTheme.BG_1, MvxmenuTheme.BG_2, hoverProgress);
                borderColor = MvxmenuTheme.BD_2;
                textColor = MvxmenuTheme.TX_0;
                break;
        }

        // Rounded background (rx=8)
        RoundedRectRenderer.render(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_BUTTON, bgColor);

        // Border
        if (variant != Variant.DEFAULT) {
            RoundedRectRenderer.renderBorder(context, bounds.x, bounds.y, bounds.width, bounds.height,
                    MvxmenuTheme.R_BUTTON, 1, borderColor, bgColor);
        } else {
            // Top accent bar for default
            context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + 1, borderColor);
        }

        // Text
        int textX = bounds.centerX() - getTextWidth(text) / 2;
        int textY = bounds.y + (bounds.height - 10) / 2;
        dev.mvxmenu.theme.FontRenderer.drawTextSimple(context, text, textX, textY, textColor, true, "ui");

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

    private int getTextWidth(String text) {
        net.minecraft.client.font.TextRenderer tr = net.minecraft.client.MinecraftClient.getInstance().textRenderer;
        return tr != null ? tr.getWidth(text) : text.length() * 6;
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

    public Variant getVariant() {
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