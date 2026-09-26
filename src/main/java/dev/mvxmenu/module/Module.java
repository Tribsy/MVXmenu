package dev.mvxmenu.module;

import dev.mvxmenu.ui.widget.ModuleCardWidget;
import dev.mvxmenu.theme.MvxmenuIcons;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Base class for all modules in MVXmenu.
 * <p>
 * Modules are registered via the {@link ModuleProvider} SPI and managed by the {@link ModuleRegistry}.
 * </p>
 */
public abstract class Module {

    /**
     * Category of a module.
     */
    public enum Category {
        COMBAT("Combat"),
        MOVEMENT("Movement"),
        PLAYER("Player"),
        RENDER("Render"),
        EXPLOIT("Exploit"),
        WORLD("World");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public MvxmenuIcons getIcon() {
            return MvxmenuIcons.fromCategoryName(displayName);
        }
    }

    private final ModuleMetadata metadata;
    private final List<Setting<?>> settings = new ArrayList<>();
    private volatile boolean enabled = false;

    /**
     * Creates a module with the given metadata.
     *
     * @param metadata the module's metadata (must not be null)
     */
    protected Module(ModuleMetadata metadata) {
        this.metadata = Objects.requireNonNull(metadata);
    }

    // --- Metadata accessors ---

    public ModuleId getId() {
        return metadata.getId();
    }

    public String getDisplayName() {
        return metadata.getDisplayName();
    }

    public String getDescription() {
        return metadata.getDescription();
    }

    public Module.Category getCategory() {
        return metadata.getCategory();
    }

    public String getVersion() {
        return metadata.getVersion();
    }

    public Set<ModuleId> getDependencies() {
        return metadata.getDependencies();
    }

    public Set<ModuleId> getSoftDependencies() {
        return metadata.getSoftDependencies();
    }

    public List<String> getAuthors() {
        return metadata.getAuthors();
    }

    public String getHomepage() {
        return metadata.getHomepage();
    }

    // --- Settings ---

    /**
     * Registers a setting with this module.
     * <p>
     * Settings must be registered in the constructor.
     * </p>
     *
     * @param setting the setting to register
     * @param <S>     the type of the setting
     * @return the registered setting (for chaining)
     */
    protected <S extends Setting<?>> S registerSetting(S setting) {
        settings.add(setting);
        return setting;
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    // --- Convenience ---

    /**
     * Returns the display name of the module.
     *
     * @return the display name
     */
    public String getName() {
        return getDisplayName();
    }

    /**
     * Returns true if the module is enabled.
     *
     * @return true if enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the enabled state of the module.
     * <p>
     * This method should be called by the module manager when the module is toggled.
     * </p>
     *
     * @param enabled the new enabled state
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // --- Lifecycle ---

    /**
     * Called once after the module has been registered and all dependencies are satisfied.
     * <p>
     * This is the appropriate place to perform one-time initialization.
     * </p>
     */
    public void onInitialize() {
    }

    /**
     * Called when the module is enabled.
     */
    public void onEnable() {
    }

    /**
     * Called when the module is disabled.
     */
    public void onDisable() {
    }

    /**
     * Called every tick while the module is enabled.
     * <p>
     * This is called on the client thread, so it is safe to access client-only classes.
     * </p>
     */
    public void onTick() {
    }

    /**
     * Creates a widget representing this module in the module list.
     * <p>
     * The default implementation returns a {@link ModuleCardWidget}.
     * </p>
     *
     * @return a widget for this module
     */
    public ModuleCardWidget createCardWidget() {
        return new ModuleCardWidget(this);
    }
}