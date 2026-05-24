package com.k0myt.innercore.blockentity;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.network.NetworkHooks;

import com.k0myt.innercore.block.InnerCorePlateBlock;
import com.k0myt.innercore.menu.InnerCoreContainerMenu;

public class InnerCorePlateBlockEntity extends BlockEntity {
    public static final int GRID_SIZE = 6;
    public static final int MAX_BLOCKS = GRID_SIZE * GRID_SIZE * GRID_SIZE;
    
    private static final String TAG_BLOCKS = "blocks";
    private static final String TAG_BLOCK_STATE = "state";
    private static final String TAG_BLOCK_DATA = "data";
    private static final String TAG_X = "x";
    private static final String TAG_Y = "y";
    private static final String TAG_Z = "z";

    private final Map<BlockPos, StoredBlock> storedBlocks = new HashMap<>();
    private SimpleContainer inventory;

    public InnerCorePlateBlockEntity(BlockPos pos, BlockState state) {
        super(InnerCoreBlockEntities.INNER_CORE_PLATE.get(), pos, state);
        this.inventory = new SimpleContainer(MAX_BLOCKS);
    }

    /**
     * Stored block wrapper class
     */
    public static class StoredBlock {
        public BlockState state;
        public CompoundTag data;

        public StoredBlock(BlockState state, CompoundTag data) {
            this.state = state;
            this.data = data != null ? data.copy() : new CompoundTag();
        }

        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();
            tag.put(TAG_BLOCK_STATE, NbtUtils.writeBlockState(state));
            tag.put(TAG_BLOCK_DATA, data);
            return tag;
        }

        public static StoredBlock load(CompoundTag tag) {
            BlockState state = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asHolderIdMap(), tag.getCompound(TAG_BLOCK_STATE));
            CompoundTag data = tag.getCompound(TAG_BLOCK_DATA);
            return new StoredBlock(state, data);
        }
    }

    /**
     * Add a block to the plate at the given grid position
     */
    public boolean addBlock(BlockPos gridPos, BlockState state, CompoundTag data) {
        if (!isValidGridPos(gridPos) || storedBlocks.containsKey(gridPos)) {
            return false;
        }

        if (storedBlocks.size() >= MAX_BLOCKS) {
            return false;
        }

        storedBlocks.put(gridPos, new StoredBlock(state, data));
        updateInventory();
        return true;
    }

    /**
     * Remove a block from the plate
     */
    public ItemStack removeBlock(BlockPos gridPos) {
        StoredBlock stored = storedBlocks.remove(gridPos);
        if (stored != null) {
            updateInventory();
            return new ItemStack(stored.state.getBlock());
        }
        return ItemStack.EMPTY;
    }

    /**
     * Check if there's a block at this position
     */
    public boolean hasBlockAt(BlockPos gridPos) {
        return storedBlocks.containsKey(gridPos);
    }

    /**
     * Get the stored block at position
     */
    public StoredBlock getBlockAt(BlockPos gridPos) {
        return storedBlocks.get(gridPos);
    }

    /**
     * Check if can place block at position
     */
    public boolean canPlaceBlockAt(BlockPos gridPos, BlockState state) {
        if (!isValidGridPos(gridPos)) return false;
        if (storedBlocks.containsKey(gridPos)) return false;
        if (state.getBlock() instanceof InnerCorePlateBlock) return false; // No nested plates
        return true;
    }

    /**
     * Get closest empty slot based on hit position
     */
    public BlockPos getClosestEmptySlot(Vec3 hitPos) {
        // Convert world hit position to grid coordinates
        BlockPos blockPos = this.getBlockPos();
        double relX = hitPos.x - blockPos.getX();
        double relY = hitPos.y - blockPos.getY();
        double relZ = hitPos.z - blockPos.getZ();

        // Find closest empty slot
        double minDist = Double.MAX_VALUE;
        BlockPos closest = null;

        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                for (int z = 0; z < GRID_SIZE; z++) {
                    BlockPos gridPos = new BlockPos(x, y, z);
                    if (!storedBlocks.containsKey(gridPos)) {
                        double cx = x + 0.5;
                        double cy = y + 0.5;
                        double cz = z + 0.5;
                        double dist = Math.sqrt((relX - cx) * (relX - cx) + (relY - cy) * (relY - cy) + (relZ - cz) * (relZ - cz));
                        if (dist < minDist) {
                            minDist = dist;
                            closest = gridPos;
                        }
                    }
                }
            }
        }
        return closest;
    }

    /**
     * Get closest existing block based on hit position
     */
    public BlockPos getClosestBlockAt(Vec3 hitPos) {
        BlockPos blockPos = this.getBlockPos();
        double relX = hitPos.x - blockPos.getX();
        double relY = hitPos.y - blockPos.getY();
        double relZ = hitPos.z - blockPos.getZ();

        double minDist = Double.MAX_VALUE;
        BlockPos closest = null;

        for (BlockPos gridPos : storedBlocks.keySet()) {
            double cx = gridPos.getX() + 0.5;
            double cy = gridPos.getY() + 0.5;
            double cz = gridPos.getZ() + 0.5;
            double dist = Math.sqrt((relX - cx) * (relX - cx) + (relY - cy) * (relY - cy) + (relZ - cz) * (relZ - cz));
            if (dist < minDist) {
                minDist = dist;
                closest = gridPos;
            }
        }
        return closest;
    }

    /**
     * Get closest slot for interactive blocks
     */
    public BlockPos getClosestSlotForInteractive(Vec3 hitPos) {
        return getClosestEmptySlot(hitPos); // Same as empty slot for now
    }

    /**
     * Drop all blocks when plate is destroyed
     */
    public void dropAllBlocks(Level level, BlockPos pos) {
        for (StoredBlock stored : storedBlocks.values()) {
            ItemEntity itemEntity = new ItemEntity(level, 
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    new ItemStack(stored.state.getBlock()));
            level.addFreshEntity(itemEntity);
        }
        storedBlocks.clear();
    }

    /**
     * Open the container menu for the player
     */
    public void openMenu(Player player) {
        if (level != null && !level.isClientSide) {
            NetworkHooks.openScreen((net.minecraft.server.level.ServerPlayer) player, 
                new net.minecraft.world.SimpleMenuProvider(
                    (windowId, playerInventory, playerEntity) -> 
                        new InnerCoreContainerMenu(windowId, playerInventory, this.getBlockPos(), this),
                    net.minecraft.network.chat.Component.literal("InnerCore Plate")
                ));
        }
    }

    /**
     * Update inventory representation
     */
    private void updateInventory() {
        inventory = new SimpleContainer(MAX_BLOCKS);
        int index = 0;
        for (StoredBlock stored : storedBlocks.values()) {
            if (index < MAX_BLOCKS) {
                ItemStack stack = new ItemStack(stored.state.getBlock());
                inventory.setItem(index, stack);
                index++;
            }
        }
    }

    /**
     * Validate grid position
     */
    private boolean isValidGridPos(BlockPos pos) {
        return pos.getX() >= 0 && pos.getX() < GRID_SIZE &&
               pos.getY() >= 0 && pos.getY() < GRID_SIZE &&
               pos.getZ() >= 0 && pos.getZ() < GRID_SIZE;
    }

    /**
     * NBT save/load
     */
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag blocksList = new ListTag();

        for (Map.Entry<BlockPos, StoredBlock> entry : storedBlocks.entrySet()) {
            CompoundTag blockTag = entry.getValue().save();
            blockTag.putInt(TAG_X, entry.getKey().getX());
            blockTag.putInt(TAG_Y, entry.getKey().getY());
            blockTag.putInt(TAG_Z, entry.getKey().getZ());
            blocksList.add(blockTag);
        }

        tag.put(TAG_BLOCKS, blocksList);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        storedBlocks.clear();

        ListTag blocksList = tag.getList(TAG_BLOCKS, Tag.TAG_COMPOUND);
        for (int i = 0; i < blocksList.size(); i++) {
            CompoundTag blockTag = blocksList.getCompound(i);
            int x = blockTag.getInt(TAG_X);
            int y = blockTag.getInt(TAG_Y);
            int z = blockTag.getInt(TAG_Z);
            StoredBlock stored = StoredBlock.load(blockTag);
            storedBlocks.put(new BlockPos(x, y, z), stored);
        }

        updateInventory();
    }

    public Map<BlockPos, StoredBlock> getStoredBlocks() {
        return storedBlocks;
    }

    public SimpleContainer getInventory() {
        return inventory;
    }
}
