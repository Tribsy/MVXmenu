package dev.mvxmenu.ui.screen;

import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.widget.MvxmenuWidget;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Content panel widget - rounded container (rx=12) for content area.
 * Handles module grid, detail view, or settings panel.
 */
public class ContentPanelWidget implements MvxmenuWidget {

    private final String id = "content_panel";
    private MvxmenuLayout.Bounds bounds;
    private final MvxmenuLayout layout;
    private final List<MvxmenuWidget> children = new ArrayList<>();
    private boolean visible = true;

    public ContentPanelWidget(MvxmenuLayout layout) {
        this.layout = layout;
    }

    public void addChild(MvxmenuWidget widget) {
        children.add(widget);
        // Set bounds on the new child to fill the content panel
        if (bounds != null) {
            widget.setBounds(new MvxmenuLayout.Bounds(
                bounds.x,
                bounds.y,
                bounds.width,
                bounds.height
            ));
        }
    }

    public void removeChild(MvxmenuWidget widget) {
        children.remove(widget);
    }

    public void clearChildren() {
        children.clear();
    }

    public List<MvxmenuWidget> getChildren() {
        return children;
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
        // Propagate bounds to children (they should fill the content panel)
        for (MvxmenuWidget child : children) {
            child.setBounds(new MvxmenuLayout.Bounds(
                bounds.x,
                bounds.y,
                bounds.width,
                bounds.height
            ));
        }
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
        if (bounds == null || !bounds.contains((int)x, (int)y)) return false;

        // Dispatch to children in reverse order (top-most first)
        for (int i = children.size() - 1; i >= 0; i--) {
            if (children.get(i).mouseClicked(x, y, button)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (MvxmenuWidget child : children) {
            if (child.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        for (MvxmenuWidget child : children) {
            if (child.charTyped(codePoint, modifiers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (bounds == null || !visible) return;

        // Panel background with rounded corners (rx=12)
        RoundedRectRenderer.render(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_PANEL, MvxmenuTheme.BG_0);

        // Top accent highlight (SHADOW_PANEL)
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + 1, MvxmenuTheme.SHADOW_PANEL);

        // Subtle border
        RoundedRectRenderer.renderBorder(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_PANEL, 1, MvxmenuTheme.BD_1, MvxmenuTheme.BG_0);

        // Render children
        for (MvxmenuWidget child : children) {
            if (child.isVisible()) {
                child.render(context, mouseX, mouseY, delta);
            }
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return "Content Panel";
    }

    @Override
    public Type getType() {
        return Type.SCREEN;
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public void setFocused(boolean focused) {}
}