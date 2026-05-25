package com.k0myt.innercore.optimization;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import java.util.*;

/**
 * Optimized spatial indexing for quick block lookup
 */
public class SpatialIndex {
    private static final int GRID_CELL_SIZE = 2; // 2x2x2 cells per plate cell

    public static class GridCell {
        private final BlockPos min;
        private final BlockPos max;
        private final List<BlockPos> blocks = new ArrayList<>();

        public GridCell(BlockPos min, BlockPos max) {
            this.min = min;
            this.max = max;
        }

        public void addBlock(BlockPos pos) {
            if (isInBounds(pos)) {
                blocks.add(pos);
            }
        }

        public boolean isInBounds(BlockPos pos) {
            return pos.getX() >= min.getX() && pos.getX() <= max.getX() &&
                   pos.getY() >= min.getY() && pos.getY() <= max.getY() &&
                   pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ();
        }

        public List<BlockPos> getBlocks() {
            return blocks;
        }
    }

    private final Map<Integer, GridCell> cells = new HashMap<>();

    public void insert(BlockPos pos) {
        int cellKey = getCellKey(pos);
        GridCell cell = cells.computeIfAbsent(cellKey, k -> createCell(pos));
        cell.addBlock(pos);
    }

    public List<BlockPos> queryRange(BlockPos min, BlockPos max) {
        List<BlockPos> result = new ArrayList<>();
        for (GridCell cell : cells.values()) {
            if (cell.min.getX() <= max.getX() && cell.max.getX() >= min.getX() &&
                cell.min.getY() <= max.getY() && cell.max.getY() >= min.getY() &&
                cell.min.getZ() <= max.getZ() && cell.max.getZ() >= min.getZ()) {
                result.addAll(cell.getBlocks());
            }
        }
        return result;
    }

    private int getCellKey(BlockPos pos) {
        int x = pos.getX() / GRID_CELL_SIZE;
        int y = pos.getY() / GRID_CELL_SIZE;
        int z = pos.getZ() / GRID_CELL_SIZE;
        return Objects.hash(x, y, z);
    }

    private GridCell createCell(BlockPos pos) {
        int cellX = (pos.getX() / GRID_CELL_SIZE) * GRID_CELL_SIZE;
        int cellY = (pos.getY() / GRID_CELL_SIZE) * GRID_CELL_SIZE;
        int cellZ = (pos.getZ() / GRID_CELL_SIZE) * GRID_CELL_SIZE;
        return new GridCell(
            new BlockPos(cellX, cellY, cellZ),
            new BlockPos(cellX + GRID_CELL_SIZE - 1, cellY + GRID_CELL_SIZE - 1, cellZ + GRID_CELL_SIZE - 1)
        );
    }
}
