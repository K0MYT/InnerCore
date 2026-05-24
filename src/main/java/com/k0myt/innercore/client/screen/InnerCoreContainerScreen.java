package com.k0myt.innercore.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.resources.ResourceLocation;

import com.k0myt.innercore.InnerCoreMod;
import com.k0myt.innercore.menu.InnerCoreContainerMenu;

public class InnerCoreContainerScreen extends AbstractContainerScreen<InnerCoreContainerMenu> {
    private static final ResourceLocation TEXTURE = 
            new ResourceLocation(InnerCoreMod.MOD_ID, "textures/gui/inner_core_container.png");

    public InnerCoreContainerScreen(InnerCoreContainerMenu pMenu, Inventory pPlayerInventory, net.minecraft.network.chat.Component pComponent) {
        super(pMenu, pPlayerInventory, pComponent);
        this.imageWidth = 256;
        this.imageHeight = 308;
    }

    @Override
    protected void init() {
        super.init();
        // Center the inventory screen
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBg(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindForSetup(TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;

        this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
