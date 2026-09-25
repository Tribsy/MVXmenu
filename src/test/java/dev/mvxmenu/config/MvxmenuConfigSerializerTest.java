package dev.mvxmenu.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class MvxmenuConfigSerializerTest {

    @Test
    void testCreateDefaults(@TempDir Path tempDir) {
        MvxmenuConfigSerializer serializer = new MvxmenuConfigSerializer(tempDir);
        MvxmenuConfig config = serializer.load();

        assertEquals("1.0.0", config.getVersion());
        assertEquals(3, config.getGuiScale());
        assertEquals("DEFAULT (PURPLE)", config.getThemeAccent());
        assertTrue(config.isBlurEffects());
        assertFalse(config.isScanlineOverlay());
        assertEquals(100, config.getTickRateLimit());
        assertEquals("AUTO-DETECT", config.getRenderBackend());
        assertTrue(config.isTelemetry());
    }

    @Test
    void testSaveAndLoad(@TempDir Path tempDir) throws IOException {
        MvxmenuConfigSerializer serializer = new MvxmenuConfigSerializer(tempDir);

        MvxmenuConfig original = new MvxmenuConfig();
        original.setGuiScale(2);
        original.setThemeAccent("PURPLE");
        original.setBlurEffects(false);
        original.setScanlineOverlay(true);
        original.setTickRateLimit(60);
        original.setRenderBackend("SOFTWARE");
        original.setTelemetry(false);

        serializer.save(original);

        assertTrue(Files.exists(serializer.getConfigPath()));

        MvxmenuConfig loaded = serializer.load();

        assertEquals(original.getVersion(), loaded.getVersion());
        assertEquals(original.getGuiScale(), loaded.getGuiScale());
        assertEquals(original.getThemeAccent(), loaded.getThemeAccent());
        assertEquals(original.isBlurEffects(), loaded.isBlurEffects());
        assertEquals(original.isScanlineOverlay(), loaded.isScanlineOverlay());
        assertEquals(original.getTickRateLimit(), loaded.getTickRateLimit());
        assertEquals(original.getRenderBackend(), loaded.getRenderBackend());
        assertEquals(original.isTelemetry(), loaded.isTelemetry());
    }

    @Test
    void testJsonContainsExposeFieldsOnly(@TempDir Path tempDir) throws IOException {
        MvxmenuConfigSerializer serializer = new MvxmenuConfigSerializer(tempDir);
        MvxmenuConfig config = new MvxmenuConfig();
        serializer.save(config);

        String content = new String(Files.readAllBytes(serializer.getConfigPath()));

        assertTrue(content.contains("\"gui_scale\""), "Should contain gui_scale");
        assertTrue(content.contains("\"theme_accent\""), "Should contain theme_accent");
        assertTrue(content.contains("\"blur_effects\""), "Should contain blur_effects");
        assertTrue(content.contains("\"version\""), "Should contain version");
    }

    @Test
    void testCategoryFromName(@TempDir Path tempDir) {
        assertEquals(MvxmenuConfigCategory.APPEARANCE, MvxmenuConfigCategory.fromName("appearance"));
        assertEquals(MvxmenuConfigCategory.PERFORMANCE, MvxmenuConfigCategory.fromName("performance"));
        assertEquals(MvxmenuConfigCategory.SYSTEM, MvxmenuConfigCategory.fromName("system"));
        assertEquals(MvxmenuConfigCategory.APPEARANCE, MvxmenuConfigCategory.fromName("APPEARANCE"));
    }

    @Test
    void testCategoryInvalidName(@TempDir Path tempDir) {
        assertThrows(IllegalArgumentException.class, () -> {
            MvxmenuConfigCategory.fromName("nonexistent");
        });
    }
}
