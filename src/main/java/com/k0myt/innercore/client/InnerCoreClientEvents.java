package com.k0myt.innercore.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import com.k0myt.innercore.InnerCoreMod;
import com.k0myt.innercore.blockentity.InnerCoreBlockEntities;
import com.k0myt.innercore.client.renderer.InnerCorePlateBlockEntityRenderer;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = InnerCoreMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class InnerCoreClientEvents {

    @net.neoforged.neoforge.api.distmarker.OnlyIn(Dist.CLIENT)
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(InnerCoreBlockEntities.INNER_CORE_PLATE.get(),
                InnerCorePlateBlockEntityRenderer::new);
    }
}
