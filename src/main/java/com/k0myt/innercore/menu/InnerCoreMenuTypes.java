package com.k0myt.innercore.menu;

import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.IContainerFactory;
import net.minecraft.core.BlockPos;

import com.k0myt.innercore.InnerCoreMod;
import com.k0myt.innercore.blockentity.InnerCorePlateBlockEntity;

public class InnerCoreMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = 
            DeferredRegister.create(Registries.MENU, InnerCoreMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<InnerCoreContainerMenu>> INNER_CORE_CONTAINER =
            MENU_TYPES.register("inner_core_container", () ->
                    new MenuType<>((IContainerFactory<InnerCoreContainerMenu>) (windowId, playerInventory, data) -> {
                        BlockPos pos = data.readBlockPos();
                        InnerCorePlateBlockEntity blockEntity = (InnerCorePlateBlockEntity) playerInventory.player.level().getBlockEntity(pos);
                        return new InnerCoreContainerMenu(windowId, playerInventory, pos, blockEntity);
                    }));
}
