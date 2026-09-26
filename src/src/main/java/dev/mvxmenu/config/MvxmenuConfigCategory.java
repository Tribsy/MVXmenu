package dev.mvxmenu.config;

public enum MvxmenuConfigCategory {
    APPEARANCE("appearance"),
    PERFORMANCE("performance"),
    SYSTEM("system");

    private final String name;

    MvxmenuConfigCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MvxmenuConfigCategory fromName(String name) {
        for (MvxmenuConfigCategory category : values()) {
            if (category.name.equalsIgnoreCase(name)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Unknown config category: " + name);
    }
}
