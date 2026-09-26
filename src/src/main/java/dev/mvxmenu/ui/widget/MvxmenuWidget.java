package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import net.minecraft.client.gui.DrawContext;

public interface MvxmenuWidget {

    String getId();

    MvxmenuLayout.Bounds getBounds();

    void setBounds(MvxmenuLayout.Bounds bounds);

    boolean isEnabled();

    void setEnabled(boolean enabled);

    boolean isVisible();

    void setVisible(boolean visible);

    boolean isHovered(int mouseX, int mouseY);

    boolean mouseClicked(double x, double y, int button);

    boolean keyPressed(int keyCode, int scanCode, int modifiers);

    default boolean charTyped(char codePoint, int modifiers) {
        return false;
    }

    default boolean mouseReleased(double x, double y, int button) {
        return false;
    }

    void render(DrawContext context, int mouseX, int mouseY, float delta);

    String getTooltipText(int mouseX, int mouseY);

    default boolean isFocused() {
        return false;
    }

    default void setFocused(boolean focused) {
    }

    enum Type {
        BUTTON,
        TOGGLE,
        SLIDER,
        DROPDOWN,
        CHECKBOX,
        KEYBIND,
        MODULE_CARD,
        ICON_BUTTON,
        LABEL,
        SCREEN
    }

    Type getType();
}