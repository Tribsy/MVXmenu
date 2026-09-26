package dev.mvxmenu.module;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Immutable identifier for a module, consisting of a namespace and a path.
 * <p>
 * The namespace is typically the mod ID (e.g., "mvxmenu"), and the path is the module's unique identifier within that namespace.
 * </p>
 */
public final class ModuleId {

    private static final Pattern VALID_NAMESPACE = Pattern.compile("^[a-z0-9_.-]{1,64}$");
    private static final Pattern VALID_PATH = Pattern.compile("^[a-z0-9_./-]{1,64}$");

    private final String namespace;
    private final String path;

    private ModuleId(String namespace, String path) {
        if (!VALID_NAMESPACE.matcher(namespace).matches()) {
            throw new IllegalArgumentException("Invalid namespace: " + namespace);
        }
        if (!VALID_PATH.matcher(path).matches()) {
            throw new IllegalArgumentException("Invalid path: " + path);
        }
        this.namespace = namespace;
        this.path = path;
    }

    /**
     * Creates a module ID with the given namespace and path.
     *
     * @param namespace the namespace (must not be null)
     * @param path      the path (must not be null)
     * @return a new ModuleId
     */
    public static ModuleId of(String namespace, String path) {
        return new ModuleId(namespace, path);
    }

    /**
     * Creates a module ID with the default namespace "mvxmenu" and the given path.
     *
     * @param path the path (must not be null)
     * @return a new ModuleId
     */
    public static ModuleId of(String path) {
        return new ModuleId("mvxmenu", path);
    }

    /**
     * Creates a module ID from a colon-separated string "namespace:path".
     *
     * @param colonNotation the string in the form "namespace:path"
     * @return a new ModuleId
     * @throws IllegalArgumentException if the string is not in the correct format
     */
    public static ModuleId from(String colonNotation) {
        if (colonNotation == null) {
            throw new IllegalArgumentException("Module ID cannot be null");
        }
        int index = colonNotation.indexOf(':');
        if (index <= 0 || index >= colonNotation.length() - 1) {
            throw new IllegalArgumentException("Invalid module ID format: " + colonNotation);
        }
        String namespace = colonNotation.substring(0, index);
        String path = colonNotation.substring(index + 1);
        return new ModuleId(namespace, path);
    }

    public String getNamespace() {
        return namespace;
    }

    public String getPath() {
        return path;
    }

    /**
     * Returns the identifier in the form "namespace:path".
     *
     * @return the colon-separated identifier
     */
    public String toColonNotation() {
        return namespace + ":" + path;
    }

    /**
     * Returns the identifier in the form "namespace.path".
     *
     * @return the dot-separated identifier
     */
    public String toDotNotation() {
        return namespace + "." + path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ModuleId)) return false;
        ModuleId moduleId = (ModuleId) o;
        return Objects.equals(namespace, moduleId.namespace) && Objects.equals(path, moduleId.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, path);
    }

    @Override
    public String toString() {
        return toColonNotation();
    }
}