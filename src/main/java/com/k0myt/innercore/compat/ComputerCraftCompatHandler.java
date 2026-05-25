package com.k0myt.innercore.compat;

import net.minecraft.world.level.block.state.BlockState;

/**
 * Compatibility handler for ComputerCraft: Tweaked integration
 * Handles modem signal transmission
 */
public class ComputerCraftCompatHandler {
    
    /**
     * Check if block is a CC:T modem
     */
    public static boolean isCCModem(BlockState state) {
        String blockName = state.getBlock().getName().toString();
        return blockName.contains("modem") && blockName.contains("computercraft");
    }

    /**
     * Check if block is a CC:T computer
     */
    public static boolean isCCComputer(BlockState state) {
        String blockName = state.getBlock().getName().toString();
        return (blockName.contains("computer") || blockName.contains("turtle")) && 
               blockName.contains("computercraft");
    }

    /**
     * Check if modem can transmit signals through plate
     */
    public static boolean canTransmitSignal(BlockState state) {
        return isCCModem(state) || isCCComputer(state);
    }
}
