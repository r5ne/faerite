package data.regiondata;


import java.util.function.Consumer;

public record RegionDataBuilderConfig(
    String name,
    String id,
    String wikidataId,
    Consumer<RegionDataBuilder> overrides
) {
    public RegionDataBuilderConfig(String name, String wikidataId, Consumer<RegionDataBuilder> overrides) {
        this(name, nameToId(name), wikidataId, overrides);
    }

    public RegionDataBuilderConfig(String name, String id, String wikidataId) {
        this(name, id, wikidataId, null);
    }

    public RegionDataBuilderConfig(String name, String wikidataId) {
        this(name, nameToId(name), wikidataId, null);
    }

    private static String nameToId(String name) {
        return name.toLowerCase().replace(" ", "-");
    }
}
