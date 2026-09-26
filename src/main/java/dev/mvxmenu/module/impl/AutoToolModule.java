package dev.mvxmenu.module.impl;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.BooleanSetting;
import dev.mvxmenu.module.IntegerSetting;
import dev.mvxmenu.module.ModuleId;
import dev.mvxmenu.module.ModuleMetadata;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.slot.SlotActionType;

import java.util.List;
import java.util.Set;
import java.util.Collections;

public class AutoToolModule extends Module {

    private final BooleanSetting switchBack;
    private final IntegerSetting delay;
    private int switchCooldown = 0;
    private int prevSlot = -1;

    public AutoToolModule() {
        super(ModuleMetadata.builder()
                .id(ModuleId.of("auto_tool"))
                .displayName("Auto Tool")
                .description("Automatically switch to best tool")
                .category(Module.Category.PLAYER)
                .version("1.0.0")
                .dependencies(Set.of())
                .softDependencies(Set.of())
                .authors(List.of("MVXmenu"))
                .homepage("https://github.com/mvxmenu/mvxmenu")
                .build());
        this.switchBack = registerSetting(new BooleanSetting("switch_back", "Switch Back", "Return to previous slot", true));
        this.delay = registerSetting(new IntegerSetting("delay", "Switch Delay", "Delay in ticks", 1, 0, 10));
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
        if (mc == null || mc.player == null || mc.interactionManager == null) return;

        if (switchCooldown > 0) {
            switchCooldown--;
            return;
        }

        net.minecraft.util.hit.HitResult crosshair = mc.crosshairTarget;
        if (crosshair == null || crosshair.getType() != net.minecraft.util.hit.HitResult.Type.BLOCK) return;

        net.minecraft.util.math.BlockPos blockPos = ((net.minecraft.util.hit.BlockHitResult) crosshair).getBlockPos();
        net.minecraft.block.BlockState targetBlock = mc.world.getBlockState(blockPos);
        if (targetBlock == null || targetBlock.isAir()) return;

        int bestSlot = findBestTool(targetBlock);
        if (bestSlot != -1 && bestSlot != mc.player.getInventory().selectedSlot) {
            if (switchBack.getValue() && prevSlot == -1) {
                prevSlot = mc.player.getInventory().selectedSlot;
            }
            mc.player.getInventory().selectedSlot = bestSlot;
            switchCooldown = delay.getValue();
        } else if (switchBack.getValue() && prevSlot != -1 && prevSlot != mc.player.getInventory().selectedSlot) {
            mc.player.getInventory().selectedSlot = prevSlot;
            prevSlot = -1;
        }
    }

    private int findBestTool(net.minecraft.block.BlockState targetBlock) {
        MinecraftClient mc = getMc();
        if (mc == null || mc.player == null) return -1;

        int bestSlot = -1;
        float bestSpeed = 1.0f;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;

            float speed = stack.getMiningSpeedMultiplier(targetBlock);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }

        return bestSlot;
    }

    public boolean isSwitchBack() {
        return switchBack.getValue();
    }

    public int getDelay() {
        return delay.getValue();
    }
}