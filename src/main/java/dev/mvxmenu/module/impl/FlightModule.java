package dev.mvxmenu.module.impl;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.BooleanSetting;
import dev.mvxmenu.module.IntegerSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class FlightModule extends Module {

    private final BooleanSetting creativeFlight;
    private final IntegerSetting flySpeed;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private boolean wasFlying = false;

    public FlightModule() {
        super("flight", "Flight", "Allows flying in survival", Module.Category.MOVEMENT);
        this.creativeFlight = registerSetting(new BooleanSetting("creative_flight", "Creative Flight", "Use creative-style flight", true));
        this.flySpeed = registerSetting(new IntegerSetting("fly_speed", "Fly Speed", "Flight speed multiplier (%)", 150, 50, 300));
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            wasFlying = mc.player.getAbilities().flying;
            mc.player.getAbilities().allowFlying = true;
            mc.player.getAbilities().flying = true;
            mc.player.sendAbilitiesUpdate();
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.getAbilities().allowFlying = false;
            mc.player.getAbilities().flying = wasFlying;
            mc.player.getAbilities().setFlySpeed(0.05f);
            mc.player.sendAbilitiesUpdate();
        }
    }

    @Override
    public void onTick() {
        if (mc.player == null || !mc.player.getAbilities().flying) return;

        double speed = flySpeed.getValue() / 100.0 * 0.1;
        Vec3d motion = Vec3d.ZERO;

        if (mc.options.forwardKey.isPressed()) motion = motion.add(mc.player.getRotationVec(1.0f).multiply(speed));
        if (mc.options.backKey.isPressed()) motion = motion.subtract(mc.player.getRotationVec(1.0f).multiply(speed));
        if (mc.options.leftKey.isPressed()) motion = motion.add(mc.player.getRotationVec(1.0f).rotateY(90).multiply(speed));
        if (mc.options.rightKey.isPressed()) motion = motion.add(mc.player.getRotationVec(1.0f).rotateY(-90).multiply(speed));
        if (mc.options.jumpKey.isPressed()) motion = motion.add(0, speed, 0);
        if (mc.options.sneakKey.isPressed()) motion = motion.subtract(0, speed, 0);

        mc.player.setVelocity(mc.player.getVelocity().add(motion));
        mc.player.fallDistance = 0;
    }

    public boolean isCreativeFlight() {
        return creativeFlight.getValue();
    }

    public int getFlySpeed() {
        return flySpeed.getValue();
    }
}