package dev.mvxmenu.module.impl;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.BooleanSetting;
import dev.mvxmenu.module.IntegerSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

public class ScaffoldModule extends Module {

    private final IntegerSetting expandLength;
    private final BooleanSetting rotate;
    private final BooleanSetting tower;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private int placeCooldown = 0;

    public ScaffoldModule() {
        super("scaffold", "Scaffold", "Automatically place blocks under you", Module.Category.MOVEMENT);
        this.expandLength = registerSetting(new IntegerSetting("expand_length", "Expand Length", "Blocks to place ahead", 6, 1, 10));
        this.rotate = registerSetting(new BooleanSetting("rotate", "Rotate", "Rotate to place blocks", true));
        this.tower = registerSetting(new BooleanSetting("tower", "Tower", "Jump to place blocks upward", true));
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;

        if (placeCooldown > 0) {
            placeCooldown--;
            return;
        }

        if (!mc.options.jumpKey.isPressed() && !tower.getValue()) return;

        BlockPos playerPos = mc.player.getBlockPos();
        Vec3d lookVec = mc.player.getRotationVec(1.0f);
        BlockPos targetPos = playerPos.add((int)Math.floor(lookVec.x * expandLength.getValue()), -1, (int)Math.floor(lookVec.z * expandLength.getValue()));

        if (mc.world.getBlockState(targetPos).isAir()) {
            int blockSlot = findBlock();
            if (blockSlot == -1) return;

            int prevSlot = mc.player.getInventory().selectedSlot;
            mc.player.getInventory().selectedSlot = blockSlot;

            BlockHitResult hitResult = new BlockHitResult(
                    Vec3d.ofCenter(targetPos).add(0, 1, 0),
                    Direction.UP,
                    targetPos,
                    false
            );

            if (rotate.getValue()) {
                double diffX = targetPos.getX() + 0.5 - mc.player.getX();
                double diffZ = targetPos.getZ() + 0.5 - mc.player.getZ();
                double diffY = targetPos.getY() + 1 - mc.player.getY();
                double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
                float yaw = (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90;
                float pitch = (float) (-Math.toDegrees(Math.atan2(diffY, dist)));
                mc.player.setYaw(yaw);
                mc.player.setPitch(pitch);
            }

            mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hitResult);
            mc.player.getInventory().selectedSlot = prevSlot;
            placeCooldown = 4;
        }
    }

    private int findBlock() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isEmpty() && stack.getItem() instanceof net.minecraft.item.BlockItem) {
                return i;
            }
        }
        return -1;
    }

    public int getExpandLength() {
        return expandLength.getValue();
    }

    public boolean isRotate() {
        return rotate.getValue();
    }

    public boolean isTower() {
        return tower.getValue();
    }
}