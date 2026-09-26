package dev.mvxmenu.integration;

import dev.mvxmenu.config.ConfigParameter;
import dev.mvxmenu.config.ConfigSchema;
import dev.mvxmenu.config.MvxmenuConfigCategory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MvxmenuIntegrationImplTest {

    @Test
    void testRegisterAndGetModule() {
        MvxmenuIntegrationImpl integration = new MvxmenuIntegrationImpl();

        MvxmenuModule module = new MvxmenuModule() {
            @Override public String getId() { return "test_mod"; }
            @Override public String getName() { return "Test Module"; }
            @Override public String getDescription() { return "A test module"; }
            @Override public boolean isEnabled() { return true; }
            @Override public void setEnabled(boolean enabled) { }
            @Override public List<ConfigParameter> getParameters() { return List.of(); }
            @Override public ConfigParameter getParameter(String name) { return null; }
        };

        integration.registerModule("test_mod", module);
        assertSame(module, integration.getModule("test_mod"));
    }

    @Test
    void testGetModuleNotFound() {
        MvxmenuIntegrationImpl integration = new MvxmenuIntegrationImpl();
        assertNull(integration.getModule("nonexistent"));
    }

    @Test
    void testGetModuleIds() {
        MvxmenuIntegrationImpl integration = new MvxmenuIntegrationImpl();
        integration.registerModule("mod_a", createModule("mod_a"));
        integration.registerModule("mod_b", createModule("mod_b"));

        List<String> ids = integration.getModuleIds();
        assertEquals(2, ids.size());
        assertTrue(ids.contains("mod_a"));
        assertTrue(ids.contains("mod_b"));
    }

    @Test
    void testRegisterScreenFactory() {
        MvxmenuIntegrationImpl integration = new MvxmenuIntegrationImpl();
        MvxmenuIntegration.ScreenFactory factory = new MvxmenuIntegration.ScreenFactory() {};
        integration.registerScreenFactory("test_screen", factory);
        assertSame(factory, integration.getScreenFactory("test_screen"));
    }

    @Test
    void testRegisterConfigSchema() {
        MvxmenuIntegrationImpl integration = new MvxmenuIntegrationImpl();
        ConfigSchema schema = new ConfigSchema() {
            @Override public String getId() { return "test"; }
            @Override public String getName() { return "Test Schema"; }
            @Override public List<MvxmenuConfigCategory> getCategories() { return List.of(); }
            @Override public Map<String, List<ConfigParameter>> getParametersByCategory() { return Map.of(); }
        };
        integration.registerConfigSchema("test_mod", schema);
        assertSame(schema, integration.getConfigSchema("test_mod"));
    }

    @Test
    void testGetAllModules() {
        MvxmenuIntegrationImpl integration = new MvxmenuIntegrationImpl();
        MvxmenuModule modA = new MvxmenuModule() {
            @Override public String getId() { return "mod_a"; }
            @Override public String getName() { return "Test Module"; }
            @Override public String getDescription() { return ""; }
            @Override public boolean isEnabled() { return false; }
            @Override public void setEnabled(boolean enabled) { }
            @Override public List<ConfigParameter> getParameters() { return List.of(); }
            @Override public ConfigParameter getParameter(String name) { return null; }
        };
        integration.registerModule("mod_a", modA);
        Map<String, MvxmenuModule> all = integration.getAllModules();
        assertEquals(1, all.size());
        assertEquals("Test Module", all.get("mod_a").getName());
    }

    private MvxmenuModule createModule(String id) {
        return new MvxmenuModule() {
            @Override public String getId() { return id; }
            @Override public String getName() { return id; }
            @Override public String getDescription() { return ""; }
            @Override public boolean isEnabled() { return false; }
            @Override public void setEnabled(boolean enabled) { }
            @Override public List<ConfigParameter> getParameters() { return List.of(); }
            @Override public ConfigParameter getParameter(String name) { return null; }
        };
    }
}
