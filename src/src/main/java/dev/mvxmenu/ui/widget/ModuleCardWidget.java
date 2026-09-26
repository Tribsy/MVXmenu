package dev.mvxmenu.ui.widget;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.gui.DrawContext;

public class ModuleCardWidget implements MvxmenuWidget, NarratableWidget {

    private String id;
    private String name;
    private String description;
    private boolean enabled;
    private boolean disabled;
    private MvxmenuLayout.Bounds bounds;
    private boolean hovered;
    private boolean visible = true;
    private boolean focused;
    private Module module;

    public ModuleCardWidget(String id, String name, String desc, boolean enabled, boolean disabled) {
        this.id = id;
        this.name = name;
        this.description = desc;
        this.enabled = enabled;
        this.disabled = disabled;
    }

    public ModuleCardWidget(Module module) {
        this(module.getId().toString(), module.getDisplayName(), module.getDescription(), module.isEnabled(), false);
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
        this.id = module.getId().toString();
        this.name = module.getDisplayName();
        this.description = module.getDescription();
        this.enabled = module.isEnabled();
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
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
        if (disabled) return false;
        enabled = !enabled;
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
        
        // Use design tokens for backgrounds
        int bgColor = disabled ? MvxmenuTheme.BG_1 : (hovered || focused) ? MvxmenuTheme.BG_2 : MvxmenuTheme.BG_0;
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height, bgColor);
        
        if (hovered || focused || enabled) {
            int accent = enabled ? MvxmenuTheme.AC : MvxmenuTheme.AC_DIM;
            context.fill(bounds.x + 2, bounds.y + 2, bounds.x + bounds.width - 2, bounds.y + 4, accent);
            context.fill(bounds.x + 2, bounds.y + 2, bounds.x + 4, bounds.y + bounds.height - 2, accent);
        }
        
        context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + 2, getBorderColor());
        context.fill(bounds.x, bounds.y + bounds.height - 1, bounds.x + bounds.width, bounds.y + bounds.height, MvxmenuTheme.BD_1);
        context.fill(bounds.x + 8, bounds.y + 10, bounds.x + 18, bounds.y + 18, getStatusDotColor());
        context.fill(bounds.x + 8, bounds.y + 18, bounds.x + 8 + 34, bounds.y + 19, MvxmenuTheme.BD_2);

        net.minecraft.client.font.TextRenderer textRenderer = net.minecraft.client.MinecraftClient.getInstance().textRenderer;
        if (textRenderer != null) {
            context.drawText(textRenderer, name.toUpperCase(), bounds.x + 24, bounds.y + 8, MvxmenuTheme.TX_0, true);
            context.drawText(textRenderer, description, bounds.x + 8, bounds.y + 24, MvxmenuTheme.TX_1, true);
            context.drawText(textRenderer, enabled ? "ON" : "OFF", bounds.x + bounds.width - 22, bounds.y + 8, enabled ? MvxmenuTheme.SUCCESS : MvxmenuTheme.TX_2, true);
        }

        if (focused) {
            FocusRing.render(context, bounds.x - 1, bounds.y - 1, bounds.width + 2, bounds.height + 2, MvxmenuTheme.AC);
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return name + (enabled ? " (enabled)" : " (disabled)");
    }

    @Override
    public MvxmenuWidget.Type getType() {
        return MvxmenuWidget.Type.MODULE_CARD;
    }

    @Override
    public String getNarrationText() {
        return "Module " + name + ", " + (disabled ? "locked" : enabled ? "enabled" : "disabled") + ". " + description;
    }

    @Override
    public Priority getNarrationPriority() {
        return Priority.NORMAL;
    }

    @Override
    public boolean isFocused() {
        return focused;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public int getBorderColor() {
        if (disabled) return MvxmenuTheme.BD_1;
        if (enabled) return MvxmenuTheme.AC;
        if (hovered || focused) return MvxmenuTheme.BD_2;
        return MvxmenuTheme.BD_1;
    }

    public int getStatusDotColor() {
        return enabled ? MvxmenuTheme.SUCCESS : MvxmenuTheme.TX_2;
    }
}
