package com.k0myt.innercore.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import com.k0myt.innercore.InnerCoreMod;
import com.k0myt.innercore.menu.InnerCoreContainerMenu;

public class InnerCoreContainerScreen extends AbstractContainerScreen<InnerCoreContainerMenu> {
    private static final ResourceLocation TEXTURE = 
            new ResourceLocation(InnerCoreMod.MOD_ID, "textures/gui/inner_core_container.png");
    private static final int GRID_WIDTH = 18;
    private static final int GRID_HEIGHT = 12;
    private static final int SLOT_SIZE = 9;

    private int scrollX = 0;
    private int scrollY = 0;

    public InnerCoreContainerScreen(InnerCoreContainerMenu pMenu, Inventory pPlayerInventory, Component pComponent) {
        super(pMenu, pPlayerInventory, pComponent);
        this.imageWidth = 256;
        this.imageHeight = 308;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBg(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
        
        // Draw grid info
        this.font.draw(pPoseStack, "InnerCore Plate (6x6x6 Grid)", 
                this.leftPos + 8, this.topPos + 8, 0xFFFFFF);
        this.font.draw(pPoseStack, "Blocks: " + this.menu.getBlockEntity().getStoredBlocks().size() + "/216",
                this.leftPos + 8, this.topPos + 18, 0xAAAAAA);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        
        // Draw background
        fill(pPoseStack, this.leftPos, this.topPos, 
             this.leftPos + this.imageWidth, this.topPos + this.imageHeight, 0xFF8B8B8B);
        
        // Draw container title area
        fill(pPoseStack, this.leftPos + 5, this.topPos + 5, 
             this.leftPos + this.imageWidth - 5, this.topPos + 30, 0xFF3C3C3C);
        
        // Draw grid area
        fill(pPoseStack, this.leftPos + 5, this.topPos + 35, 
             this.leftPos + this.imageWidth - 5, this.topPos + 210, 0xFF1C1C1C);
        
        // Draw slots grid
        drawSlotsGrid(pPoseStack);
    }

    private void drawSlotsGrid(PoseStack pPoseStack) {
        int startX = this.leftPos + 8;
        int startY = this.topPos + 38;
        
        // Draw 18x12 grid (216 slots)
        for (int x = 0; x < GRID_WIDTH; x++) {
            for (int y = 0; y < GRID_HEIGHT; y++) {
                int slotX = startX + x * SLOT_SIZE;
                int slotY = startY + y * SLOT_SIZE;
                
                fill(pPoseStack, slotX, slotY, slotX + SLOT_SIZE - 1, slotY + SLOT_SIZE - 1, 0xFF2C2C2C);
                GuiComponent.drawRect(pPoseStack, slotX, slotY, slotX + SLOT_SIZE - 1, slotY + SLOT_SIZE - 1, 0xFF4C4C4C);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY) {
        // Handle scrolling for grid visualization
        return super.mouseScrolled(pMouseX, pMouseY, pScrollX, pScrollY);
    }
}
