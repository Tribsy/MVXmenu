package dev.mvxmenu.ui.widget;

import dev.mvxmenu.networking.MvxmenuNetworking;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
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
        listening = true;
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!listening) return false;
        if (keyCode == 1) {
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
        int bgColor = listening ? MvxmenuTheme.AC_DIM : (hovered || focused) ? MvxmenuTheme.BG_2 : MvxmenuTheme.BG_1;
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, bgColor);
        context.fill(bounds.x, bounds.y, bounds.x + 2, bounds.y + bounds.height, MvxmenuTheme.AC);
        if (focused) {
            FocusRing.render(context, bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2, MvxmenuTheme.AC);
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
        return "KEY:" + keyCode;
    }
}