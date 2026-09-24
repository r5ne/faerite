package faerite.model;

public enum KoeppenClimateClassification {
    AF(
        "Af",
        "Tropical rainforest",
        "No pronounced dry season; every month averages at least 60 mm of precipitation. " +
            "Temperatures remain warm and stable year-round, typically above 18°C in the coldest month. " +
            "Seasonality is driven more by rainfall intensity than by temperature. " +
            "Found in equatorial belts with consistently high humidity."
    ),
    AM(
        "Am",
        "Tropical monsoon",
        "Features a short dry season, but annual rainfall is high enough to offset the deficit. " +
            "Precipitation is strongly concentrated in a wet monsoon period, often with extreme monthly totals. " +
            "Temperatures stay uniformly warm throughout the year. " +
            "The dry months are brief and less severe than in savanna climates."
    ),
    AW(
        "Aw",
        "Tropical savanna",
        "Distinguished by a clear dry season in winter, with at least one month below the 60 mm threshold. " +
            "Annual temperatures remain warm, but rainfall is markedly seasonal rather than evenly spread. " +
            "Vegetation-relevant moisture availability fluctuates strongly across the year. " +
            "Common in transitional zones between rainforest and arid climates."
    ),
    AS("As", "Tropical savanna (dry summer)", "How did you find this?"),

    BWH(
        "BWh",
        "Hot desert",
        "Extremely low annual precipitation combined with high annual mean temperatures. " +
            "Diurnal and seasonal temperature swings can be large despite the hot overall regime. " +
            "Evaporative demand generally exceeds the sparse rainfall. " +
            "Found under the subtropical ridge in places of consistent high atmospheric pressure."
    ),
    BWK(
        "BWk",
        "Cold desert",
        "Very low precipitation as in BWh, but with a cooler annual temperature regime, often including cold winters. " +
            "Aridity remains the defining feature rather than the seasonal temperature pattern. " +
            "Precipitation, where it occurs, is typically irregular and low in total amount. " +
            "Found in continental interior or high-latitude arid zones."
    ),
    BSH(
        "BSh",
        "Hot steppe",
        "Precipitation is low but somewhat higher than desert thresholds, " +
            "supporting a semi-arid rather than hyper-arid regime. " +
            "Annual temperatures are warm to hot. " +
            "Rainfall variability year to year tends to be considerable. " +
            "Represents a transitional zone between desert and subhumid climates."
    ),
    BSK(
        "BSk",
        "Cold steppe",
        "Semi-arid precipitation totals similar to BSh, " +
            "but with a cooler annual temperature profile and often cold winters. " +
            "Continentality tends to be more pronounced than in hot steppe regions. " +
            "Moisture deficits persist despite the lower temperatures. " +
            "Common in mid-latitude continental interiors."
    ),

    CWA(
        "Cwa",
        "Monsoon-influenced subtropical",
        "Hot summers (above 22°C) paired with a distinct dry winter season, " +
            "often linked to monsoon-type circulation. " +
            "Most annual precipitation falls in the warmer months. " +
            "Winters are notably drier than summers, unlike Cfa. " +
            "These climates normally lie on southeast side of all continents, near monsoon-influenced regions."
    ),
    CWB(
        "Cwb",
        "Monsoon-influenced subtropical highland",
        "Mild summers (below 22°C, at least four months above 10°C) combined with a pronounced dry winter. " +
            "Precipitation is concentrated in the warmer half of the year. " +
            "The seasonal moisture contrast is stronger than in oceanic (Cfb) climates. " +
            "Often associated with subtropical highland or monsoon-margin settings."
    ),
    CWC(
        "Cwc",
        "Monsoon-influenced cold subtropical highland",
        "Short mild season (fewer than four months above 10°C) with a dry winter pattern. " +
            "Precipitation concentrates in the summer months. " +
            "Temperature regime is cooler than Cwa/Cwb, often reflecting elevation or higher-latitude influence. " +
            "A less common variant within the Cw group."
    ),
    CFA(
        "Cfa",
        "Humid subtropical",
        "Mild winters and hot, humid summers with precipitation distributed fairly evenly across the year, " +
            "without a formal dry season. " +
            "The warmest month typically exceeds 22°C. " +
            "Rainfall totals are usually moderate to high. " +
            "These climates normally lie on the southeast side of all continents."
    ),
    CFB(
        "Cfb",
        "Temperate oceanic",
        "Mild summers (warmest month below 22°C but with at least four months above 10°C) " +
            "and cool, not severely cold, winters. " +
            "Precipitation is fairly even throughout the year without a pronounced dry season. " +
            "Low seasonal temperature amplitude reflects strong maritime influence. " +
            "Typical of west coasts in the higher middle latitudes of continents."
    ),
    CFC(
        "Cfc",
        "Subpolar oceanic",
        "Similar to Cfb but with a shorter mild season — fewer than four months above 10°C. " +
            "Winters remain relatively mild for the latitude due to maritime moderation. " +
            "Precipitation is distributed evenly across the year. " +
            "Found at higher-latitude or more exposed maritime margins."
    ),
    CSA(
        "Csa",
        "Hot Mediterranean",
        "Dry, hot summers (warmest month above 22°C) contrast with wetter, mild winters. " +
            "The dry season falls in summer, driven by seasonal shifts in atmospheric circulation. " +
            "Annual precipitation is often modest and concentrated in the cooler months. " +
            "Characteristic of Mediterranean-type climate zones."
    ),
    CSB(
        "Csb",
        "Warm Mediterranean",
        "Dry summers as in Csa, but with cooler summer temperatures " +
            "(warmest month below 22°C, at least four months above 10°C). " +
            "Winters are mild and receive the bulk of annual precipitation. " +
            "The dry-summer pattern remains the defining seasonal feature. " +
            "Found in cooler Mediterranean or west-coast subtropical settings."
    ),
    CSC(
        "Csc",
        "Subpolar Mediterranean",
        "A rarer variant with dry summers but a short mild season (fewer than four months above 10°C). " +
            "Winters can be more pronounced than in Csa/Csb. " +
            "Precipitation still concentrates in the cooler part of the year. " +
            "Typically restricted to limited elevation-influenced or marginal locations."
    ),

    DWA(
        "Dwa",
        "Monsoon-influenced hot-summer continental",
        "Hot summers combined with a distinct dry winter season, unlike the evenly distributed Dfa. " +
            "Most precipitation falls during the warm season. " +
            "Winters are cold and comparatively dry. " +
            "Caused by the interaction of intense continental temperature variations, " +
            "the Siberian High, and the East Asian summer monsoon."
    ),
    DWB(
        "Dwb",
        "Monsoon-influenced hemiboreal",
        "Warm summers (below 22°C) paired with a dry winter and cold conditions. " +
            "Precipitation concentrates in the summer months, driven by seasonal circulation shifts. " +
            "Continentality remains pronounced. " +
            "Found in monsoon-margin continental zones."
    ),
    DWC(
        "Dwc",
        "Monsoon-influenced subpolar",
        "Short cool summers and a pronounced dry winter season. " +
            "Precipitation is concentrated in the brief warm period. " +
            "Winters are long, cold, and notably drier than summers. " +
            "Found in parts of East Asia, like China, " +
            "where the Siberian High creates the long dry winter conditions required for the climate."
    ),
    DWD(
        "Dwd",
        "Frigid monsoon-influenced subpolar",
        "Combines the extreme winter cold of Dfd (coldest month below -38°C) with a distinct dry winter season. " +
            "Summers are short and receive most of the limited annual precipitation. " +
            "Represents one of the most seasonally extreme climate types in the classification. " +
            "Restricted to specific continental interior settings with monsoon influence."
    ),
    DFA(
        "Dfa",
        "Humid hot-summer continental",
        "Hot summers (above 22°C) and cold winters with precipitation spread fairly evenly through the year. " +
            "Seasonal temperature amplitude is large, reflecting strong continentality. " +
            "No formal dry season is present. " +
            "Typical of continental interiors at mid-latitudes."
    ),
    DFB(
        "Dfb",
        "Humid hemiboreal",
        "Warm but not hot summers (below 22°C, at least four months above 10°C) " +
            "with cold winters and year-round precipitation. " +
            "Continentality remains high, producing a pronounced annual temperature range. " +
            "Rainfall is generally adequate in all seasons. " +
            "Common across continental mid to high-latitude zones."
    ),
    DFC(
        "Dfc",
        "Subpolar",
        "Short, cool summers (fewer than four months above 10°C) and long, cold winters, " +
            "with precipitation distributed evenly across the year. " +
            "Annual temperature amplitude is substantial. " +
            "Precipitation totals are often moderate rather than high. " +
            "Found in high-latitude continental interiors."
    ),
    DFD(
        "Dfd",
        "Frigid subpolar",
        "An extreme variant of Dfc with the coldest month falling below -38°C. " +
            "Summers remain short and cool, with precipitation spread through the year. " +
            "Represents some of the largest annual temperature amplitudes recorded under this system. " +
            "Restricted to the most severe continental interior locations."
    ),
    DSA(
        "Dsa",
        "Mediterranean-influenced hot-summer continental",
        "Hot summers but with the dry season occurring in summer rather than winter, an uncommon combination. " +
            "Precipitation concentrates in the cooler months. " +
            "Winters are cold, consistent with the broader D group. " +
            "This combination is relatively rare globally."
    ),
    DSB(
        "Dsb",
        "Mediterranean-influenced hemiboreal",
        "Warm (not hot) summers with a dry-summer precipitation pattern and cold winters. " +
            "Most rainfall occurs outside summer. " +
            "Continentality remains a defining feature. " +
            "A relatively uncommon variant within the D group."
    ),
    DSC(
        "Dsc",
        "Mediterranean-influenced subpolar",
        "Short, cool summers combined with a dry-summer precipitation pattern, an unusual pairing. " +
            "Winters are long and cold, typically receiving more precipitation than summer. " +
            "This is one of the rarer subtypes in the classification. " +
            "Occurs in limited continental settings with specific circulation patterns."
    ),
    DSD(
        "Dsd",
        "Mediterranean-influenced frigid subpolar",
        "Combines extreme winter cold (below -38°C in the coldest month) with a dry-summer precipitation pattern. " +
            "Summers are short, cool, and drier than the rest of the year. " +
            "This is among the least commonly observed climate subtypes, " +
            "and the rarest climate of the Köppen-Geiger classifications. " +
            "Restricted to very specific continental interior locations."
    ),

    ET(
        "ET",
        "Tundra",
        "The warmest month averages between 0°C and 10°C, " +
            "too cool to sustain the thermal thresholds used for forest-associated classifications. " +
            "Precipitation is typically low, though moisture loss is also limited by the cold. " +
            "Seasonal temperature contrast exists but summers remain brief and cool. " +
            "Found at high latitudes or high elevations."
    ),
    EF(
        "EF",
        "Polar ice cap",
        "All months average below 0°C, representing the coldest category in the classification. " +
            "Precipitation is generally very low, often falling as sparse snowfall. " +
            "There is no month with above-freezing conditions, " +
            "so no thermal growing season exists under the standard thresholds. " +
            "Found in the most persistently cold environments covered by this classification."
    );

    private final String code;
    private final String overview;
    private final String description;

    KoeppenClimateClassification(String code, String overview, String description) {
        this.code = code;
        this.overview = overview + " climate";
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getOverview() {
        return overview;
    }

    public String getDescription() {
        return description;
    }
}
