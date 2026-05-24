package com.k0myt.innercore.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.core.BlockPos;

import com.k0myt.innercore.blockentity.InnerCorePlateBlockEntity;

/**
 * Custom TESR for rendering blocks stored in InnerCorePlate
 */
public class InnerCorePlateBlockEntityRenderer implements BlockEntityRenderer<InnerCorePlateBlockEntity> {
    
    private final BlockRenderDispatcher blockRenderer;

    public InnerCorePlateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderer = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(InnerCorePlateBlockEntity blockEntity, float partialTick, PoseStack poseStack, 
                      net.minecraft.client.renderer.MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        
        poseStack.pushPose();
        
        // Render each stored block in the grid
        for (var entry : blockEntity.getStoredBlocks().entrySet()) {
            BlockPos gridPos = entry.getKey();
            InnerCorePlateBlockEntity.StoredBlock stored = entry.getValue();

            poseStack.pushPose();
            
            // Translate to grid position (each block is 1/6th of the plate)
            float posX = gridPos.getX() / 6.0f;
            float posY = gridPos.getY() / 6.0f;
            float posZ = gridPos.getZ() / 6.0f;
            
            poseStack.translate(posX, posY, posZ);
            
            // Scale down to fit in grid (0.9f for spacing)
            poseStack.scale(0.9f, 0.9f, 0.9f);
            
            // Render the block
            try {
                this.blockRenderer.renderSingleBlock(stored.state, poseStack, bufferSource, packedLight, packedOverlay);
            } catch (Exception e) {
                // Fallback if block rendering fails
                this.blockRenderer.renderSingleBlock(Blocks.STONE.defaultBlockState(), poseStack, bufferSource, packedLight, packedOverlay);
            }
            
            poseStack.popPose();
        }
        
        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256; // Render from far away
    }
}
