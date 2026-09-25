package dev.mvxmenu.ui.widget;

import dev.mvxmenu.networking.MvxmenuNetworking;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import dev.mvxmenu.theme.FontRenderer;
import dev.mvxmenu.util.MathUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class KeybindWidget implements MvxmenuWidget, NarratableWidget {

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private String label;
    private String value;
    private boolean listening;
    private boolean visible = true;
    private boolean hovered;
    private boolean focused;

    // Animation
    private float hoverProgress = 0f;
    private float focusProgress = 0f;
    private float listenProgress = 0f;

    public KeybindWidget(String id, String label, String initial) {
        this.id = id;
        this.label = label;
        this.value = initial;
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
        listening = true;
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!listening) return false;
        if (keyCode == 1) { // ESC = unbind
            listening = false;
            value = "UNBOUND";
            MvxmenuNetworking.sendKeybindUpdateToServer(id, value);
            return true;
        }
        value = formatKey(keyCode);
        listening = false;
        MvxmenuNetworking.sendKeybindUpdateToServer(id, value);
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        hovered = isHovered(mouseX, mouseY);
        if (bounds == null) return;

        hoverProgress = MathUtil.lerp(hoverProgress, hovered ? 1f : 0f, delta * 10f);
        focusProgress = MathUtil.lerp(focusProgress, focused ? 1f : 0f, delta * 10f);
        listenProgress = MathUtil.lerp(listenProgress, listening ? 1f : 0f, delta * 15f);

        int bgColor;
        int borderColor;

        if (listening) {
            bgColor = MvxmenuTheme.AC_DIM;
            borderColor = MvxmenuTheme.AC;
        } else if (hovered || focused) {
            bgColor = MathUtil.lerpColor(MvxmenuTheme.BG_1, MvxmenuTheme.BG_2, hoverProgress);
            borderColor = MvxmenuTheme.AC;
        } else {
            bgColor = MvxmenuTheme.BG_1;
            borderColor = MvxmenuTheme.BD_1;
        }

        RoundedRectRenderer.render(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_INPUT, bgColor);

        RoundedRectRenderer.renderBorder(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_INPUT, 1, borderColor, bgColor);

        // Value text
        String displayValue = listening ? "PRESS KEY..." : value;
        int textColor = listening ? MvxmenuTheme.AC : MvxmenuTheme.TX_0;
        FontRenderer.drawTextSimple(context, displayValue, bounds.x + 8, bounds.y + (bounds.height - 10) / 2, textColor, true, "ui");

        // Focus ring
        if (focused || listening) {
            int ringAlpha = (int) (255 * Math.max(focusProgress, listenProgress));
            int ringColor = (MvxmenuTheme.AC & 0x00FFFFFF) | (ringAlpha << 24);
            RoundedRectRenderer.renderBorder(context,
                    bounds.x - 2, bounds.y - 2,
                    bounds.width + 4, bounds.height + 4,
                    MvxmenuTheme.R_INPUT + 2, 2, ringColor, bgColor);
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return label;
    }

    @Override
    public Type getType() {
        return Type.KEYBIND;
    }

    @Override
    public String getNarrationText() {
        if (listening) return "Keybind " + label + ", press a key to bind";
        return "Keybind " + label + " set to " + value;
    }

    @Override
    public Priority getNarrationPriority() {
        return Priority.HIGH;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public boolean isListening() {
        return listening;
    }

    private String formatKey(int keyCode) {
        if (keyCode >= 290 && keyCode <= 301) { // F1-F12
            return "F" + (keyCode - 289);
        }
        if (keyCode >= 256 && keyCode <= 265) { // Arrow keys, etc
            return switch (keyCode) {
                case 256 -> "ESC";
                case 257 -> "ENTER";
                case 258 -> "TAB";
                case 259 -> "BACKSPACE";
                case 260 -> "INSERT";
                case 261 -> "DELETE";
                case 262 -> "RIGHT";
                case 263 -> "LEFT";
                case 264 -> "DOWN";
                case 265 -> "UP";
                case 266 -> "PAGE_UP";
                case 267 -> "PAGE_DOWN";
                case 268 -> "HOME";
                case 269 -> "END";
                default -> "KEY:" + keyCode;
            };
        }
        if (keyCode >= 32 && keyCode <= 126) {
            return String.valueOf((char) keyCode).toUpperCase();
        }
        return "KEY:" + keyCode;
    }
}