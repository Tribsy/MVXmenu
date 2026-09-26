package dev.mvxmenu.ui.views;

import dev.mvxmenu.module.*;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
import dev.mvxmenu.ui.widget.*;
import dev.mvxmenu.theme.MvxmenuTheme;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ModuleDetailView extends BaseView {

    private final Module module;
    private final List<WidgetSettingPair> widgetSettingPairs = new ArrayList<>();

    public ModuleDetailView(MvxmenuLayout layout, Module module) {
        super(layout);
        this.module = Objects.requireNonNull(module);
        initSettingWidgets();
    }

    private void initSettingWidgets() {
        widgetSettingPairs.clear();
        for (Setting<?> setting : module.getSettings()) {
            MvxmenuWidget widget = createWidgetForSetting(setting);
            if (widget != null) {
                widgetSettingPairs.add(new WidgetSettingPair(widget, setting));
                addWidget(widget);
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

    @Override
    public void init() {
        clearWidgets();
        initSettingWidgets();
        
        int contentX = layout.contentX() + layout.padding();
        int contentY = layout.contentY() + layout.padding();
        int y = contentY + 70;
        
        for (MvxmenuWidget widget : widgets) {
            if (widget instanceof ToggleWidget tw) {
                widget.setBounds(new MvxmenuLayout.Bounds(contentX + layout.contentWidth() - 60, y - 2, 40, 24));
            } else {
                widget.setBounds(new MvxmenuLayout.Bounds(contentX + 8, y + 16, layout.contentWidth() - 16, 28));
                y += 20;
            }
            y += 40;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        if (textRenderer == null) return;
        int contentX = layout.contentX() + layout.padding();
        int contentY = layout.contentY() + layout.padding();
        
        context.fill(contentX, contentY, contentX + layout.contentWidth(), contentY + layout.contentHeight(), MvxmenuTheme.BG_1);
        context.fill(contentX, contentY, contentX + layout.contentWidth(), contentY + 2, MvxmenuTheme.AC);

        context.fill(contentX + 8, contentY + 10, contentX + 24, contentY + 22, module.isEnabled() ? MvxmenuTheme.SUCCESS : MvxmenuTheme.DANGER);
        context.drawText(textRenderer, module.getName().toUpperCase(), contentX + 32, contentY + 10, MvxmenuTheme.TX_0, true);
        context.drawText(textRenderer, module.getDescription(), contentX + 8, contentY + 22, MvxmenuTheme.TX_1, true);
        context.drawText(textRenderer, "STATE // " + (module.isEnabled() ? "ACTIVE" : "IDLE"), contentX + 8, contentY + 34, module.isEnabled() ? MvxmenuTheme.SUCCESS : MvxmenuTheme.DANGER, true);

        if (hasParameters()) {
            context.drawText(textRenderer, "TUNING", contentX + 8, contentY + 52, MvxmenuTheme.AC, true);
        }

        for (MvxmenuWidget w : widgets) {
            w.render(context, mouseX, mouseY, delta);
        }
    }

    public Module getModule() {
        return module;
    }

    public boolean hasParameters() {
        return !widgetSettingPairs.isEmpty();
    }

    public List<MvxmenuWidget> getSettingWidgets() {
        return widgets;
    }

    public void applySettings() {
        for (WidgetSettingPair pair : widgetSettingPairs) {
            MvxmenuWidget widget = pair.widget;
            Setting<?> setting = pair.setting;
            if (setting instanceof BooleanSetting bs && widget instanceof ToggleWidget tw) {
                bs.setValue(tw.getEnabled());
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
                } catch (IllegalArgumentException ignored) {}
            } else if (setting instanceof KeybindSetting ks && widget instanceof KeybindWidget kw) {
                String val = kw.getValue();
                if (val.startsWith("KEY:")) {
                    try {
                        ks.setKey(Integer.parseInt(val.substring(4)));
                    } catch (NumberFormatException ignored) { ks.setKey(0); }
                } else if (val.equals("UNBOUND")) {
                    ks.setKey(0);
                }
            }
        }
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
