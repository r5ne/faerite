package data.regiondata;


import java.util.function.Consumer;

public record RegionDataBuilderConfig(
    String id,
    String name,
    String wikidataId,
    Consumer<RegionDataBuilder> overrides
) {
    public RegionDataBuilderConfig(String name, String wikidataId, Consumer<RegionDataBuilder> overrides) {
        this(name, nameToId(name), wikidataId, overrides);
    }

    public RegionDataBuilderConfig(String id, String name, String wikidataId) {
        this(id, name, wikidataId, null);
    }

    public RegionDataBuilderConfig(String name, String wikidataId) {
        this(nameToId(name), name, wikidataId, null);
    }

    private static String nameToId(String name) {
        return name.toLowerCase().replace(" ", "-");
    }
}
