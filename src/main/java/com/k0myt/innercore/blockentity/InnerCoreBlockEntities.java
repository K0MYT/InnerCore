package com.k0myt.innercore.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.Registries;

public class InnerCoreBlockEntities {
    public static final DeferredRegister<BlockEntity> BLOCK_ENTITIES = 
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "innercore");

    public static final DeferredHolder<BlockEntity, BlockEntity> INNER_CORE_PLATE =
            BLOCK_ENTITIES.register("inner_core_plate", () ->
                    net.minecraft.world.level.block.entity.BlockEntityType.Builder
                    .of(InnerCorePlateBlockEntity::new, 
                        com.k0myt.innercore.block.InnerCoreBlocks.INNER_CORE_PLATE.get(),
                        com.k0myt.innercore.block.InnerCoreBlocks.INNER_CORE_PLATE_ACTIVATED.get())
                    .build(null));
}
