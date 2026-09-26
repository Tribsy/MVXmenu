package dev.mvxmenu.integration;

import dev.mvxmenu.config.ConfigSchema;
import dev.mvxmenu.config.MvxmenuConfigCategory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of {@link MvxmenuIntegration}.
 */
public class MvxmenuIntegrationImpl implements MvxmenuIntegration {

    private final Map<String, MvxmenuModule> modules = new ConcurrentHashMap<>();
    private final Map<String, ScreenFactory> screenFactories = new ConcurrentHashMap<>();
    private final Map<String, ConfigSchema> configSchemas = new ConcurrentHashMap<>();

    @Override
    public MvxmenuIntegration get() {
        return this;
    }

    @Override
    public void registerModule(String moduleId, MvxmenuModule module) {
        modules.put(moduleId, module);
    }

    @Override
    public MvxmenuModule getModule(String moduleId) {
        return modules.get(moduleId);
    }

    @Override
    public void registerScreenFactory(String screenId, ScreenFactory factory) {
        screenFactories.put(screenId, factory);
    }

    @Override
    public ScreenFactory getScreenFactory(String screenId) {
        return screenFactories.get(screenId);
    }

    @Override
    public void registerConfigSchema(String modId, ConfigSchema schema) {
        configSchemas.put(modId, schema);
    }

    @Override
    public ConfigSchema getConfigSchema(String modId) {
        return configSchemas.get(modId);
    }

    @Override
    public List<String> getModuleIds() {
        return new ArrayList<>(modules.keySet());
    }

    /**
     * Get the number of registered modules.
     *
     * @return module count
     */
    public int getModuleCount() {
        return modules.size();
    }

    /**
     * Get all registered modules.
     *
     * @return map of module ID to module
     */
    public Map<String, MvxmenuModule> getAllModules() {
        return new HashMap<>(modules);
    }
}
