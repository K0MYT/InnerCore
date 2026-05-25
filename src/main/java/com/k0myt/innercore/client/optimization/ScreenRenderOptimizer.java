package com.k0myt.innercore.client.optimization;

import net.neoforged.neoforge.client.event.ScreenEvent;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

/**
 * Optimizes client-side screen rendering
 */
public class ScreenRenderOptimizer {
    private static long lastRenderTime = 0;
    private static final long RENDER_INTERVAL = 16; // 16ms = 60 FPS

    /**
     * Check if screen should render this frame
     */
    public static boolean shouldRender() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRenderTime >= RENDER_INTERVAL) {
            lastRenderTime = currentTime;
            return true;
        }
        return false;
    }

    /**
     * Cull invisible slots from rendering
     */
    public static boolean shouldRenderSlot(AbstractContainerScreen<?> screen, int slotIndex) {
        // Calculate if slot is within visible bounds
        int slotsPerRow = 18;
        int slotY = (slotIndex / slotsPerRow);
        
        // Only render if slot is within visible area
        return slotY >= 0 && slotY < 12; // 12 rows visible
    }
}
