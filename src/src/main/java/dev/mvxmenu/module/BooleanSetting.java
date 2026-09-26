package dev.mvxmenu.module;

import net.minecraft.text.Text;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String id, String name, String description, boolean defaultValue) {
        super(id, name, description, defaultValue);
    }

    @Override
    protected Boolean validate(Boolean value) {
        return value != null ? value : false;
    }

    @Override
    public Text getDisplayValue() {
        return Text.literal(getValue() ? "ON" : "OFF");
    }
}