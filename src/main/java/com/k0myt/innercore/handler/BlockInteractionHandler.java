package com.k0myt.innercore.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import com.k0myt.innercore.blockentity.InnerCorePlateBlockEntity;
import com.k0myt.innercore.network.BlockPlacedPacket;

public class BlockInteractionHandler {
    /**
     * Handle block placement on inner core plate
     */
    public static void onBlockPlace(Player player, BlockPos platePos, ItemStack itemStack) {
        Level level = player.level();
        if (level == null || level.isClientSide) return;

        BlockEntity be = level.getBlockEntity(platePos);
        if (!(be instanceof InnerCorePlateBlockEntity blockEntity)) return;

        if (!(itemStack.getItem() instanceof BlockItem blockItem)) return;

        // Find closest empty slot and add block
        BlockPos gridPos = blockEntity.getClosestEmptySlot(player.getEyePosition());
        if (gridPos != null) {
            blockEntity.addBlock(gridPos, blockItem.getBlock().defaultBlockState(), null);
            blockEntity.setChanged();
            
            // Send packet for visual feedback
            // TODO: Send network packet with BlockPlacedPacket
            
            itemStack.shrink(1);
        }
    }

    /**
     * Handle block removal from inner core plate
     */
    public static void onBlockRemove(Player player, BlockPos platePos, BlockPos gridPos) {
        Level level = player.level();
        if (level == null || level.isClientSide) return;

        BlockEntity be = level.getBlockEntity(platePos);
        if (!(be instanceof InnerCorePlateBlockEntity blockEntity)) return;

        ItemStack removed = blockEntity.removeBlock(gridPos);
        if (!removed.isEmpty()) {
            blockEntity.setChanged();
            
            // Give item to player or drop it
            if (!player.addItem(removed)) {
                player.drop(removed, false);
            }
            
            // Send packet for visual feedback
            // TODO: Send network packet with BlockPlacedPacket
        }
    }

    /**
     * Get signal strength from blocks inside plate
     * (For redstone compatibility)
     */
    public static int getSignalStrength(InnerCorePlateBlockEntity blockEntity, BlockPos gridPos) {
        InnerCorePlateBlockEntity.StoredBlock stored = blockEntity.getBlockAt(gridPos);
        if (stored == null) return 0;
        
        // Check if block provides redstone signal
        // TODO: Implement signal strength detection
        return 0;
    }

    /**
     * Check if block receives redstone signal
     */
    public static boolean shouldReceiveRedstone(InnerCorePlateBlockEntity blockEntity, BlockPos gridPos) {
        InnerCorePlateBlockEntity.StoredBlock stored = blockEntity.getBlockAt(gridPos);
        if (stored == null) return false;
        
        // Check if block reacts to redstone
        // TODO: Implement redstone reaction detection
        return true;
    }
}
