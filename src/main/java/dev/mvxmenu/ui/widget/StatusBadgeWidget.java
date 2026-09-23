package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.gui.DrawContext;

public class StatusBadgeWidget implements MvxmenuWidget {

    public enum Status {
        ACTIVE, DISABLED, ERROR, WARNING, INFO, LOCKED
    }

    private String id;
    private Status status;
    private MvxmenuLayout.Bounds bounds;
    private boolean visible = true;

    public StatusBadgeWidget(String id, Status status) {
        this.id = id;
        this.status = status;
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
        if (bounds == null) return;
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, getBackgroundColor());
        context.fill(bounds.x, bounds.y + bounds.height - 2, bounds.x + bounds.width, bounds.y + bounds.height, getTextColor());
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return status.name();
    }

    @Override
    public Type getType() {
        return Type.LABEL;
    }

    public Status getStatus() {
        return status;
    }

    public int getTextColor() {
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
        return switch (status) {
            case ACTIVE -> MvxmenuTheme.SUCCESS_BG;
            case DISABLED -> MvxmenuTheme.BG_1;
            case ERROR -> MvxmenuTheme.DANGER_BG;
            case WARNING -> MvxmenuTheme.WARNING_BG;
            case INFO -> MvxmenuTheme.INFO_BG;
            case LOCKED -> 0x12A78BFA;
        };
    }
}
