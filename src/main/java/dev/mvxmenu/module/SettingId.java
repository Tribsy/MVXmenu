package dev.mvxmenu.module;

import java.util.Objects;

/**
 * Immutable identifier for a setting within a module.
 * <p>
 * A setting ID consists of a module ID and a setting path (the setting's ID within the module).
 * </p>
 */
public final class SettingId {

    private final ModuleId moduleId;
    private final String settingPath;

    private SettingId(ModuleId moduleId, String settingPath) {
        this.moduleId = Objects.requireNonNull(moduleId);
        this.settingPath = Objects.requireNonNull(settingPath);
    }

    /**
     * Creates a setting ID for the given module and setting path.
     *
     * @param moduleId     the module ID (must not be null)
     * @param settingPath  the setting path within the module (must not be null)
     * @return a new SettingId
     */
    public static SettingId of(ModuleId moduleId, String settingPath) {
        return new SettingId(moduleId, settingPath);
    }

    public ModuleId getModuleId() {
        return moduleId;
    }

    public String getSettingPath() {
        return settingPath;
    }

    /**
     * Returns the identifier in the form "moduleId:settingPath".
     *
     * @return the colon-separated identifier
     */
    public String toColonNotation() {
        return moduleId.toColonNotation() + ":" + settingPath;
    }

    /**
     * Returns the identifier in the form "moduleId.settingPath".
     *
     * @return the dot-separated identifier
     */
    public String toDotNotation() {
        return moduleId.toDotNotation() + "." + settingPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SettingId)) return false;
        SettingId that = (SettingId) o;
        return Objects.equals(moduleId, that.moduleId) && Objects.equals(settingPath, that.settingPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moduleId, settingPath);
    }

    @Override
    public String toString() {
        return toColonNotation();
    }
}