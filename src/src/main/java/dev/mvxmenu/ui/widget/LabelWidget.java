package dev.mvxmenu.ui.widget;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.gui.DrawContext;

public class LabelWidget implements MvxmenuWidget {

    private String id;
    private MvxmenuLayout.Bounds bounds;
    private String text;
    private int color;
    private boolean visible = true;

    public LabelWidget(String id, String text, int color) {
        this.id = id;
        this.text = text;
        this.color = color;
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
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, MvxmenuTheme.BD_0);
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return text;
    }

    @Override
    public Type getType() {
        return Type.LABEL;
    }

    public String getText() {
        return text;
    }

    public int getColor() {
        return color;
    }
}
