package dev.mvxmenu.module.impl;

import dev.mvxmenu.module.Module;
import dev.mvxmenu.module.BooleanSetting;
import dev.mvxmenu.module.IntegerSetting;
import dev.mvxmenu.module.ModuleId;
import dev.mvxmenu.module.ModuleMetadata;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Collections;

public class XRayModule extends Module {

    private final BooleanSetting oresSetting;
    private final BooleanSetting chestsSetting;
    private final IntegerSetting rangeSetting;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private static final Set<Block> XRAY_BLOCKS = new HashSet<>();

    static {
        XRAY_BLOCKS.add(Blocks.COAL_ORE);
        XRAY_BLOCKS.add(Blocks.DEEPSLATE_COAL_ORE);
        XRAY_BLOCKS.add(Blocks.IRON_ORE);
        XRAY_BLOCKS.add(Blocks.DEEPSLATE_IRON_ORE);
        XRAY_BLOCKS.add(Blocks.COPPER_ORE);
        XRAY_BLOCKS.add(Blocks.DEEPSLATE_COPPER_ORE);
        XRAY_BLOCKS.add(Blocks.GOLD_ORE);
        XRAY_BLOCKS.add(Blocks.DEEPSLATE_GOLD_ORE);
        XRAY_BLOCKS.add(Blocks.REDSTONE_ORE);
        XRAY_BLOCKS.add(Blocks.DEEPSLATE_REDSTONE_ORE);
        XRAY_BLOCKS.add(Blocks.DIAMOND_ORE);
        XRAY_BLOCKS.add(Blocks.DEEPSLATE_DIAMOND_ORE);
        XRAY_BLOCKS.add(Blocks.EMERALD_ORE);
        XRAY_BLOCKS.add(Blocks.DEEPSLATE_EMERALD_ORE);
        XRAY_BLOCKS.add(Blocks.LAPIS_ORE);
        XRAY_BLOCKS.add(Blocks.DEEPSLATE_LAPIS_ORE);
        XRAY_BLOCKS.add(Blocks.NETHER_GOLD_ORE);
        XRAY_BLOCKS.add(Blocks.NETHER_QUARTZ_ORE);
        XRAY_BLOCKS.add(Blocks.ANCIENT_DEBRIS);
        XRAY_BLOCKS.add(Blocks.CHEST);
        XRAY_BLOCKS.add(Blocks.TRAPPED_CHEST);
        XRAY_BLOCKS.add(Blocks.ENDER_CHEST);
        XRAY_BLOCKS.add(Blocks.BARREL);
        XRAY_BLOCKS.add(Blocks.SHULKER_BOX);
    }

    public XRayModule() {
        super(ModuleMetadata.builder()
                .id(ModuleId.of("xray"))
                .displayName("X-Ray")
                .description("See ores through blocks")
                .category(Module.Category.RENDER)
                .version("1.0.0")
                .dependencies(Set.of())
                .softDependencies(Set.of())
                .authors(List.of("MVXmenu"))
                .homepage("https://github.com/mvxmenu/mvxmenu")
                .build());
        this.oresSetting = registerSetting(new BooleanSetting("ores", "Ores", "Highlight ores", true));
        this.chestsSetting = registerSetting(new BooleanSetting("chests", "Chests", "Highlight chests", true));
        this.rangeSetting = registerSetting(new IntegerSetting("range", "Range", "X-ray range in blocks", 50, 10, 100));
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onTick() {
    }

    public void renderXRay(ClientWorld world, net.minecraft.client.render.VertexConsumerProvider providers, double cameraX, double cameraY, double cameraZ) {
        if (mc.player == null) return;

        int range = rangeSetting.getValue();
        BlockPos playerPos = mc.player.getBlockPos();

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    BlockState state = world.getBlockState(pos);
                    Block block = state.getBlock();

                    if (!XRAY_BLOCKS.contains(block)) continue;

                    boolean isOre = isOre(block);
                    boolean isChest = isChest(block);

                    if ((isOre && oresSetting.getValue()) || (isChest && chestsSetting.getValue())) {
                        renderBlockBox(pos, isOre ? 0xFFFF0000 : 0xFFFFFF00, cameraX, cameraY, cameraZ);
                    }
                }
            }
        }
    }

    private boolean isOre(Block block) {
        return block == Blocks.COAL_ORE || block == Blocks.DEEPSLATE_COAL_ORE ||
               block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE ||
               block == Blocks.COPPER_ORE || block == Blocks.DEEPSLATE_COPPER_ORE ||
               block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE ||
               block == Blocks.REDSTONE_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE ||
               block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE ||
               block == Blocks.EMERALD_ORE || block == Blocks.DEEPSLATE_EMERALD_ORE ||
               block == Blocks.LAPIS_ORE || block == Blocks.DEEPSLATE_LAPIS_ORE ||
               block == Blocks.NETHER_GOLD_ORE || block == Blocks.NETHER_QUARTZ_ORE ||
               block == Blocks.ANCIENT_DEBRIS;
    }

    private boolean isChest(Block block) {
        return block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST ||
               block == Blocks.ENDER_CHEST || block == Blocks.BARREL ||
               block.getName().getString().contains("SHULKER_BOX");
    }

    private void renderBlockBox(BlockPos pos, int color, double cameraX, double cameraY, double cameraZ) {}

    public boolean isOres() {
        return oresSetting.getValue();
    }

    public boolean isChests() {
        return chestsSetting.getValue();
    }

    public int getRange() {
        return rangeSetting.getValue();
    }
}