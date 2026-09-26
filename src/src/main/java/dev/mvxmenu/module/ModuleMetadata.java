package dev.mvxmenu.module;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Immutable metadata for a module, describing its identity, dependencies, and other information.
 */
public final class ModuleMetadata {

    private final ModuleId id;
    private final String displayName;
    private final String description;
    private final Module.Category category;
    private final String version;
    private final Set<ModuleId> dependencies;
    private final Set<ModuleId> softDependencies;
    private final List<String> authors;
    private final String homepage;

    private ModuleMetadata(Builder builder) {
        this.id = Objects.requireNonNull(builder.id);
        this.displayName = Objects.requireNonNull(builder.displayName);
        this.description = Objects.requireNonNull(builder.description);
        this.category = Objects.requireNonNull(builder.category);
        this.version = Objects.requireNonNull(builder.version);
        this.dependencies = Collections.unmodifiableSet(
                builder.dependencies != null ? Set.copyOf(builder.dependencies) : Set.of());
        this.softDependencies = Collections.unmodifiableSet(
                builder.softDependencies != null ? Set.copyOf(builder.softDependencies) : Set.of());
        this.authors = Collections.unmodifiableList(
                builder.authors != null ? List.copyOf(builder.authors) : List.of());
        this.homepage = builder.homepage != null ? builder.homepage : "";
    }

    public ModuleId getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Module.Category getCategory() {
        return category;
    }

    public String getVersion() {
        return version;
    }

    public Set<ModuleId> getDependencies() {
        return dependencies;
    }

    public Set<ModuleId> getSoftDependencies() {
        return softDependencies;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getHomepage() {
        return homepage;
    }

    /**
     * Builder for ModuleMetadata.
     */
    public static class Builder {
        private ModuleId id;
        private String displayName;
        private String description;
        private Module.Category category;
        private String version;
        private Set<ModuleId> dependencies;
        private Set<ModuleId> softDependencies;
        private List<String> authors;
        private String homepage;

        public Builder() {
        }

        public Builder id(ModuleId id) {
            this.id = id;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder category(Module.Category category) {
            this.category = category;
            return this;
        }

        public Builder version(String version) {
            this.version = version;
            return this;
        }

        public Builder dependencies(Set<ModuleId> dependencies) {
            this.dependencies = dependencies;
            return this;
        }

        public Builder softDependencies(Set<ModuleId> softDependencies) {
            this.softDependencies = softDependencies;
            return this;
        }

        public Builder authors(List<String> authors) {
            this.authors = authors;
            return this;
        }

        public Builder homepage(String homepage) {
            this.homepage = homepage;
            return this;
        }

        public ModuleMetadata build() {
            return new ModuleMetadata(this);
        }
    }

    /**
     * Creates a new builder for ModuleMetadata.
     *
     * @return a new builder
     */
    public static Builder builder() {
        return new Builder();
    }
}