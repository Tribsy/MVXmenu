package dev.mvxmenu.performance;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;

public class BatchRenderer {

    private static final int MAX_VERTICES = 16384;
    private static final float[] vertexBuffer = new float[MAX_VERTICES * 7]; // x,y,z,r,g,b,a
    private static int vertexCount = 0;

    public static void begin() {
        vertexCount = 0;
    }

    public static void addBox(Vec3d min, Vec3d max, int color) {
        if (vertexCount + 24 * 7 > MAX_VERTICES) {
            flush();
        }

        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = ((color >> 24) & 0xFF) / 255.0f;

        float minX = (float) min.x, minY = (float) min.y, minZ = (float) min.z;
        float maxX = (float) max.x, maxY = (float) max.y, maxZ = (float) max.z;

        // 8 corners of the box
        float[][] corners = {
            {minX, minY, minZ}, {maxX, minY, minZ}, {maxX, maxY, minZ}, {minX, maxY, minZ},
            {minX, minY, maxZ}, {maxX, minY, maxZ}, {maxX, maxY, maxZ}, {minX, maxY, maxZ}
        };

        int[][] edges = {
            {0,1}, {1,2}, {2,3}, {3,0},
            {4,5}, {5,6}, {6,7}, {7,4},
            {0,4}, {1,5}, {2,6}, {3,7}
        };

        for (int[] edge : edges) {
            float[] c1 = corners[edge[0]];
            float[] c2 = corners[edge[1]];
            int idx = vertexCount * 7;
            vertexBuffer[idx++] = c1[0]; vertexBuffer[idx++] = c1[1]; vertexBuffer[idx++] = c1[2];
            vertexBuffer[idx++] = r; vertexBuffer[idx++] = g; vertexBuffer[idx++] = b; vertexBuffer[idx++] = a;
            idx = vertexCount * 7 + 7;
            vertexBuffer[idx++] = c2[0]; vertexBuffer[idx++] = c2[1]; vertexBuffer[idx++] = c2[2];
            vertexBuffer[idx++] = r; vertexBuffer[idx++] = g; vertexBuffer[idx++] = b; vertexBuffer[idx++] = a;
            vertexCount += 2;
        }
    }

    public static void flush() {
        // Render the batch - implementation depends on render system
        vertexCount = 0;
    }

    public static int getVertexCount() {
        return vertexCount;
    }
}