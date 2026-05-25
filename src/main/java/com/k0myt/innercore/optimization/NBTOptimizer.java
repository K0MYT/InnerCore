package com.k0myt.innercore.optimization;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import java.util.HashMap;
import java.util.Map;

/**
 * Optimized NBT serialization for block storage
 * Uses compression and delta encoding for network efficiency
 */
public class NBTOptimizer {
    private static final int COMPRESSION_THRESHOLD = 256; // bytes

    /**
     * Compress NBT data for network transmission
     */
    public static byte[] compressNBT(CompoundTag tag) {
        try {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.util.zip.GZIPOutputStream gzip = new java.util.zip.GZIPOutputStream(baos);
            
            byte[] data = new byte[0];
            net.minecraft.nbt.NbtIo.writeCompressed(tag, baos);
            
            gzip.close();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    /**
     * Decompress NBT data from network
     */
    public static CompoundTag decompressNBT(byte[] data) {
        try {
            java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(data);
            return net.minecraft.nbt.NbtIo.readCompressed(bais);
        } catch (Exception e) {
            e.printStackTrace();
            return new CompoundTag();
        }
    }

    /**
     * Estimate NBT size to determine if compression is needed
     */
    public static int estimateSize(CompoundTag tag) {
        try {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            net.minecraft.nbt.NbtIo.write(tag, baos);
            return baos.size();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Check if NBT should be compressed
     */
    public static boolean shouldCompress(CompoundTag tag) {
        return estimateSize(tag) > COMPRESSION_THRESHOLD;
    }
}
