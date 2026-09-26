package dev.mvxmenu.module;

import net.minecraft.text.Text;

public class KeybindSetting extends Setting<Integer> {

    public KeybindSetting(String id, String name, String description, int defaultKey) {
        super(id, name, description, defaultKey);
    }

    @Override
    protected Integer validate(Integer value) {
        return value != null ? value : 0;
    }

    @Override
    public Text getDisplayValue() {
        int key = getValue();
        if (key <= 0) return Text.literal("UNBOUND");
        return Text.literal(org.lwjgl.glfw.GLFW.glfwGetKeyName(key, 0));
    }

    public void setKey(int keyCode) {
        setValue(keyCode);
    }
}