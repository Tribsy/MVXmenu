package dev.mvxmenu.module;

import net.minecraft.text.Text;

public class IntegerSetting extends Setting<Integer> {

    private final int min;
    private final int max;

    public IntegerSetting(String id, String name, String description, int defaultValue, int min, int max) {
        super(id, name, description, defaultValue);
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    protected Integer validate(Integer value) {
        if (value == null) return min;
        return Math.max(min, Math.min(max, value));
    }

    @Override
    public Text getDisplayValue() {
        return Text.literal(String.valueOf(getValue()));
    }
}