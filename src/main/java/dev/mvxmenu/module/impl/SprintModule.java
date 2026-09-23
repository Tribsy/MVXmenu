package dev.mvxmenu.module.impl;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.BooleanSetting;
import dev.mvxmenu.module.IntegerSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.MathHelper;

public class SprintModule extends Module {

    private final BooleanSetting omniSprint;
    private final IntegerSetting sprintSpeed;
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public SprintModule() {
        super("sprint", "Sprint", "Automatically sprint when moving forward", Module.Category.MOVEMENT);
        this.omniSprint = registerSetting(new BooleanSetting("omni_sprint", "Omni Sprint", "Sprint in all directions", false));
        this.sprintSpeed = registerSetting(new IntegerSetting("sprint_speed", "Sprint Speed", "Speed multiplier (%)", 130, 100, 200));
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        if (mc.player == null || mc.player.isSpectator() || mc.player.isCreative()) return;

        boolean forward = mc.options.forwardKey.isPressed();
        boolean backward = mc.options.backKey.isPressed();
        boolean left = mc.options.leftKey.isPressed();
        boolean right = mc.options.rightKey.isPressed();
        boolean moving = forward || backward || left || right;

        if (!moving) return;

        boolean shouldSprint = forward || (omniSprint.getValue() && (backward || left || right));

        if (shouldSprint && !mc.player.isSprinting()) {
            if (mc.player.getHungerManager().getFoodLevel() > 6) {
                mc.player.setSprinting(true);
            }
        }
    }

    public boolean isOmniSprint() {
        return omniSprint.getValue();
    }

    public int getSprintSpeed() {
        return sprintSpeed.getValue();
    }
}