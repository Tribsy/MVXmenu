package dev.mvxmenu.module;

import net.minecraft.text.Text;

public class DoubleSetting extends Setting<Double> {

    private final double min;
    private final double max;

    public DoubleSetting(String id, String name, String description, double defaultValue, double min, double max) {
        super(id, name, description, defaultValue);
        this.min = min;
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    @Override
    protected Double validate(Double value) {
        if (value == null) return min;
        return Math.max(min, Math.min(max, value));
    }

    @Override
    public Text getDisplayValue() {
        return Text.literal(String.format("%.2f", getValue()));
    }
}