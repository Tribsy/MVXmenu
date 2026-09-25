package dev.mvxmenu.ui.screen;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.Setting;
import dev.mvxmenu.module.BooleanSetting;
import dev.mvxmenu.module.IntegerSetting;
import dev.mvxmenu.module.DoubleSetting;
import dev.mvxmenu.module.EnumSetting;
import dev.mvxmenu.module.KeybindSetting;
import dev.mvxmenu.module.StringSetting;
import dev.mvxmenu.module.ColorSetting;
import dev.mvxmenu.networking.MvxmenuNetworking;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.widget.*;
import dev.mvxmenu.theme.FontRenderer;
import dev.mvxmenu.theme.MvxmenuIcons;
import dev.mvxmenu.theme.MvxmenuTheme;
import dev.mvxmenu.theme.RoundedRectRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Module detail panel widget - replaces ModuleDetailView + renderModuleDetail().
 * Slide-in from right animation (200ms spring), inline settings widgets.
 */
public class ModuleDetailPanelWidget implements MvxmenuWidget {

    private final String id = "module_detail";
    private MvxmenuLayout.Bounds bounds;
    private final MvxmenuLayout layout;
    private Module module;
    private final List<WidgetSettingPair> widgetSettingPairs = new ArrayList<>();
    private float slideProgress = 0f; // 0 = hidden, 1 = visible
    private boolean visible = false;
    private float animationTimer = 0f;

    public ModuleDetailPanelWidget(MvxmenuLayout layout) {
        this.layout = layout;
    }

    public void openModule(Module module) {
        this.module = module;
        this.visible = true;
        this.animationTimer = 0f;
        initSettingWidgets();
    }

    public void close() {
        this.visible = false;
        this.module = null;
        this.widgetSettingPairs.clear();
    }

    public boolean isOpen() {
        return visible && module != null;
    }

    public Module getModule() {
        return module;
    }

    private void initSettingWidgets() {
        widgetSettingPairs.clear();
        if (module == null) return;

        for (Setting<?> setting : module.getSettings()) {
            MvxmenuWidget widget = createWidgetForSetting(setting);
            if (widget != null) {
                widgetSettingPairs.add(new WidgetSettingPair(widget, setting));
            }
        }
    }

    private MvxmenuWidget createWidgetForSetting(Setting<?> setting) {
        String widgetId = "module_" + module.getId() + "_" + setting.getId();
        if (setting instanceof BooleanSetting bs) {
            return new ToggleWidget(widgetId, bs.getValue());
        } else if (setting instanceof IntegerSetting is) {
            return new SliderWidget(widgetId, setting.getName(), is.getMin(), is.getMax(), is.getValue());
        } else if (setting instanceof DoubleSetting ds) {
            return new SliderWidget(widgetId, setting.getName(), (int)(ds.getMin()*100), (int)(ds.getMax()*100), (int)(ds.getValue()*100));
        } else if (setting instanceof EnumSetting<?> es) {
            String[] options = new String[es.getValues().length];
            for (int i = 0; i < es.getValues().length; i++) {
                options[i] = es.getValues()[i].name();
            }
            return new DropdownWidget(widgetId, setting.getName(), options, es.getValue().name());
        } else if (setting instanceof KeybindSetting ks) {
            return new KeybindWidget(widgetId, setting.getName(), ks.getDisplayValue().getString());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public void applySettings() {
        if (module == null) return;

        for (WidgetSettingPair pair : widgetSettingPairs) {
            MvxmenuWidget widget = pair.widget;
            Setting<?> setting = pair.setting;

            if (setting instanceof BooleanSetting bs && widget instanceof ToggleWidget tw) {
                boolean oldValue = bs.getValue();
                boolean newValue = tw.getEnabled();
                bs.setValue(newValue);
                if (oldValue != newValue && module != null) {
                    MvxmenuNetworking.sendModuleToggleToServer(module.getId().toString(), module.isEnabled());
                }
            } else if (setting instanceof IntegerSetting is && widget instanceof SliderWidget sw) {
                is.setValue(sw.getValue());
            } else if (setting instanceof DoubleSetting ds && widget instanceof SliderWidget sw) {
                ds.setValue(sw.getValue() / 100.0);
            } else if (setting instanceof EnumSetting<?> es && widget instanceof DropdownWidget dw) {
                try {
                    EnumSetting rawEs = (EnumSetting) es;
                    Class<Enum> enumClass = rawEs.getEnumClass();
                    Enum value = Enum.valueOf(enumClass, dw.getValue());
                    rawEs.setValue(value);
                } catch (IllegalArgumentException e) {
                    // If the value is not a valid enum constant, ignore
                }
            } else if (setting instanceof KeybindSetting ks && widget instanceof KeybindWidget kw) {
                String val = kw.getValue();
                if (val.startsWith("KEY:")) {
                    try {
                        int keyCode = Integer.parseInt(val.substring(4));
                        ks.setKey(keyCode);
                    } catch (NumberFormatException e) {
                        ks.setKey(0);
                    }
                } else if (val.equals("UNBOUND")) {
                    ks.setKey(0);
                }
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
        layoutSettings();
    }

    private void layoutSettings() {
        if (bounds == null || module == null) return;

        int contentX = bounds.x + MvxmenuTheme.SP_4;
        int contentY = bounds.y + MvxmenuTheme.SP_4 + 40; // Below header
        int width = bounds.width - MvxmenuTheme.SP_4 * 2;
        int y = contentY;

        for (WidgetSettingPair pair : widgetSettingPairs) {
            MvxmenuWidget widget = pair.widget;
            Setting<?> setting = pair.setting;

            if (widget instanceof ToggleWidget) {
                // Label on left, toggle on right
                widget.setBounds(new MvxmenuLayout.Bounds(contentX + width - 44, y, 40, 24));
            } else if (widget instanceof SliderWidget) {
                widget.setBounds(new MvxmenuLayout.Bounds(contentX, y + 16, width, 28));
                y += 20;
            } else if (widget instanceof DropdownWidget) {
                widget.setBounds(new MvxmenuLayout.Bounds(contentX, y + 16, width, 28));
                y += 20;
            } else if (widget instanceof KeybindWidget) {
                widget.setBounds(new MvxmenuLayout.Bounds(contentX, y + 16, width, 28));
                y += 20;
            }
            y += 36;
        }
    }

    @Override
    public boolean isEnabled() {
        return visible;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.visible = enabled;
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
        if (!visible || bounds == null || !bounds.contains((int)x, (int)y)) return false;

        for (WidgetSettingPair pair : widgetSettingPairs) {
            if (pair.widget.mouseClicked(x, y, button)) {
                applySettings();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 1 || keyCode == 256) { // ESC
            close();
            return true;
        }
        for (WidgetSettingPair pair : widgetSettingPairs) {
            if (pair.widget.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        for (WidgetSettingPair pair : widgetSettingPairs) {
            if (pair.widget.charTyped(codePoint, modifiers)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!visible || module == null || bounds == null) return;

        // Animate slide-in
        animationTimer += delta * 1000f; // ms
        float targetProgress = 1f;
        slideProgress = lerp(slideProgress, targetProgress, delta * 5f); // ~200ms

        // Calculate animated position (slide from right)
        int animatedX = bounds.x + (int)((1f - slideProgress) * 100);
        int animatedWidth = bounds.width;

        // Panel background (rx=12)
        RoundedRectRenderer.render(context, animatedX, bounds.y, animatedWidth, bounds.height,
                MvxmenuTheme.R_PANEL, MvxmenuTheme.BG_0);

        // Top accent bar
        context.fill(animatedX, bounds.y, animatedX + animatedWidth, bounds.y + 2, MvxmenuTheme.AC);

        // Left edge accent
        RoundedRectRenderer.render(context, animatedX, bounds.y, 4, bounds.height,
                MvxmenuTheme.R_PANEL, MvxmenuTheme.AC);

        int contentX = animatedX + MvxmenuTheme.SP_4;
        int contentY = bounds.y + MvxmenuTheme.SP_4;

        // Module header
        int iconX = contentX + 8;
        int iconY = contentY + 10;
        MvxmenuIcons icon = MvxmenuIcons.fromCategoryName(module.getCategory().getDisplayName());
        icon.render(context, iconX, iconY, 24, module.isEnabled() ? MvxmenuTheme.SUCCESS : MvxmenuTheme.DANGER);

        // Module name
        FontRenderer.drawTextSimple(context, module.getDisplayName().toUpperCase(), iconX + 32, contentY + 10, MvxmenuTheme.TX_0, true, "ui");

        // Status badge
        String statusText = module.isEnabled() ? "ACTIVE" : "IDLE";
        int statusColor = module.isEnabled() ? MvxmenuTheme.SUCCESS : MvxmenuTheme.DANGER;
        renderBadge(context, contentX + iconX + 32 + 120, contentY + 10, statusText, statusColor);

        // Description
        FontRenderer.drawTextSimple(context, module.getDescription(), contentX + 8, contentY + 30, MvxmenuTheme.TX_1, true, "body");

        // Settings section
        if (!widgetSettingPairs.isEmpty()) {
            FontRenderer.drawTextSimple(context, "TUNING", contentX + 8, contentY + 52, MvxmenuTheme.AC, true, "ui");
            renderSettings(context, mouseX, mouseY, delta, contentX + 8, contentY + 70, bounds.width - 24);
        }

        // Back hint
        String backHint = "[ESC] Back";
        net.minecraft.client.font.TextRenderer tr = net.minecraft.client.MinecraftClient.getInstance().textRenderer;
        if (tr != null) {
            int hintX = animatedX + animatedWidth - tr.getWidth(backHint) - 12;
            int hintY = bounds.y + bounds.height - 18;
            FontRenderer.drawTextSimple(context, backHint, hintX, hintY, MvxmenuTheme.TX_2, true, "ui");
        }
    }

    private void renderSettings(DrawContext context, int mouseX, int mouseY, float delta, int x, int y, int width) {
        for (WidgetSettingPair pair : widgetSettingPairs) {
            MvxmenuWidget widget = pair.widget;
            Setting<?> setting = pair.setting;

            if (widget instanceof ToggleWidget tw) {
                FontRenderer.drawTextSimple(context, setting.getName().toUpperCase(), x, y, MvxmenuTheme.TX_1, true, "ui");
                widget.setBounds(new MvxmenuLayout.Bounds(x + width - 44, y - 2, 40, 24));
                widget.render(context, mouseX, mouseY, delta);
                y += 36;
            } else if (widget instanceof SliderWidget sw) {
                FontRenderer.drawTextSimple(context, sw.getLabel() + ": " + sw.getValue(), x, y, MvxmenuTheme.TX_1, true, "ui");
                widget.setBounds(new MvxmenuLayout.Bounds(x, y + 16, width, 28));
                widget.render(context, mouseX, mouseY, delta);
                y += 44;
            } else if (widget instanceof DropdownWidget dw) {
                FontRenderer.drawTextSimple(context, dw.getLabel() + ": " + dw.getValue(), x, y, MvxmenuTheme.TX_1, true, "ui");
                widget.setBounds(new MvxmenuLayout.Bounds(x, y + 16, width, 28));
                widget.render(context, mouseX, mouseY, delta);
                y += 44;
            } else if (widget instanceof KeybindWidget kw) {
                FontRenderer.drawTextSimple(context, kw.getLabel() + ": " + kw.getValue(), x, y, MvxmenuTheme.TX_1, true, "ui");
                widget.setBounds(new MvxmenuLayout.Bounds(x, y + 16, width, 28));
                widget.render(context, mouseX, mouseY, delta);
                y += 44;
            }
        }
    }

    private void renderBadge(DrawContext context, int x, int y, String text, int color) {
        net.minecraft.client.font.TextRenderer tr = net.minecraft.client.MinecraftClient.getInstance().textRenderer;
        if (tr == null) return;

        int padding = 6;
        int textWidth = tr.getWidth(text);
        int badgeWidth = textWidth + padding * 2;
        int badgeHeight = 16;

        RoundedRectRenderer.render(context, x, y, badgeWidth, badgeHeight, MvxmenuTheme.R_BADGE,
                (color & 0x00FFFFFF) | 0x30000000);
        FontRenderer.drawTextSimple(context, text, x + padding, y + 2, color, true, "ui");
    }

    @Override
    public String getTooltipText(int mouseX, int mouseY) {
        return module != null ? module.getDisplayName() : "Module Detail";
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

    private float lerp(float a, float b, float t) {
        return a + (b - a) * Math.min(1f, t);
    }

    private static class WidgetSettingPair {
        final MvxmenuWidget widget;
        final Setting<?> setting;

        WidgetSettingPair(MvxmenuWidget widget, Setting<?> setting) {
            this.widget = widget;
            this.setting = setting;
        }
    }
}