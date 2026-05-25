package com.k0myt.innercore.optimization;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import java.util.*;

/**
 * Frustum culling for efficient rendering of stored blocks
 * Only renders blocks that are visible in the camera view
 */
public class FrustumCulling {
    private static final float FOV = 70.0f;
    private static final float NEAR_PLANE = 0.05f;
    private static final float FAR_PLANE = 256.0f;

    public static class Frustum {
        private final Vec3 pos;
        private final Vec3 forward;
        private final Vec3 right;
        private final Vec3 up;
        private final float fov;
        private final float aspectRatio;

        public Frustum(Vec3 cameraPos, Vec3 cameraForward, Vec3 cameraRight, Vec3 cameraUp, float fov, float aspectRatio) {
            this.pos = cameraPos;
            this.forward = cameraForward.normalize();
            this.right = cameraRight.normalize();
            this.up = cameraUp.normalize();
            this.fov = fov;
            this.aspectRatio = aspectRatio;
        }

        public boolean isAABBVisible(BlockPos minPos, BlockPos maxPos) {
            // Simple frustum check using sphere test
            double centerX = (minPos.getX() + maxPos.getX()) / 2.0;
            double centerY = (minPos.getY() + maxPos.getY()) / 2.0;
            double centerZ = (minPos.getZ() + maxPos.getZ()) / 2.0;

            double radius = Math.sqrt(
                Math.pow((maxPos.getX() - minPos.getX()) / 2.0, 2) +
                Math.pow((maxPos.getY() - minPos.getY()) / 2.0, 2) +
                Math.pow((maxPos.getZ() - minPos.getZ()) / 2.0, 2)
            );

            Vec3 toCenter = new Vec3(centerX - pos.x, centerY - pos.y, centerZ - pos.z);
            double distToCenter = toCenter.length();

            if (distToCenter > FAR_PLANE + radius) return false;
            if (distToCenter < NEAR_PLANE - radius) return false;

            // Check FOV
            double dot = toCenter.normalize().dot(forward);
            double halfFov = Math.toRadians(fov / 2.0);
            if (dot < Math.cos(halfFov)) return false;

            return true;
        }
    }

    public static Frustum createFrustum(Vec3 cameraPos, Vec3 cameraForward, Vec3 cameraRight, Vec3 cameraUp) {
        return new Frustum(cameraPos, cameraForward, cameraRight, cameraUp, FOV, 16.0f / 9.0f);
    }
}
