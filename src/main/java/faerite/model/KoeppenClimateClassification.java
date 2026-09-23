package faerite.model;

public enum KoeppenClimateClassification {
    AF("Af"),
    AM("Am"),
    AW("Aw"),
    AS("As"),

    BWh("BWh"),
    BWk("BWk"),
    BSh("BSk"),
    BSk("BSk"),

    Cwa("Cwa"),
    Cwb("Cwb"),
    Cwc("Cwc"),
    Cfa("Cfa"),
    Cfb("Cfb"),
    Cfc("Cfc"),
    Csa("Csa"),
    Csb("Csb"),
    Csc("Csc"),

    Dwa("Dwa"),
    Dwb("Dwb"),
    Dwc("Dwc"),
    Dwd("Dwd"),
    Dfa("Dfa"),
    Dfb("Dfb"),
    Dfc("Dfc"),
    Dfd("Dfd"),
    Dsa("Dsa"),
    Dsb("Dsb"),
    Dsc("Dsc"),
    Dsd("Dsd"),

    ET("ET"),
    EF("EF");

    private final String displayName;

    KoeppenClimateClassification(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}