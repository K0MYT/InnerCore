package com.k0myt.innercore.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

import com.k0myt.innercore.blockentity.InnerCorePlateBlockEntity;

public class InnerCoreContainerMenu extends AbstractContainerMenu {
    public static final int CONTAINER_SLOTS = 216; // 6x6x6
    public static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    public static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    public static final int HOTBAR_SLOT_COUNT = 9;

    private final SimpleContainer containerInventory;
    private final InnerCorePlateBlockEntity blockEntity;
    private final BlockPos blockPos;

    public InnerCoreContainerMenu(int windowId, Inventory playerInventory, BlockPos blockPos, InnerCorePlateBlockEntity blockEntity) {
        super(InnerCoreMenuTypes.INNER_CORE_CONTAINER.get(), windowId);
        
        this.blockPos = blockPos;
        this.blockEntity = blockEntity;
        this.containerInventory = blockEntity.getInventory();

        // Add container slots (6x6x6 grid = 216 slots)
        for (int i = 0; i < CONTAINER_SLOTS; i++) {
            int row = (i / 18) % 12; // 18 slots per row, 12 rows
            int col = i % 18;
            this.addSlot(new InnerCoreSlot(containerInventory, i, 8 + col * 9, 18 + row * 18));
        }

        // Add player inventory slots
        addPlayerInventory(playerInventory);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        // Main inventory
        for (int row = 0; row < PLAYER_INVENTORY_ROW_COUNT; row++) {
            for (int col = 0; col < PLAYER_INVENTORY_COLUMN_COUNT; col++) {
                this.addSlot(new Slot(
                    playerInventory,
                    col + row * 9 + 9,
                    8 + col * 18,
                    228 + row * 18
                ));
            }
        }

        // Hotbar
        for (int col = 0; col < HOTBAR_SLOT_COUNT; col++) {
            this.addSlot(new Slot(
                playerInventory,
                col,
                8 + col * 18,
                286
            ));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (pIndex < CONTAINER_SLOTS) {
                if (!this.moveItemStackTo(itemstack1, CONTAINER_SLOTS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, CONTAINER_SLOTS, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true; // Always valid for now
    }

    public InnerCorePlateBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }
}
