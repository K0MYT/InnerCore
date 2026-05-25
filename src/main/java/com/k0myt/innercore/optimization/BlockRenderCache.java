package com.k0myt.innercore.optimization;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import java.util.HashMap;
import java.util.Map;

/**
 * Caches block rendering data to avoid repeated lookups
 */
public class BlockRenderCache {
    private static final Map<Block, BlockRenderData> CACHE = new HashMap<>();
    private static final int MAX_CACHE_SIZE = 512;

    public static class BlockRenderData {
        public boolean isOpaqueBlock;
        public boolean hasSpecialModel;
        public int lightLevel;

        public BlockRenderData(boolean opaque, boolean special, int light) {
            this.isOpaqueBlock = opaque;
            this.hasSpecialModel = special;
            this.lightLevel = light;
        }
    }

    public static BlockRenderData getCachedData(Block block) {
        return CACHE.computeIfAbsent(block, b -> {
            if (CACHE.size() >= MAX_CACHE_SIZE) {
                CACHE.clear(); // Clear cache if too large
            }
            
            BlockState state = block.defaultBlockState();
            boolean opaque = !state.getMaterial().isReplaceable();
            boolean special = state.getRenderShape() != net.minecraft.world.level.block.RenderShape.MODEL;
            int light = state.getLightEmission();
            
            return new BlockRenderData(opaque, special, light);
        });
    }

    public static void clearCache() {
        CACHE.clear();
    }
}
