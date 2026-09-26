package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import dev.mvxmenu.theme.FontRenderer;
import dev.mvxmenu.util.MathUtil;
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
    private static final int OPTION_HEIGHT = 24;
    private boolean visible = true;
    private boolean hovered;
    private boolean focused;

    // Animation
    private float hoverProgress = 0f;
    private float focusProgress = 0f;
    private float openProgress = 0f;

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
        if (bounds != null && bounds.contains(mouseX, mouseY)) return true;
        if (open) {
            int listY = bounds.y + bounds.height;
            int listHeight = Math.min(options.length, VISIBLE_OPTIONS) * OPTION_HEIGHT;
            return mouseX >= bounds.x && mouseX < bounds.x + bounds.width
                    && mouseY >= listY && mouseY < listY + listHeight;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (open) {
            int listY = bounds.y + bounds.height;
            int optionIndex = (int) ((y - listY) / OPTION_HEIGHT);
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
            if (keyCode == 257) { // Enter
                open = true;
                scrollOffset = 0;
                return true;
            }
            return false;
        }
        if (keyCode == 257) { // Enter - select
            open = false;
            return true;
        }
        if (keyCode == 264) { // Down
            selectedIndex = Math.min(options.length - 1, selectedIndex + 1);
            value = options[selectedIndex];
            if (selectedIndex >= scrollOffset + VISIBLE_OPTIONS) scrollOffset++;
            return true;
        }
        if (keyCode == 263) { // Up
            selectedIndex = Math.max(0, selectedIndex - 1);
            value = options[selectedIndex];
            if (selectedIndex < scrollOffset) scrollOffset--;
            return true;
        }
        if (keyCode == 256) { // ESC
            open = false;
            return true;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        hovered = isHovered(mouseX, mouseY);
        if (bounds == null) return;

        hoverProgress = MathUtil.lerp(hoverProgress, (hovered || open) ? 1f : 0f, delta * 10f);
        focusProgress = MathUtil.lerp(focusProgress, focused ? 1f : 0f, delta * 10f);
        openProgress = MathUtil.lerp(openProgress, open ? 1f : 0f, delta * 15f);

        // Main button
        int bgColor = MathUtil.lerpColor(MvxmenuTheme.BG_1, MvxmenuTheme.BG_2, hoverProgress);
        if (open) bgColor = MvxmenuTheme.AC_DIM;

        RoundedRectRenderer.render(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_INPUT, bgColor);

        // Border
        int borderColor = MathUtil.lerpColor(MvxmenuTheme.BD_1, MvxmenuTheme.AC, Math.max(hoverProgress, openProgress));
        RoundedRectRenderer.renderBorder(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_INPUT, 1, borderColor, bgColor);

        // Value text
        FontRenderer.drawTextSimple(context, value, bounds.x + 8, bounds.y + (bounds.height - 10) / 2, MvxmenuTheme.TX_0, true, "ui");

        // Dropdown arrow
        int arrowX = bounds.x + bounds.width - 16;
        int arrowY = bounds.y + (bounds.height - 8) / 2;
        int arrowColor = MvxmenuTheme.TX_1;
        context.fill(arrowX, arrowY, arrowX + 8, arrowY + 1, arrowColor);
        context.fill(arrowX + 1, arrowY + 1, arrowX + 7, arrowY + 2, arrowColor);
        context.fill(arrowX + 2, arrowY + 2, arrowX + 6, arrowY + 3, arrowColor);
        context.fill(arrowX + 3, arrowY + 3, arrowX + 5, arrowY + 4, arrowColor);

        // Focus ring
        if (focused) {
            int ringAlpha = (int) (255 * focusProgress);
            int ringColor = (MvxmenuTheme.AC & 0x00FFFFFF) | (ringAlpha << 24);
            RoundedRectRenderer.renderBorder(context,
                    bounds.x - 2, bounds.y - 2,
                    bounds.width + 4, bounds.height + 4,
                    MvxmenuTheme.R_INPUT + 2, 2, ringColor, bgColor);
        }

        // Options dropdown
        if (open && openProgress > 0.01f) {
            int listX = bounds.x;
            int listY = bounds.y + bounds.height + 2;
            int listWidth = bounds.width;
            int visibleCount = Math.min(options.length, VISIBLE_OPTIONS);
            int listHeight = visibleCount * OPTION_HEIGHT;

            // Dropdown background
            RoundedRectRenderer.render(context, listX, listY, listWidth, listHeight,
                    MvxmenuTheme.R_INPUT, MvxmenuTheme.BG_1);

            // Border
            RoundedRectRenderer.renderBorder(context, listX, listY, listWidth, listHeight,
                    MvxmenuTheme.R_INPUT, 1, MvxmenuTheme.BD_1, MvxmenuTheme.BG_1);

            for (int i = 0; i < visibleCount; i++) {
                int optionIndex = i + scrollOffset;
                if (optionIndex >= options.length) break;
                int oy = listY + i * OPTION_HEIGHT;

                boolean isSelected = optionIndex == selectedIndex;
                boolean isHovered = mouseX >= listX && mouseX < listX + listWidth
                        && mouseY >= oy && mouseY < oy + OPTION_HEIGHT;

                if (isSelected || isHovered) {
                    int optBgColor = isSelected ? MvxmenuTheme.AC_DIM : MvxmenuTheme.BG_2;
                    context.fill(listX + 1, oy, listX + listWidth - 1, oy + OPTION_HEIGHT, optBgColor);
                }

                if (isSelected) {
                    context.fill(listX + 1, oy, listX + 3, oy + OPTION_HEIGHT, MvxmenuTheme.AC);
                }

                int textColor = isSelected ? MvxmenuTheme.TX_0 : MvxmenuTheme.TX_1;
                FontRenderer.drawTextSimple(context, options[optionIndex], listX + 8, oy + (OPTION_HEIGHT - 10) / 2, textColor, true, "ui");
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