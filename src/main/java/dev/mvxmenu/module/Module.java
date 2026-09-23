package dev.mvxmenu.module;

import dev.mvxmenu.ui.widget.ModuleCardWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class Module {

    private final String id;
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;
    private final List<Setting<?>> settings = new ArrayList<>();

    public Module(String id, String name, String description, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.enabled = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    protected <S extends Setting<?>> S registerSetting(S setting) {
        settings.add(setting);
        return setting;
    }

    public void onEnable() {}

    public void onDisable() {}

    public void onTick() {}

    public ModuleCardWidget createCardWidget() {
        return new ModuleCardWidget(this);
    }

    public enum Category {
        COMBAT("Combat", dev.mvxmenu.theme.MvxmenuIcons.CROSSHAIR),
        MOVEMENT("Movement", dev.mvxmenu.theme.MvxmenuIcons.ZAP),
        PLAYER("Player", dev.mvxmenu.theme.MvxmenuIcons.EYE),
        RENDER("Render", dev.mvxmenu.theme.MvxmenuIcons.LAYERS),
        WORLD("World", dev.mvxmenu.theme.MvxmenuIcons.MAP),
        EXPLOIT("Exploit", dev.mvxmenu.theme.MvxmenuIcons.SHIELD),
        MISC("Misc", dev.mvxmenu.theme.MvxmenuIcons.SLIDERS);

        private final String displayName;
        private final dev.mvxmenu.theme.MvxmenuIcons icon;

        Category(String displayName, dev.mvxmenu.theme.MvxmenuIcons icon) {
            this.displayName = displayName;
            this.icon = icon;
        }

        public String getDisplayName() {
            return displayName;
        }

        public dev.mvxmenu.theme.MvxmenuIcons getIcon() {
            return icon;
        }
    }
}