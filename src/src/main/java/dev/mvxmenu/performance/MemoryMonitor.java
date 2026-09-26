package dev.mvxmenu.performance;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryMonitor {

    private static final MemoryMXBean MEMORY_BEAN = ManagementFactory.getMemoryMXBean();
    private static long lastGcTime = 0;
    private static int gcCount = 0;

    public static MemoryStats getStats() {
        MemoryUsage heap = MEMORY_BEAN.getHeapMemoryUsage();
        MemoryUsage nonHeap = MEMORY_BEAN.getNonHeapMemoryUsage();

        return new MemoryStats(
                (int)(heap.getUsed() / 1024 / 1024),
                (int)(heap.getMax() / 1024 / 1024),
                (int)(heap.getCommitted() / 1024 / 1024),
                (int)(nonHeap.getUsed() / 1024 / 1024),
                gcCount
        );
    }

    public static void checkAndGc() {
        MemoryUsage heap = MEMORY_BEAN.getHeapMemoryUsage();
        double usagePercent = (double) heap.getUsed() / heap.getMax();

        if (usagePercent > 0.85 && System.currentTimeMillis() - lastGcTime > 30000) {
            System.gc();
            lastGcTime = System.currentTimeMillis();
            gcCount++;
        }
    }

    public static void logStats() {
        MemoryStats stats = getStats();
        System.out.printf("[MVXmenu Memory] Heap: %d/%d MB (%.1f%%), Non-heap: %d MB, GC: %d%n",
                stats.usedHeap, stats.maxHeap, (double) stats.usedHeap / stats.maxHeap * 100,
                stats.usedNonHeap, stats.gcCount);
    }

    public record MemoryStats(
            int usedHeap,
            int maxHeap,
            int committedHeap,
            int usedNonHeap,
            int gcCount
    ) {}
}