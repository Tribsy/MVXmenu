package dev.mvxmenu.config;

/**
 * A single configuration parameter for a module or screen.
 */
public final class ConfigParameter {

    private final String name;
    private final String label;
    private final String type;
    private final Object defaultValue;
    private Object value;

    public ConfigParameter(String name, String label, String type, Object defaultValue) {
        this.name = name;
        this.label = label;
        this.type = type;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getType() {
        return type;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void resetToDefault() {
        this.value = defaultValue;
    }
}
