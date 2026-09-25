package dev.mvxmenu.ui.widget;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.theme.FontRenderer;
import dev.mvxmenu.theme.MvxmenuIcons;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

/**
 * Module Card Widget v2 - Rounded corners (rx=12), SVG icons, new typography.
 * 200x80px, left status bar (4px), top accent bar (2px), JetBrains Mono + Inter fonts.
 */
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

    // Animation progress
    private float hoverProgress = 0f;
    private float focusProgress = 0f;

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
        if (module != null) {
            module.setEnabled(enabled);
        }
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 257 || keyCode == 32) { // Enter or Space
            if (bounds != null && (hovered || focused)) {
                enabled = !enabled;
                if (module != null) {
                    module.setEnabled(enabled);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        hovered = isHovered(mouseX, mouseY);
        if (bounds == null) return;

        // Animate hover/focus progress
        hoverProgress = lerp(hoverProgress, (hovered || focused) ? 1f : 0f, delta * 8f);
        focusProgress = lerp(focusProgress, focused ? 1f : 0f, delta * 8f);

        // Determine colors
        int bgColor;
        int borderColor;
        int statusColor;

        if (disabled) {
            bgColor = MvxmenuTheme.BG_1;
            borderColor = MvxmenuTheme.BD_1;
            statusColor = MvxmenuTheme.TX_2;
        } else if (enabled) {
            bgColor = lerpColor(MvxmenuTheme.BG_0, MvxmenuTheme.BG_2, hoverProgress * 0.3f);
            borderColor = MvxmenuTheme.AC;
            statusColor = MvxmenuTheme.SUCCESS;
        } else {
            bgColor = lerpColor(MvxmenuTheme.BG_0, MvxmenuTheme.BG_2, hoverProgress);
            borderColor = lerpColor(MvxmenuTheme.BD_1, MvxmenuTheme.AC, hoverProgress);
            statusColor = MvxmenuTheme.TX_2;
        }

        // Rounded background (rx=12)
        RoundedRectRenderer.render(context, bounds.x, bounds.y, bounds.width, bounds.height,
                MvxmenuTheme.R_CARD, bgColor);

        // Top accent bar (2px)
        if (enabled || hovered || focused) {
            int accentAlpha = (int) (255 * (enabled ? 1f : hoverProgress));
            int topAccentColor = (borderColor & 0x00FFFFFF) | (accentAlpha << 24);
            context.fill(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + 2, topAccentColor);
        }

        // Left status bar (4px wide, full height, rounded on left)
        RoundedRectRenderer.render(context, bounds.x, bounds.y, 4, bounds.height,
                MvxmenuTheme.R_CARD, statusColor);

        // Category icon (24px, SVG)
        if (module != null) {
            MvxmenuIcons icon = MvxmenuIcons.fromCategoryName(module.getCategory().getDisplayName());
            int iconX = bounds.x + 12;
            int iconY = bounds.y + (bounds.height - 24) / 2;
            int iconColor = enabled ? MvxmenuTheme.AC : (hovered ? MvxmenuTheme.TX_1 : MvxmenuTheme.TX_2);
            icon.render(context, iconX, iconY, 24, iconColor);
        }

        // Text with new typography
        // Name: JetBrains Mono, TYPE_DEFAULT (12px), uppercase
        // Description: Inter, TYPE_BODY (11px)
        int textX = bounds.x + 44;
        int nameY = bounds.y + 10;
        int descY = bounds.y + 26;

        FontRenderer.drawTextSimple(context, name.toUpperCase(), textX, nameY, MvxmenuTheme.TX_0, true, "ui");
        FontRenderer.drawTextSimple(context, description, textX, descY, MvxmenuTheme.TX_1, true, "body");

        // State text (ON/OFF) - right aligned
        String stateText = enabled ? "ON" : "OFF";
        int stateColor = enabled ? MvxmenuTheme.SUCCESS : MvxmenuTheme.TX_2;
        net.minecraft.client.font.TextRenderer tr = net.minecraft.client.MinecraftClient.getInstance().textRenderer;
        if (tr != null) {
            int stateX = bounds.x + bounds.width - tr.getWidth(stateText) - 12;
            FontRenderer.drawTextSimple(context, stateText, stateX, nameY, stateColor, true, "ui");
        }

        // Focus ring
        if (focused) {
            float ringProgress = focusProgress;
            int ringAlpha = (int) (255 * ringProgress);
            int ringColor = (MvxmenuTheme.AC & 0x00FFFFFF) | (ringAlpha << 24);
            RoundedRectRenderer.renderBorder(context,
                    bounds.x - 2, bounds.y - 2,
                    bounds.width + 4, bounds.height + 4,
                    MvxmenuTheme.R_CARD + 2, 2, ringColor, bgColor);
        }
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return name + (enabled ? " (enabled)" : " (disabled)") + " - " + description;
    }

    @Override
    public Type getType() {
        return Type.MODULE_CARD;
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

    private float lerp(float a, float b, float t) {
        return a + (b - a) * Math.min(1f, t);
    }

    private int lerpColor(int from, int to, float t) {
        t = Math.min(1f, Math.max(0f, t));
        int r = (int) ((((from >> 16) & 0xFF) * (1 - t)) + (((to >> 16) & 0xFF) * t));
        int g = (int) ((((from >> 8) & 0xFF) * (1 - t)) + (((to >> 8) & 0xFF) * t));
        int b = (int) (((from & 0xFF) * (1 - t)) + ((to & 0xFF) * t));
        int a = (int) ((((from >> 24) & 0xFF) * (1 - t)) + (((to >> 24) & 0xFF) * t));
        return (a << 24) | (r << 16) | (g << 8) | b;
    }
}