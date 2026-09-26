package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuIcons;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.gui.DrawContext;

public class CategoryButtonWidget implements MvxmenuWidget, NarratableWidget {

    private String id;
    private String categoryName;
    private MvxmenuIcons icon;
    private boolean active;
    private MvxmenuLayout.Bounds bounds;
    private boolean hovered;
    private boolean visible = true;
    private boolean focused;

    public CategoryButtonWidget(String id, String categoryName, MvxmenuIcons icon) {
        this.id = id;
        this.categoryName = categoryName;
        this.icon = icon;
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
        active = true;
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        hovered = isHovered(mouseX, mouseY);
        if (bounds == null) return;
        int bgColor = active ? MvxmenuTheme.AC_DIM : (hovered || focused) ? MvxmenuTheme.BG_2 : MvxmenuTheme.BG_1;
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, bgColor);
        if (hovered || active) {
            context.fill(bounds.x + 2, bounds.y + 2, bounds.x + bounds.width - 2, bounds.y + 4, active ? MvxmenuTheme.AC : MvxmenuTheme.AC_DIM);
        }
        context.fill(bounds.x, bounds.y + bounds.height - 2, bounds.x + bounds.width, bounds.y + bounds.height, active ? MvxmenuTheme.AC : MvxmenuTheme.BD_1);
        if (active) {
            context.fill(bounds.x + 2, bounds.y + 2, bounds.x + 4, bounds.y + bounds.height - 2, MvxmenuTheme.AC);
        }
        int iconX = bounds.x + 8;
        int iconY = bounds.y + (bounds.height - 24) / 2;
        icon.render(context, iconX, iconY, 24, getIconColor());
        context.fill(iconX + 28, bounds.y + 6, iconX + 30, bounds.y + bounds.height - 6, getTextColor());

        net.minecraft.client.font.TextRenderer textRenderer = net.minecraft.client.MinecraftClient.getInstance().textRenderer;
        if (textRenderer != null) {
            context.drawText(textRenderer, categoryName, iconX + 38, bounds.y + (bounds.height - 8) / 2, getTextColor(), true);
        }

        if (focused) {
            FocusRing.render(context, bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2, MvxmenuTheme.AC);
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return categoryName;
    }

    @Override
    public Type getType() {
        return Type.ICON_BUTTON;
    }

    @Override
    public String getNarrationText() {
        return "Category " + categoryName + (active ? " selected" : "");
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

    public String getCategoryName() {
        return categoryName;
    }

    public MvxmenuIcons getIcon() {
        return icon;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getTextColor() {
        if (active) return MvxmenuTheme.TX_0;
        if (hovered || focused) return MvxmenuTheme.TX_1;
        return MvxmenuTheme.TX_2;
    }

    public int getIconColor() {
        if (active) return MvxmenuTheme.AC;
        return MvxmenuTheme.TX_2;
    }

    public boolean isActiveState() {
        return active;
    }
}