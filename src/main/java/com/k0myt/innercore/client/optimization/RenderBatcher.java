package com.k0myt.innercore.client.optimization;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import java.util.*;

/**
 * Batches block rendering to reduce draw calls
 */
public class RenderBatcher {
    private static final int BATCH_SIZE = 64; // Render up to 64 blocks per batch

    public static class RenderBatch {
        public final List<BlockState> states = new ArrayList<>();
        public final List<BlockPos> positions = new ArrayList<>();
        public final List<Integer> lights = new ArrayList<>();
        public final List<Integer> overlays = new ArrayList<>();

        public boolean canAdd() {
            return states.size() < BATCH_SIZE;
        }

        public void add(BlockState state, BlockPos pos, int light, int overlay) {
            states.add(state);
            positions.add(pos);
            lights.add(light);
            overlays.add(overlay);
        }

        public void clear() {
            states.clear();
            positions.clear();
            lights.clear();
            overlays.clear();
        }

        public int size() {
            return states.size();
        }
    }

    /**
     * Create render batches from block list
     */
    public static List<RenderBatch> createBatches(List<BlockState> blockStates, List<BlockPos> positions, List<Integer> lights) {
        List<RenderBatch> batches = new ArrayList<>();
        RenderBatch currentBatch = new RenderBatch();

        for (int i = 0; i < blockStates.size(); i++) {
            if (!currentBatch.canAdd()) {
                batches.add(currentBatch);
                currentBatch = new RenderBatch();
            }
            currentBatch.add(blockStates.get(i), positions.get(i), lights.get(i), 0);
        }

        if (currentBatch.size() > 0) {
            batches.add(currentBatch);
        }

        return batches;
    }
}
