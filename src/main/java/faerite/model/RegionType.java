package faerite.model;

public enum RegionType {
    ARCHIPELAGO("Archipelago"), ISLAND("Island");

    private final String displayName;

    RegionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
