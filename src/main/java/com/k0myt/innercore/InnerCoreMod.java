package com.k0myt.innercore;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import com.k0myt.innercore.block.InnerCoreBlocks;
import com.k0myt.innercore.blockentity.InnerCoreBlockEntities;
import com.k0myt.innercore.item.InnerCoreItems;
import com.k0myt.innercore.menu.InnerCoreMenus;

@Mod(InnerCoreMod.MOD_ID)
public class InnerCoreMod {
    public static final String MOD_ID = "innercore";

    public InnerCoreMod(ModContainer modContainer, IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::buildCreativeModeTabs);

        InnerCoreBlocks.BLOCKS.register(modEventBus);
        InnerCoreItems.ITEMS.register(modEventBus);
        InnerCoreBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        InnerCoreMenus.MENUS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Common setup
        });
    }

    private void buildCreativeModeTabs(final BuildCreativeModeTabContentsEvent event) {
        // Add items to creative tabs
    }
}
