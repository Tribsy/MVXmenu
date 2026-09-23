package dev.mvxmenu.module;

import net.minecraft.text.Text;

public class EnumSetting<E extends Enum<E>> extends Setting<E> {

    private final Class<E> enumClass;

    public EnumSetting(String id, String name, String description, E defaultValue, Class<E> enumClass) {
        super(id, name, description, defaultValue);
        this.enumClass = enumClass;
    }

    public Class<E> getEnumClass() {
        return enumClass;
    }

    public E[] getValues() {
        return enumClass.getEnumConstants();
    }

    @Override
    protected E validate(E value) {
        return value != null ? value : getDefaultValue();
    }

    @Override
    public Text getDisplayValue() {
        return Text.literal(getValue().name());
    }
}