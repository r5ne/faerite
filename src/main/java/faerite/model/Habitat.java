package faerite.model;

import org.jetbrains.annotations.Nullable;

public enum Habitat {
    GRASSLAND(null, "Grassland"),
    COASTAL_GRASSLAND(
        GRASSLAND,
        "Coastal grassland",
        "A grassland found near sea headland. " +
            "Due to the higher elevation there is not enough moisture for coastal meadows to thrive, " +
            "and so mostly features grasses."
    ),
    CALCAREOUS_GRASSLAND(
        GRASSLAND,
        "Calcareous grassland",
        "A grassland growing on thin poor soil such as chalk or limestone. " +
            "These conditions usually form on steep hills or escarpments, too steep for meadows to form."
    ),
    DUNE_GRASSLAND(
        GRASSLAND,
        "Dune grassland",
        "Sometimes referred to as 'fixed dunes', dune grasslands are a coastal habitat " +
            "formed when shifting sand dunes become anchored by tough specialized grasses."
    ),
    MEADOW(
        GRASSLAND,
        "Meadow",
        "A grassland benefiting from moist soils and therefore dominated by various grasses, " +
            "herbs and wildflowers. It is usually found in lowland areas."
    ),
    COASTAL_MEADOW(
        MEADOW,
        "Coastal meadow",
        "A meadow found near low-lying sea coastlines. " +
            "The sea moistens the soil allowing for meadows, but high salinity from the air, sea and wind " +
            "causes the flora to be dominated by specific salt-tolerant species."
    ),

    SHRUBLAND(null, "Shrubland", "A habitat dominated by shrubs."),
    HEATHLAND(
        SHRUBLAND,
        "Heathland",
        "A shrubland characterized by low-growing vegetation and found at low elevations. " +
            "A counterpart to moorlands that grow at higher elevations."
    ),
    COASTAL_HEATHLAND(
        HEATHLAND,
        "Coastal heathland",
        "A heathland formed near sea headland " +
            "where the constant spray of salt combined with high winds cause the soil to be nutrient poor."
    ),
    MOORLAND(
        SHRUBLAND,
        "Moorland",
        "A shrubland found in upland areas characterized by low-growing vegetation. " +
            "A counterpart to heathlands that grow at lower elevations"
    ),
    HEATHER_MOORLAND(MOORLAND, "Heather moorland", "A moorland dominated by heather."),

    WETLAND(null, "Wetland", "A habitat characterized by areas flooded or saturated with water."),
    PEATLAND(WETLAND, "Peatland", "A wetland whose soils consist of decaying plants, forming peat."),
    FEN(PEATLAND, "Fen", "A peat-accumulating wetland with a basic pH."),
    BOG(PEATLAND, "Bog", "A peat-accumulating wetland with an acidic pH."),
    MARSH(
        WETLAND,
        "Marsh",
        "A wetland dominated by non-woody low-lying grasses and reeds, featuring seasonally waterlogged terrain. " +
            "Marshes can be commonly found near lakes and streams."
    ),

    FOREST(
        null,
        "Forest",
        "A dense community of trees with a canopy cover of more than 60%. " +
            "Unlike woodlands, there is much shade for grasses or shrubs. " +
            "Instead, the forest floor is littered with decaying plants, ferns and mosses."
    ),
    CONIFEROUS_FOREST(
        FOREST,
        "Coniferous forest",
        "A forest dominated by evergreen, needle-bearing trees. " +
            "The canopy, combined with the thicker, more acidic needles means coniferous forests are " +
            "dominated purely by resilient mosses and fungi."
    ),
    WOODLAND(
        null,
        "Woodland",
        "A sparse forest (>60% canopy cover) with a light canopy that still allows light to reach the floor. " +
            "This results in woodlands often supporting a thick layer of grasses unlike forests."
    ),
    BROADLEAF_WOODLAND(
        WOODLAND,
        "Broadleaf woodland",
        "A woodland characterized by trees with wide flat leaves. " +
            "The undergrowth usually consists of grasses, ferns and wildflowers."
    ),
    CONIFEROUS_WOODLAND(
        WOODLAND,
        "Coniferous woodland",
        "A woodland characterized by trees with evergreen, needle-bearing leaves. " +
            "Despite being woodlands and not full forests, " +
            "corniferous woodland doesn't often have grass or other shrubs growing on the floor. " +
            "This is due to the needles they drop being thick, waxy, acidic and dry, " +
            "taking a relatively long time to decay."
    ),
    MIXED_WOODLAND(WOODLAND, "Mixed woodland", "A woodland neither dominated by coniferous nor broadleaf trees."),
    BOCAGE(
        null,
        "Bocage",
        "A man made habitat characterized by straight dense lines of trees or shrubs " +
            "separating pastures and grasslands."
    );

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

    public String getDescription() {
        return description;
    }

    public boolean isSubHabitatOf(Habitat habitat) {
        if (this == habitat) return true;
        if (this.parent == null) return false;
        return this.parent.isSubHabitatOf(habitat);
    }
}
