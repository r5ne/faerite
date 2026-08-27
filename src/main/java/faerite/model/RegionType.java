package faerite.model;

/// The possible geographic types that a region can have.
public enum RegionType {
    ARCHIPELAGO("Archipelago"),
    ISLAND("Island"),
    ISLAND_GROUP("Island group"),
    ROCK_GROUP("Rock group"),
    REEF("Reef");

    private final String displayName;

    RegionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
