package dev.mvxmenu.module.impl;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.BooleanSetting;
import dev.mvxmenu.module.IntegerSetting;
import dev.mvxmenu.module.ModuleId;
import dev.mvxmenu.module.ModuleMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Set;
import java.util.Collections;

public class SprintModule extends Module {

    private final BooleanSetting omniSprint;
    private final IntegerSetting sprintSpeed;

    public SprintModule() {
        super(ModuleMetadata.builder()
                .id(ModuleId.of("sprint"))
                .displayName("Sprint")
                .description("Automatically sprint when moving forward")
                .category(Module.Category.MOVEMENT)
                .version("1.0.0")
                .dependencies(Set.of())
                .softDependencies(Set.of())
                .authors(List.of("MVXmenu"))
                .homepage("https://github.com/mvxmenu/mvxmenu")
                .build());
        this.omniSprint = registerSetting(new BooleanSetting("omni_sprint", "Omni Sprint", "Sprint in all directions", false));
        this.sprintSpeed = registerSetting(new IntegerSetting("sprint_speed", "Sprint Speed", "Speed multiplier (%)", 130, 100, 200));
    }

    private MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onTick() {
        MinecraftClient mc = getMc();
        if (mc == null || mc.player == null || mc.player.isSpectator() || mc.player.isCreative()) return;

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