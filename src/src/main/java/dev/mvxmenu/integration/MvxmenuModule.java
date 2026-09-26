package dev.mvxmenu.integration;

import dev.mvxmenu.config.ConfigParameter;
import dev.mvxmenu.config.ConfigSchema;

import java.util.List;

/**
 * A module registered with MVXmenu.
 *
 * <p>Modules provide configurable features that integrate with the MVXmenu
 * UI system. Each module has an ID, name, description, and configurable
 * parameters.
 */
public interface MvxmenuModule {

    /**
     * Get the unique module identifier.
     *
     * @return the module ID
     */
    String getId();

    /**
     * Get the display name of the module.
     *
     * @return the module name
     */
    String getName();

    /**
     * Get a description of what the module does.
     *
     * @return the module description
     */
    String getDescription();

    /**
     * Whether the module is currently enabled.
     *
     * @return true if enabled
     */
    boolean isEnabled();

    /**
     * Set whether the module is enabled.
     *
     * @param enabled true to enable
     */
    void setEnabled(boolean enabled);

    /**
     * Get the configuration parameters for this module.
     *
     * @return list of config parameters
     */
    List<ConfigParameter> getParameters();

    /**
     * Get a configuration parameter by name.
     *
     * @param name the parameter name
     * @return the config parameter, or null if not found
     */
    ConfigParameter getParameter(String name);
}
