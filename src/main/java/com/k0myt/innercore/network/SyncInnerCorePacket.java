package com.k0myt.innercore.network;

import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.k0myt.innercore.InnerCoreMod;
import com.k0myt.innercore.blockentity.InnerCorePlateBlockEntity;

public class SyncInnerCorePacket implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(InnerCoreMod.MOD_ID, "sync_innercore");

    private final BlockPos blockPos;
    private final int blockCount;

    public SyncInnerCorePacket(BlockPos blockPos, int blockCount) {
        this.blockPos = blockPos;
        this.blockCount = blockCount;
    }

    public SyncInnerCorePacket(FriendlyByteBuf buf) {
        this.blockPos = buf.readBlockPos();
        this.blockCount = buf.readInt();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(blockPos);
        buf.writeInt(blockCount);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static void handle(SyncInnerCorePacket packet, PlayPayloadContext context) {
        context.enqueueWork(() -> {
            // Client-side packet handling
        });
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public int getBlockCount() {
        return blockCount;
    }
}
