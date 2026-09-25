package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import dev.mvxmenu.theme.FontRenderer;
import net.minecraft.client.gui.DrawContext;

public class StatusBadgeWidget implements MvxmenuWidget {

    public enum Status {
        ACTIVE, DISABLED, ERROR, WARNING, INFO, LOCKED
    }

    private String id;
    private Status status;
    private String customText;
    private MvxmenuLayout.Bounds bounds;
    private boolean visible = true;

    public StatusBadgeWidget(String id, Status status) {
        this.id = id;
        this.status = status;
    }

    public StatusBadgeWidget(String id, String customText, int color) {
        this.id = id;
        this.customText = customText;
        this.status = null;
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
        return false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (bounds == null || !visible) return;

        int bgColor = getBackgroundColor();
        int textColor = getTextColor();
        String text = customText != null ? customText : status.name();

        net.minecraft.client.font.TextRenderer tr = net.minecraft.client.MinecraftClient.getInstance().textRenderer;
        if (tr == null) return;

        int padding = 6;
        int textWidth = tr.getWidth(text);
        int badgeWidth = textWidth + padding * 2;
        int badgeHeight = 16;

        // Center in bounds
        int x = bounds.x + (bounds.width - badgeWidth) / 2;
        int y = bounds.y + (bounds.height - badgeHeight) / 2;

        // Pill background
        RoundedRectRenderer.render(context, x, y, badgeWidth, badgeHeight, MvxmenuTheme.R_BADGE, bgColor);

        // Text
        FontRenderer.drawTextSimple(context, text, x + padding, y + 2, textColor, true, "ui");
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return customText != null ? customText : status.name();
    }

    @Override
    public Type getType() {
        return Type.LABEL;
    }

    public Status getStatus() {
        return status;
    }

    public int getTextColor() {
        if (customText != null) return MvxmenuTheme.TX_0;
        return switch (status) {
            case ACTIVE -> MvxmenuTheme.SUCCESS;
            case DISABLED -> MvxmenuTheme.TX_2;
            case ERROR -> MvxmenuTheme.DANGER;
            case WARNING -> MvxmenuTheme.WARNING;
            case INFO -> MvxmenuTheme.INFO;
            case LOCKED -> MvxmenuTheme.PURPLE;
        };
    }

    public int getBackgroundColor() {
        if (customText != null) return MvxmenuTheme.AC_DIM;
        return switch (status) {
            case ACTIVE -> MvxmenuTheme.SUCCESS_BG;
            case DISABLED -> MvxmenuTheme.BG_1;
            case ERROR -> MvxmenuTheme.DANGER_BG;
            case WARNING -> MvxmenuTheme.WARNING_BG;
            case INFO -> MvxmenuTheme.INFO_BG;
            case LOCKED -> MvxmenuTheme.PURPLE_BG;
        };
    }
}