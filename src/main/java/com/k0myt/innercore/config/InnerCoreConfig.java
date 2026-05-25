package com.k0myt.innercore.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class InnerCoreConfig {
    public static final CommonConfig COMMON;
    public static final ModConfigSpec COMMON_SPEC;

    static {
        final Pair<CommonConfig, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(CommonConfig::new);
        COMMON = specPair.getLeft();
        COMMON_SPEC = specPair.getRight();
    }

    public static class CommonConfig {
        public final ModConfigSpec.IntValue gridSize;
        public final ModConfigSpec.IntValue maxBlocksPerPlate;
        public final ModConfigSpec.BooleanValue enableCreateCompat;
        public final ModConfigSpec.BooleanValue enableComputerCraftCompat;
        public final ModConfigSpec.BooleanValue enableParticleEffects;
        public final ModConfigSpec.BooleanValue enableSoundEffects;
        public final ModConfigSpec.IntValue renderDistance;
        public final ModConfigSpec.BooleanValue enableNetworkOptimization;

        public CommonConfig(ModConfigSpec.Builder builder) {
            builder.comment("InnerCore Mod Configuration").push("innercore");

            gridSize = builder
                    .comment("Grid size for InnerCore Plate (default 6 = 6x6x6)")
                    .defineInRange("gridSize", 6, 3, 10);

            maxBlocksPerPlate = builder
                    .comment("Maximum blocks that can be stored in one plate")
                    .defineInRange("maxBlocksPerPlate", 216, 27, 1000);

            enableCreateCompat = builder
                    .comment("Enable Create mod compatibility (redstone links, mechanical power)")
                    .define("enableCreateCompat", true);

            enableComputerCraftCompat = builder
                    .comment("Enable ComputerCraft: Tweaked compatibility (modem signals)")
                    .define("enableComputerCraftCompat", true);

            enableParticleEffects = builder
                    .comment("Enable visual particle effects for block placement/removal")
                    .define("enableParticleEffects", true);

            enableSoundEffects = builder
                    .comment("Enable sound effects for block interactions")
                    .define("enableSoundEffects", true);

            renderDistance = builder
                    .comment("Block render distance (in blocks, default 256)")
                    .defineInRange("renderDistance", 256, 64, 1024);

            enableNetworkOptimization = builder
                    .comment("Enable network packet optimization (recommended for servers)")
                    .define("enableNetworkOptimization", true);

            builder.pop();
        }
    }
}
