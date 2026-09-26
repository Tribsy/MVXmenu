package dev.mvxmenu.module;

import dev.mvxmenu.ui.views.BaseView;
import dev.mvxmenu.ui.layout.MvxmenuLayout;
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
     * @param metadata the module metadata (must not be null)
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

    protected <S extends Setting<?>> S registerSetting(S setting) {
        settings.add(setting);
        return setting;
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    // --- Convenience ---

    public String getName() {
        return getDisplayName();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // --- Lifecycle ---

    public void onInitialize() {
    }

    public void onEnable() {
    }

    public void onDisable() {
    }

    public void onTick() {
    }

    /**
     * Creates a widget representing this module in the module list.
     */
    public ModuleCardWidget createCardWidget() {
        return new ModuleCardWidget(this);
    }

    /**
     * Creates a specialized view for this module.
     * <p>
     * Default implementation returns a {@link dev.mvxmenu.ui.views.ModuleDetailView}.
     * </p>
     *
     * @param layout the current layout context
     * @return a view for this module
     */
    public BaseView createDetailView(MvxmenuLayout layout) {
        return new dev.mvxmenu.ui.views.ModuleDetailView(layout, this);
    }
}

