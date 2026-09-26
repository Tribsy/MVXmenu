package dev.mvxmenu.module;

import net.minecraft.text.Text;

public abstract class Setting<T> {

    private final String id;
    private final String name;
    private final String description;
    private T value;
    private final T defaultValue;

    public Setting(String id, String name, String description, T defaultValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.value = defaultValue;
        this.defaultValue = defaultValue;
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

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = validate(value);
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public void reset() {
        this.value = defaultValue;
    }

    protected abstract T validate(T value);

    public abstract Text getDisplayValue();
}