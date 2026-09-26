package dev.mvxmenu.performance;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;

public class RenderCache {

    private static final int MAX_CACHED_ENTITIES = 256;
    private static final int CACHE_TTL_TICKS = 20;

    private static final RenderEntry[] entityCache = new RenderEntry[MAX_CACHED_ENTITIES];
    private static int cacheSize = 0;
    private static long lastCleanup = 0;

    public static void cacheEntity(int entityId, Vec3d pos, float yaw, float pitch, int color) {
        if (cacheSize >= MAX_CACHED_ENTITIES) return;
        entityCache[cacheSize++] = new RenderEntry(entityId, pos, yaw, pitch, color, System.currentTimeMillis());
    }

    public static void clearCache() {
        for (int i = 0; i < cacheSize; i++) {
            entityCache[i] = null;
        }
        cacheSize = 0;
    }

    public static void cleanup(long currentTime) {
        if (currentTime - lastCleanup < CACHE_TTL_TICKS * 50) return;
        lastCleanup = currentTime;
        int writeIdx = 0;
        for (int i = 0; i < cacheSize; i++) {
            if (entityCache[i] != null && currentTime - entityCache[i].timestamp < CACHE_TTL_TICKS * 50) {
                entityCache[writeIdx++] = entityCache[i];
            }
        }
        for (int i = writeIdx; i < cacheSize; i++) {
            entityCache[i] = null;
        }
        cacheSize = writeIdx;
    }

    public static RenderEntry[] getCachedEntities() {
        return entityCache;
    }

    public static int getCacheSize() {
        return cacheSize;
    }

    public static class RenderEntry {
        public final int entityId;
        public final Vec3d pos;
        public final float yaw;
        public final float pitch;
        public final int color;
        public final long timestamp;

        public RenderEntry(int entityId, Vec3d pos, float yaw, float pitch, int color, long timestamp) {
            this.entityId = entityId;
            this.pos = pos;
            this.yaw = yaw;
            this.pitch = pitch;
            this.color = color;
            this.timestamp = timestamp;
        }
    }
}