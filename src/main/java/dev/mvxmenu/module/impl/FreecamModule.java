package dev.mvxmenu.module.impl;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.BooleanSetting;
import dev.mvxmenu.module.IntegerSetting;
import dev.mvxmenu.module.ModuleId;
import dev.mvxmenu.module.ModuleMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Set;
import java.util.Collections;

public class FreecamModule extends Module {

    private final IntegerSetting speed;
    private final BooleanSetting noclip;
    private ClientPlayerEntity originalPlayer;
    private Vec3d cameraPos;
    private float prevYaw, prevPitch;

    public FreecamModule() {
        super(ModuleMetadata.builder()
                .id(ModuleId.of("freecam"))
                .displayName("Freecam")
                .description("Detach camera from player")
                .category(Module.Category.EXPLOIT)
                .version("1.0.0")
                .dependencies(Set.of())
                .softDependencies(Set.of())
                .authors(List.of("MVXmenu"))
                .homepage("https://github.com/mvxmenu/mvxmenu")
                .build());
        this.speed = registerSetting(new IntegerSetting("speed", "Speed", "Camera movement speed (%)", 200, 50, 500));
        this.noclip = registerSetting(new BooleanSetting("noclip", "NoClip", "Pass through blocks", false));
    }

    private MinecraftClient getMc() {
        return MinecraftClient.getInstance();
    }

    @Override
    public void onEnable() {
        MinecraftClient mc = getMc();
        if (mc == null || mc.player == null) return;

        cameraPos = mc.player.getPos();
        prevYaw = mc.player.getYaw();
        prevPitch = mc.player.getPitch();

        mc.player.setInvisible(true);
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = getMc();
        if (mc == null || mc.player == null) return;

        mc.player.setPos(cameraPos.x, cameraPos.y, cameraPos.z);
        mc.player.setYaw(prevYaw);
        mc.player.setPitch(prevPitch);
        mc.player.setInvisible(false);
    }

    @Override
    public void onTick() {
        MinecraftClient mc = getMc();
        if (mc == null || mc.player == null) return;

        double moveSpeed = speed.getValue() / 100.0 * 0.1;
        Vec3d motion = Vec3d.ZERO;

        if (mc.options.forwardKey.isPressed()) motion = motion.add(mc.player.getRotationVec(1.0f).multiply(moveSpeed));
        if (mc.options.backKey.isPressed()) motion = motion.subtract(mc.player.getRotationVec(1.0f).multiply(moveSpeed));
        if (mc.options.leftKey.isPressed()) motion = motion.add(mc.player.getRotationVec(1.0f).rotateY(90).multiply(moveSpeed));
        if (mc.options.rightKey.isPressed()) motion = motion.add(mc.player.getRotationVec(1.0f).rotateY(-90).multiply(moveSpeed));
        if (mc.options.jumpKey.isPressed()) motion = motion.add(0, moveSpeed, 0);
        if (mc.options.sneakKey.isPressed()) motion = motion.subtract(0, moveSpeed, 0);

        cameraPos = cameraPos.add(motion);
    }

    public int getSpeed() {
        return speed.getValue();
    }

    public boolean isNoclip() {
        return noclip.getValue();
    }

    public Vec3d getCameraPos() {
        return cameraPos;
    }
}