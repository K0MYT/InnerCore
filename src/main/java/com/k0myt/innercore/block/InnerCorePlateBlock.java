package com.k0myt.innercore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import com.k0myt.innercore.blockentity.InnerCorePlateBlockEntity;

public class InnerCorePlateBlock extends Block implements EntityBlock {

    public InnerCorePlateBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new InnerCorePlateBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        InnerCorePlateBlockEntity blockEntity = (InnerCorePlateBlockEntity) level.getBlockEntity(pos);
        if (blockEntity == null) {
            return InteractionResult.FAIL;
        }

        ItemStack itemInHand = player.getMainHandItem();

        // Shift + Left Click = Remove block
        if (player.isShiftKeyDown() && !itemInHand.isEmpty() && !(itemInHand.getItem() instanceof BlockItem)) {
            BlockPos gridPos = blockEntity.getClosestBlockAt(hit.getLocation());
            if (gridPos != null && blockEntity.hasBlockAt(gridPos)) {
                ItemStack extracted = blockEntity.removeBlock(gridPos);
                if (!extracted.isEmpty()) {
                    ItemEntity itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, extracted);
                    level.addFreshEntity(itemEntity);
                    blockEntity.setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
        }

        // Right Click with Block Item = Place block
        if (!player.isShiftKeyDown() && itemInHand.getItem() instanceof BlockItem blockItem) {
            BlockPos gridPos = blockEntity.getClosestEmptySlot(hit.getLocation());
            if (gridPos != null && blockEntity.canPlaceBlockAt(gridPos, blockItem.getBlock().defaultBlockState())) {
                ItemStack copy = itemInHand.copy();
                copy.setCount(1);
                
                // Get NBT data if available
                CompoundTag blockTag = copy.getTagElement("BlockEntityTag");
                blockEntity.addBlock(gridPos, blockItem.getBlock().defaultBlockState(), blockTag);
                
                itemInHand.shrink(1);
                blockEntity.setChanged();
                return InteractionResult.SUCCESS;
            }
        }

        // Right Click empty hand = Open GUI
        if (itemInHand.isEmpty() && !player.isShiftKeyDown()) {
            if (player.hasContainerOpen()) {
                player.closeContainer();
                return InteractionResult.SUCCESS;
            }
            blockEntity.openMenu(player);
            return InteractionResult.SUCCESS;
        }

        // Shift + Right Click = Place on interactive blocks
        if (player.isShiftKeyDown() && itemInHand.getItem() instanceof BlockItem blockItem) {
            BlockPos gridPos = blockEntity.getClosestSlotForInteractive(hit.getLocation());
            if (gridPos != null && blockEntity.canPlaceBlockAt(gridPos, blockItem.getBlock().defaultBlockState())) {
                ItemStack copy = itemInHand.copy();
                copy.setCount(1);
                
                CompoundTag blockTag = copy.getTagElement("BlockEntityTag");
                blockEntity.addBlock(gridPos, blockItem.getBlock().defaultBlockState(), blockTag);
                
                itemInHand.shrink(1);
                blockEntity.setChanged();
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            InnerCorePlateBlockEntity blockEntity = (InnerCorePlateBlockEntity) level.getBlockEntity(pos);
            if (blockEntity != null) {
                blockEntity.dropAllBlocks(level, pos);
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }
}
