package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import dev.mvxmenu.theme.FontRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.font.TextRenderer;

public class TextFieldWidget implements MvxmenuWidget, NarratableWidget {

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private String text;
    private String placeholder;
    private boolean focused;
    private boolean visible = true;
    private boolean hovered;
    private int cursorPosition;
    private int maxLength;

    // Animation
    private float hoverProgress = 0f;
    private float focusProgress = 0f;
    private long cursorBlinkTime = 0;

    public TextFieldWidget(String id, String placeholder, int maxLength) {
        this.id = id;
        this.placeholder = placeholder;
        this.maxLength = maxLength;
        this.text = "";
        this.cursorPosition = 0;
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
        focused = true;
        cursorPosition = text.length();
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) return false;
        if (keyCode == 259 && cursorPosition > 0) { // Backspace
            text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
            cursorPosition--;
            return true;
        }
        if (keyCode == 257) { // Enter
            focused = false;
            return true;
        }
        if (keyCode == 256) { // Escape
            focused = false;
            return true;
        }
        if (keyCode == 263 && cursorPosition > 0) { // Left arrow
            cursorPosition--;
            return true;
        }
        if (keyCode == 262 && cursorPosition < text.length()) { // Right arrow
            cursorPosition++;
            return true;
        }
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (!focused) return false;
        if (text.length() >= maxLength) return false;
        if (codePoint >= 32 && codePoint <= 126) {
            text = text.substring(0, cursorPosition) + codePoint + text.substring(cursorPosition);
            cursorPosition++;
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        hovered = isHovered(mouseX, mouseY);
        if (bounds == null) return;

        hoverProgress = lerp(hoverProgress, hovered ? 1f : 0f, delta * 10f);
        focusProgress = lerp(focusProgress, focused ? 1f : 0f, delta * 10f);

        // Background
        int bgColor = lerpColor(MvxmenuTheme.BG_0, MvxmenuTheme.BG_2, Math.max(hoverProgress, focusProgress * 0.5f));
        if (focused) bgColor = MvxmenuTheme.BG_2;

        RoundedRectRenderer.render(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_INPUT, bgColor);

        // Bottom border (accent when focused)
        int borderColor = lerpColor(MvxmenuTheme.BD_1, MvxmenuTheme.AC, focusProgress);
        context.fill(bounds.x, bounds.y + bounds.height - 2, bounds.x + bounds.width, bounds.y + bounds.height, borderColor);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (textRenderer != null) {
            String displayText = text.isEmpty() && !focused ? placeholder : text;
            int textColor = text.isEmpty() && !focused ? MvxmenuTheme.TX_2 : MvxmenuTheme.TX_0;
            FontRenderer.drawTextSimple(context, displayText, bounds.x + 8, bounds.y + (bounds.height - 10) / 2, textColor, true, "ui");

            // Cursor blink
            if (focused) {
                cursorBlinkTime += (long)(delta * 1000);
                if ((cursorBlinkTime / 500) % 2 == 0) {
                    int cursorX = bounds.x + 8 + getTextWidth(textRenderer, text.substring(0, cursorPosition));
                    context.fill(cursorX, bounds.y + 4, cursorX + 1, bounds.y + bounds.height - 4, MvxmenuTheme.AC);
                }
            }
        }

        // Focus ring
        if (focused) {
            int ringAlpha = (int) (255 * focusProgress);
            int ringColor = (MvxmenuTheme.AC & 0x00FFFFFF) | (ringAlpha << 24);
            RoundedRectRenderer.renderBorder(context,
                    bounds.x - 2, bounds.y - 2,
                    bounds.width + 4, bounds.height + 4,
                    MvxmenuTheme.R_INPUT + 2, 2, ringColor, bgColor);
        }
    }

    private int getTextWidth(TextRenderer textRenderer, String text) {
        return textRenderer.getWidth(text);
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return placeholder;
    }

    @Override
    public Type getType() {
        return Type.LABEL;
    }

    @Override
    public String getNarrationText() {
        return "Search field " + (text.isEmpty() ? "empty" : text);
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.cursorPosition = text.length();
    }

    public String getPlaceholder() {
        return placeholder;
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