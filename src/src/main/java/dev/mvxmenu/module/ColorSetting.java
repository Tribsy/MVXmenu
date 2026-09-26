package dev.mvxmenu.module;

import net.minecraft.text.Text;

public class ColorSetting extends Setting<Integer> {

    private final boolean alpha;

    public ColorSetting(String id, String name, String description, int defaultColor, boolean alpha) {
        super(id, name, description, defaultColor);
        this.alpha = alpha;
    }

    public boolean hasAlpha() {
        return alpha;
    }

    @Override
    protected Integer validate(Integer value) {
        return value != null ? value : 0xFFFFFFFF;
    }

    @Override
    public Text getDisplayValue() {
        int color = getValue();
        String hex = String.format("#%06X", color & 0xFFFFFF);
        if (alpha) {
            hex = String.format("#%08X", color);
        }
        return Text.literal(hex);
    }

    public int getRed() {
        return (getValue() >> 16) & 0xFF;
    }

    public int getGreen() {
        return (getValue() >> 8) & 0xFF;
    }

    public int getBlue() {
        return getValue() & 0xFF;
    }

    public int getAlpha() {
        return (getValue() >> 24) & 0xFF;
    }
}