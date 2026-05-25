package com.k0myt.innercore.util;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.Level;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

public class VisualFeedback {
    /**
     * Spawn particles when block is placed
     */
    public static void spawnPlaceParticles(Level level, double x, double y, double z, int count) {
        for (int i = 0; i < count; i++) {
            double vx = (Math.random() - 0.5) * 0.4;
            double vy = (Math.random() - 0.5) * 0.4;
            double vz = (Math.random() - 0.5) * 0.4;
            
            level.addParticle(
                ParticleTypes.CLOUD,
                x, y, z, vx, vy, vz
            );
        }
    }

    /**
     * Spawn particles when block is removed
     */
    public static void spawnRemoveParticles(Level level, double x, double y, double z, int count) {
        for (int i = 0; i < count; i++) {
            double vx = (Math.random() - 0.5) * 0.6;
            double vy = (Math.random()) * 0.3;
            double vz = (Math.random() - 0.5) * 0.6;
            
            level.addParticle(
                ParticleTypes.POOF,
                x, y, z, vx, vy, vz
            );
        }
    }

    /**
     * Play sound for block placement
     */
    public static void playPlaceSound(Level level, double x, double y, double z) {
        level.playSound(
            null,
            x, y, z,
            SoundEvents.STONE_PLACE,
            SoundSource.BLOCKS,
            0.7f,
            1.2f
        );
    }

    /**
     * Play sound for block removal
     */
    public static void playRemoveSound(Level level, double x, double y, double z) {
        level.playSound(
            null,
            x, y, z,
            SoundEvents.STONE_BREAK,
            SoundSource.BLOCKS,
            0.6f,
            1.0f
        );
    }

    /**
     * Play redstone activation sound
     */
    public static void playRedstoneSound(Level level, double x, double y, double z) {
        level.playSound(
            null,
            x, y, z,
            SoundEvents.REDSTONE_TORCH_BURNOUT,
            SoundSource.BLOCKS,
            0.3f,
            0.8f
        );
    }
}
