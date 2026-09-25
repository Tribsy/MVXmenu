package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuIcons;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import dev.mvxmenu.theme.FontRenderer;
import dev.mvxmenu.util.MathUtil;
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

    // Animation progress
    private float hoverProgress = 0f;
    private float focusProgress = 0f;

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

        // Animate
        hoverProgress = MathUtil.lerp(hoverProgress, (hovered || active) ? 1f : 0f, delta * 10f);
        focusProgress = MathUtil.lerp(focusProgress, focused ? 1f : 0f, delta * 10f);

        int bgColor;
        if (active) {
            bgColor = MathUtil.lerpColor(MvxmenuTheme.BG_1, MvxmenuTheme.AC_DIM, hoverProgress);
        } else if (hovered || focused) {
            bgColor = MathUtil.lerpColor(MvxmenuTheme.BG_1, MvxmenuTheme.BG_2, hoverProgress);
        } else {
            bgColor = MvxmenuTheme.BG_1;
        }

        RoundedRectRenderer.render(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_BUTTON, bgColor);

        if (active) {
            context.fill(bounds.x + 2, bounds.y + 4, bounds.x + 4, bounds.y + bounds.height - 4, MvxmenuTheme.AC);
        }

        // Icon (SVG)
        int iconX = bounds.x + 8;
        int iconY = bounds.y + (bounds.height - 24) / 2;
        int iconColor = getIconColor();
        icon.render(context, iconX, iconY, 24, iconColor);

        // Separator line
        int sepColor = getTextColor();
        context.fill(iconX + 28, bounds.y + 6, iconX + 30, bounds.y + bounds.height - 6, sepColor);

        // Category name
        FontRenderer.drawTextSimple(context, categoryName, iconX + 38, bounds.y + (bounds.height - 8) / 2, getTextColor(), true, "ui");

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

    private int getTextColor() {
        if (active) return MvxmenuTheme.TX_0;
        if (hovered || focused) return MvxmenuTheme.TX_1;
        return MvxmenuTheme.TX_2;
    }

    private int getIconColor() {
        if (active) return MvxmenuTheme.AC;
        if (hovered) return MvxmenuTheme.TX_1;
        return MvxmenuTheme.TX_2;
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

    public boolean isActiveState() {
        return active;
    }
}