package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
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
        return false;
    }

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
        int bgColor = focused ? MvxmenuTheme.BG_2 : hovered ? MvxmenuTheme.BG_1 : MvxmenuTheme.BG_0;
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, bgColor);
        context.fill(bounds.x, bounds.y + bounds.height - 2, bounds.x + bounds.width, bounds.y + bounds.height, focused ? MvxmenuTheme.AC : MvxmenuTheme.BD_1);
        
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        String displayText = text.isEmpty() && !focused ? placeholder : text;
        int textColor = text.isEmpty() && !focused ? MvxmenuTheme.TX_2 : MvxmenuTheme.TX_0;
        context.drawText(textRenderer, displayText, bounds.x + 6, bounds.y + (bounds.height - 8) / 2, textColor, true);
        
        if (focused && (System.currentTimeMillis() / 500) % 2 == 0) {
            int cursorX = bounds.x + 6 + getTextWidth(textRenderer, text.substring(0, cursorPosition));
            context.fill(cursorX, bounds.y + 4, cursorX + 1, bounds.y + bounds.height - 4, MvxmenuTheme.AC);
        }
        
        if (focused) {
            FocusRing.render(context, bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2, MvxmenuTheme.AC);
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
}