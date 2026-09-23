package faerite.model;

import org.jetbrains.annotations.Nullable;

public enum Habitat {
    GRASSLAND(null, "Grassland"),
    SHRUBLAND(null, "Shrubland"),
    HEATHLAND(SHRUBLAND, "Heathland"),
    COASTAL_HEATHLAND(HEATHLAND, "Coastal heathland"),
    MOORLAND(SHRUBLAND, "Moorland"),
    HEATHER_MOORLAND(MOORLAND, "Heather moorland");

    @Nullable
    private final Habitat parent;

    private final String displayName;
    private final String description;

    Habitat(@Nullable Habitat parentHabitat, String displayName) {
        this.parent = parentHabitat;
        this.displayName = displayName;
        this.description = "";
    }

    Habitat(@Nullable Habitat parentHabitat, String displayName, String description) {
        this.parent = parentHabitat;
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isSubBiomeOf(Habitat habitat) {
        if (this == habitat) return true;
        if (this.parent == null) return false;
        return this.parent.isSubBiomeOf(habitat);
    }
}
