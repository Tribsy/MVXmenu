package dev.mvxmenu.module;

import net.minecraft.text.Text;

public class StringSetting extends Setting<String> {

    public StringSetting(String id, String name, String description, String defaultValue) {
        super(id, name, description, defaultValue);
    }

    @Override
    protected String validate(String value) {
        return value != null ? value : "";
    }

    @Override
    public Text getDisplayValue() {
        return Text.literal(getValue());
    }
}