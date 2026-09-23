package dev.mvxmenu.ui.view;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.Setting;
import dev.mvxmenu.ui.widget.*;

import java.util.ArrayList;
import java.util.List;

public class ModuleDetailView {

    private final Module module;
    private final List<MvxmenuWidget> settingWidgets;

    public ModuleDetailView(Module module) {
        this.module = module;
        this.settingWidgets = new ArrayList<>();
        initSettingWidgets();
    }

    private void initSettingWidgets() {
        settingWidgets.clear();
        for (Setting<?> setting : module.getSettings()) {
            MvxmenuWidget widget = createWidgetForSetting(setting);
            if (widget != null) {
                settingWidgets.add(widget);
            }
        }
    }

    private MvxmenuWidget createWidgetForSetting(Setting<?> setting) {
        String widgetId = "module_" + module.getId() + "_" + setting.getId();
        if (setting instanceof dev.mvxmenu.module.BooleanSetting bs) {
            return new ToggleWidget(widgetId, bs.getValue());
        } else if (setting instanceof dev.mvxmenu.module.IntegerSetting is) {
            return new SliderWidget(widgetId, setting.getName(), is.getMin(), is.getMax(), is.getValue());
        } else if (setting instanceof dev.mvxmenu.module.DoubleSetting ds) {
            return new SliderWidget(widgetId, setting.getName(), (int)(ds.getMin()*100), (int)(ds.getMax()*100), (int)(ds.getValue()*100));
        } else if (setting instanceof dev.mvxmenu.module.StringSetting ss) {
            return null;
        } else if (setting instanceof dev.mvxmenu.module.EnumSetting<?> es) {
            String[] options = new String[es.getValues().length];
            for (int i = 0; i < es.getValues().length; i++) {
                options[i] = es.getValues()[i].name();
            }
            return new DropdownWidget(widgetId, setting.getName(), options, es.getValue().name());
        } else if (setting instanceof dev.mvxmenu.module.ColorSetting cs) {
            return null;
        } else if (setting instanceof dev.mvxmenu.module.KeybindSetting ks) {
            return new KeybindWidget(widgetId, setting.getName(), ks.getDisplayValue().getString());
        }
        return null;
    }

    public Module getModule() {
        return module;
    }

    public String getStatus() {
        return module.isEnabled() ? "enabled" : "disabled";
    }

    public boolean hasParameters() {
        return !settingWidgets.isEmpty();
    }

    public List<MvxmenuWidget> getSettingWidgets() {
        return settingWidgets;
    }

    @SuppressWarnings("unchecked")
    public void applySettings() {
        for (MvxmenuWidget widget : settingWidgets) {
            String widgetId = widget.getId();
            String settingId = widgetId.replace("module_" + module.getId() + "_", "");
            for (Setting<?> setting : module.getSettings()) {
                if (setting.getId().equals(settingId)) {
                    if (setting instanceof dev.mvxmenu.module.BooleanSetting bs && widget instanceof ToggleWidget tw) {
                        bs.setValue(tw.getEnabled());
                    } else if (setting instanceof dev.mvxmenu.module.IntegerSetting is && widget instanceof SliderWidget sw) {
                        is.setValue(sw.getValue());
                    } else if (setting instanceof dev.mvxmenu.module.DoubleSetting ds && widget instanceof SliderWidget sw) {
                        ds.setValue(sw.getValue() / 100.0);
                    } else if (setting instanceof dev.mvxmenu.module.EnumSetting<?> es && widget instanceof DropdownWidget dw) {
                        try {
                            dev.mvxmenu.module.EnumSetting rawEs = (dev.mvxmenu.module.EnumSetting) es;
                            Class<Enum> enumClass = (Class<Enum>) rawEs.getEnumClass();
                            Enum value = Enum.valueOf(enumClass, dw.getValue());
                            rawEs.setValue(value);
                        } catch (IllegalArgumentException ignored) {}
                    } else if (setting instanceof dev.mvxmenu.module.KeybindSetting kbs && widget instanceof KeybindWidget kw) {
                        String val = kw.getValue();
                        if (val.startsWith("KEY:")) {
                            try {
                                int keyCode = Integer.parseInt(val.substring(4));
                                kbs.setKey(keyCode);
                            } catch (NumberFormatException ignored) {
                                kbs.setKey(0);
                            }
                        } else if (val.equals("UNBOUND")) {
                            kbs.setKey(0);
                        }
                    }
                }
            }
        }
    }
}