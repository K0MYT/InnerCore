package com.k0myt.innercore.menu;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;

public class InnerCoreSlot extends Slot {
    public InnerCoreSlot(Container container, int index, int x, int y) {
        super(container, index, x, y);
    }

    @Override
    public boolean mayPlace(ItemStack pStack) {
        // Only allow block items
        return pStack.getItem() instanceof BlockItem;
    }

    @Override
    public int getMaxStackSize() {
        return 1; // One block per slot
    }
}
