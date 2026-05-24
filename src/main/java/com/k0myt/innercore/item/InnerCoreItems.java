package com.k0myt.innercore.item;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import com.k0myt.innercore.block.InnerCoreBlocks;

public class InnerCoreItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("innercore");

    public static final DeferredItem<Item> INNER_CORE_PLATE = ITEMS.register("inner_core_plate",
            () -> new BlockItem(InnerCoreBlocks.INNER_CORE_PLATE.get(), new Item.Properties()));

    public static final DeferredItem<Item> INNER_CORE_PLATE_ACTIVATED = ITEMS.register("inner_core_plate_activated",
            () -> new BlockItem(InnerCoreBlocks.INNER_CORE_PLATE_ACTIVATED.get(), new Item.Properties()));

    public static void addCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(INNER_CORE_PLATE);
            event.accept(INNER_CORE_PLATE_ACTIVATED);
        }
    }
}
