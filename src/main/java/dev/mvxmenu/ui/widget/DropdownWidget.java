package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

public class DropdownWidget implements MvxmenuWidget {

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private String label;
    private String value;
    private String[] options;
    private boolean open;
    private int selectedIndex;
    private int scrollOffset;
    private static final int VISIBLE_OPTIONS = 6;
    private static final int OPTION_HEIGHT = 20;
    private boolean visible = true;
    private boolean hovered;
    private boolean focused;

    public DropdownWidget(String id, String label, String[] options, String initial) {
        this.id = id;
        this.label = label;
        this.options = options;
        this.value = initial;
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(initial)) {
                this.selectedIndex = i;
                break;
            }
        }
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
        if (bounds != null && bounds.contains(mouseX, mouseY)) return true;
        if (open) {
            int listY = bounds.y + bounds.height;
            int listHeight = Math.min(options.length, VISIBLE_OPTIONS) * OPTION_HEIGHT;
            return mouseY >= listY && mouseY < listY + listHeight;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (open) {
            int listY = bounds.y + bounds.height;
            int optionIndex = (int)((y - listY) / OPTION_HEIGHT);
            if (optionIndex >= 0 && optionIndex < options.length) {
                selectedIndex = optionIndex;
                value = options[optionIndex];
                open = false;
                return true;
            }
            open = false;
            return true;
        }
        if (bounds == null || !bounds.contains((int) x, (int) y)) return false;
        open = !open;
        scrollOffset = 0;
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!open) {
            if (keyCode == 257) {
                open = true;
                scrollOffset = 0;
                return true;
            }
            return false;
        }
        if (keyCode == 257) {
            open = false;
            return true;
        }
        if (keyCode == 264) {
            selectedIndex = Math.min(options.length - 1, selectedIndex + 1);
            value = options[selectedIndex];
            if (selectedIndex >= scrollOffset + VISIBLE_OPTIONS) scrollOffset++;
            return true;
        }
        if (keyCode == 263) {
            selectedIndex = Math.max(0, selectedIndex - 1);
            value = options[selectedIndex];
            if (selectedIndex < scrollOffset) scrollOffset--;
            return true;
        }
        if (keyCode == 256) {
            open = false;
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        hovered = isHovered(mouseX, mouseY);
        if (bounds == null) return;
        int bgColor = open ? MvxmenuTheme.AC_DIM : (hovered || focused) ? MvxmenuTheme.BG_2 : MvxmenuTheme.BG_1;
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, bgColor);
        context.fill(bounds.x, bounds.y + bounds.height - 2, bounds.x + bounds.width, bounds.y + bounds.height, MvxmenuTheme.BD_1);
        context.drawText(MinecraftClient.getInstance().textRenderer, value, bounds.x + 4, bounds.y + (bounds.height - 8) / 2, MvxmenuTheme.TX_0, false);
        if (focused) {
            FocusRing.render(context, bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2, MvxmenuTheme.AC);
        }

        if (open) {
            int listX = bounds.x;
            int listY = bounds.y + bounds.height;
            int listWidth = bounds.width;
            int visibleCount = Math.min(options.length, VISIBLE_OPTIONS);
            int listHeight = visibleCount * OPTION_HEIGHT;

            context.fill(listX, listY, listX + listWidth, listY + listHeight, MvxmenuTheme.BG_1);
            context.fill(listX, listY, listX + listWidth, listY + 1, MvxmenuTheme.BD_1);
            context.fill(listX, listY + listHeight - 1, listX + listWidth, listY + listHeight, MvxmenuTheme.BD_1);

            for (int i = 0; i < visibleCount; i++) {
                int optionIndex = i + scrollOffset;
                if (optionIndex >= options.length) break;
                int oy = listY + i * OPTION_HEIGHT;

                if (optionIndex == selectedIndex) {
                    context.fill(listX, oy, listX + listWidth, oy + OPTION_HEIGHT, MvxmenuTheme.AC_DIM);
                }

                if (optionIndex == selectedIndex) {
                    context.fill(listX, oy, listX + 2, oy + OPTION_HEIGHT, MvxmenuTheme.AC);
                }

                context.drawText(MinecraftClient.getInstance().textRenderer, options[optionIndex], listX + 4, oy + (OPTION_HEIGHT - 8) / 2,
                        optionIndex == selectedIndex ? MvxmenuTheme.TX_0 : MvxmenuTheme.TX_1, false);
            }
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return value;
    }

    @Override
    public Type getType() {
        return Type.DROPDOWN;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
        if (!focused) open = false;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(value)) {
                selectedIndex = i;
                break;
            }
        }
    }

    public String[] getOptions() {
        return options;
    }

    public String getLabel() {
        return label;
    }

    public boolean isOpen() {
        return open;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}