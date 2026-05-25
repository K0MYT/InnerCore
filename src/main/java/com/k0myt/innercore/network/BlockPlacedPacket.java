package com.k0myt.innercore.network;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import com.k0myt.innercore.InnerCoreMod;

public class BlockPlacedPacket implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(InnerCoreMod.MOD_ID, "block_placed");

    private final BlockPos platePos;
    private final BlockPos gridPos;
    private final boolean placed; // true = placed, false = removed

    public BlockPlacedPacket(BlockPos platePos, BlockPos gridPos, boolean placed) {
        this.platePos = platePos;
        this.gridPos = gridPos;
        this.placed = placed;
    }

    public BlockPlacedPacket(FriendlyByteBuf buf) {
        this.platePos = buf.readBlockPos();
        this.gridPos = buf.readBlockPos();
        this.placed = buf.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(platePos);
        buf.writeBlockPos(gridPos);
        buf.writeBoolean(placed);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static void handle(BlockPlacedPacket packet, PlayPayloadContext context) {
        context.enqueueWork(() -> {
            // Client-side handling for particles/sounds
            Level level = context.player().level();
            if (level != null && level.isClientSide) {
                BlockPos platePos = packet.platePos;
                
                // Play sound
                if (packet.placed) {
                    level.playSound(null, platePos, SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 0.5f, 1.0f);
                } else {
                    level.playSound(null, platePos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 0.5f, 1.0f);
                }
                
                // Spawn particles
                spawnParticles(level, platePos, packet.gridPos, packet.placed);
            }
        });
    }

    private static void spawnParticles(Level level, BlockPos platePos, BlockPos gridPos, boolean placed) {
        // Calculate particle position
        double x = platePos.getX() + gridPos.getX() / 6.0 + 0.5;
        double y = platePos.getY() + gridPos.getY() / 6.0 + 0.5;
        double z = platePos.getZ() + gridPos.getZ() / 6.0 + 0.5;

        // Spawn dust particles
        for (int i = 0; i < 8; i++) {
            double vx = (Math.random() - 0.5) * 0.2;
            double vy = (Math.random() - 0.5) * 0.2;
            double vz = (Math.random() - 0.5) * 0.2;
            level.addParticle(
                net.minecraft.core.particles.ParticleTypes.DUST,
                x, y, z, vx, vy, vz
            );
        }
    }

    public BlockPos getPlatePos() {
        return platePos;
    }

    public BlockPos getGridPos() {
        return gridPos;
    }

    public boolean isPlaced() {
        return placed;
    }
}
