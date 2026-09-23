package dev.mvxmenu.performance;

import dev.mvxmenu.MvxmenuClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class PerformanceManager {

    private static boolean initialized = false;

    public static void init() {
        if (initialized) return;
        initialized = true;

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            MemoryMonitor.checkAndGc();

            if (client.player != null && client.player.age % 1200 == 0) { // Every 60 seconds
                MemoryMonitor.logStats();
            }
        });

        // Log initial stats
        MemoryMonitor.logStats();
    }

    public static void onFrameStart() {
        BatchRenderer.begin();
    }

    public static void onFrameEnd() {
        BatchRenderer.flush();
        RenderCache.cleanup(System.currentTimeMillis());
    }
}