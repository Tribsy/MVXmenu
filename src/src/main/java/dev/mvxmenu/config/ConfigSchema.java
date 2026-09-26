package dev.mvxmenu.config;

import java.util.List;
import java.util.Map;

/**
 * A configuration schema that mods can register with MVXmenu.
 *
 * <p>Defines the structure of a configuration section including
 * category organization and parameter definitions.
 */
public interface ConfigSchema {

    /**
     * Get the schema identifier (usually the mod ID).
     *
     * @return the schema ID
     */
    String getId();

    /**
     * Get the display name of the schema.
     *
     * @return the display name
     */
    String getName();

    /**
     * Get all categories defined in this schema.
     *
     * @return list of categories
     */
    List<MvxmenuConfigCategory> getCategories();

    /**
     * Get all parameters grouped by category name.
     *
     * @return map of category name to parameter list
     */
    Map<String, List<ConfigParameter>> getParametersByCategory();
}
