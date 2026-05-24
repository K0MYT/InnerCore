package com.k0myt.innercore.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class InnerCoreBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks("innercore");

    public static final DeferredBlock<Block> INNER_CORE_PLATE = BLOCKS.register("inner_core_plate",
            () -> new InnerCorePlateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noCollission()
                    .noOcclusion()));

    public static final DeferredBlock<Block> INNER_CORE_PLATE_ACTIVATED = BLOCKS.register("inner_core_plate_activated",
            () -> new InnerCorePlateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .noCollission()
                    .noOcclusion()
                    .lightLevel(state -> 7)));
}
