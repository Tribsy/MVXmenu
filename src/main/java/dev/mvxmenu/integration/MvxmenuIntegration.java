package dev.mvxmenu.integration;

import dev.mvxmenu.config.ConfigSchema;

import java.util.List;

/**
 * Public integration API for MVXmenu.
 *
 * <p>Other mods can use this interface to register their modules,
 * screens, and configuration schemas with the MVXmenu system.
 *
 * <p>Usage:
 * <pre>{@code
 * MvxmenuIntegration.get().registerModule("mymod", myModule);
 * }</pre>
 */
public interface MvxmenuIntegration {

    /**
     * Returns the global MVXmenu integration instance.
     *
     * @return the integration API instance
     */
    MvxmenuIntegration get();

    /**
     * Register a module with MVXmenu.
     *
     * @param moduleId the unique module identifier
     * @param module   the module to register
     */
    void registerModule(String moduleId, MvxmenuModule module);

    /**
     * Get a registered module by its ID.
     *
     * @param moduleId the module identifier
     * @return the registered module, or null if not found
     */
    MvxmenuModule getModule(String moduleId);

    /**
     * Register a screen factory with MVXmenu.
     *
     * @param screenId the screen identifier
     * @param factory  the screen factory
     */
    void registerScreenFactory(String screenId, ScreenFactory factory);

    /**
     * Get a registered screen factory by its ID.
     *
     * @param screenId the screen identifier
     * @return the screen factory, or null if not found
     */
    ScreenFactory getScreenFactory(String screenId);

    /**
     * Register a configuration schema for a mod.
     *
     * @param modId  the mod identifier
     * @param schema the configuration schema
     */
    void registerConfigSchema(String modId, ConfigSchema schema);

    /**
     * Get a registered configuration schema by mod ID.
     *
     * @param modId the mod identifier
     * @return the config schema, or null if not found
     */
    ConfigSchema getConfigSchema(String modId);

    /**
     * Get all registered module IDs.
     *
     * @return list of module IDs
     */
    List<String> getModuleIds();

    interface ScreenFactory {
    }
}
