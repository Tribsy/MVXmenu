package dev.mvxmenu.module.impl;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.BooleanSetting;
import dev.mvxmenu.module.IntegerSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

public class AutoEatModule extends Module {

    private final IntegerSetting healthThreshold;
    private final BooleanSetting preferGoldenApples;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private int eatCooldown = 0;

    public AutoEatModule() {
        super("auto_eat", "Auto Eat", "Automatically eat food when hungry", Module.Category.PLAYER);
        this.healthThreshold = registerSetting(new IntegerSetting("health_threshold", "Health Threshold", "Eat when health below", 18, 1, 20));
        this.preferGoldenApples = registerSetting(new BooleanSetting("prefer_golden_apples", "Prefer Golden Apples", "Eat golden apples first", true));
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}

    @Override
    public void onTick() {
        if (mc.player == null || mc.player.isSpectator() || mc.player.isCreative()) return;

        float health = mc.player.getHealth();
        float maxHealth = mc.player.getMaxHealth();
        float healthPercent = (health / maxHealth) * 20;

        if (healthPercent >= healthThreshold.getValue()) return;

        if (eatCooldown > 0) {
            eatCooldown--;
            return;
        }

        int foodSlot = findBestFood();
        if (foodSlot == -1) return;

        int prevSlot = mc.player.getInventory().selectedSlot;
        mc.player.getInventory().selectedSlot = foodSlot;
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, foodSlot + 36, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, mc.player.currentScreenHandler.slots.size() - 1, 0, SlotActionType.PICKUP, mc.player);
        mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, foodSlot + 36, 0, SlotActionType.PICKUP, mc.player);
        mc.player.getInventory().selectedSlot = prevSlot;

        eatCooldown = 20; // 1 second cooldown
    }

    private int findBestFood() {
        int bestSlot = -1;
        int bestValue = -1;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;

            int value = getFoodValue(stack);
            if (value > bestValue) {
                bestValue = value;
                bestSlot = i;
            }
        }

        // Also check offhand
        ItemStack offhand = mc.player.getOffHandStack();
        if (!offhand.isEmpty()) {
            int value = getFoodValue(offhand);
            if (value > bestValue) {
                return 40; // Special slot for offhand
            }
        }

        return bestSlot;
    }

    private int getFoodValue(ItemStack stack) {
        if (stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) return 100;
        if (stack.isOf(Items.GOLDEN_APPLE)) return preferGoldenApples.getValue() ? 90 : 50;
        if (stack.isOf(Items.GOLDEN_CARROT)) return 40;
        if (stack.isOf(Items.COOKED_BEEF) || stack.isOf(Items.COOKED_PORKCHOP) || stack.isOf(Items.COOKED_MUTTON) || stack.isOf(Items.COOKED_SALMON) || stack.isOf(Items.COOKED_COD)) return 30;
        if (stack.isOf(Items.BREAD)) return 20;
        if (stack.isOf(Items.COOKED_CHICKEN) || stack.isOf(Items.COOKED_RABBIT)) return 15;
        if (stack.isOf(Items.APPLE) || stack.isOf(Items.CARROT) || stack.isOf(Items.POTATO) || stack.isOf(Items.BEETROOT)) return 10;
        if (stack.isOf(Items.ROTTEN_FLESH)) return 5;
        return 0;
    }

    public int getHealthThreshold() {
        return healthThreshold.getValue();
    }

    public boolean preferGoldenApples() {
        return preferGoldenApples.getValue();
    }
}