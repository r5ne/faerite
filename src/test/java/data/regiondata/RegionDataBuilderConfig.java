package data.regiondata;


import java.util.function.Consumer;

public record RegionDataBuilderConfig(
    String name,
    String id,
    String wikidataId,
    Consumer<RegionDataBuilder> overrides
) {
    public RegionDataBuilderConfig(String name, String wikidataId, Consumer<RegionDataBuilder> overrides) {
        this(nameToId(name), name, wikidataId, overrides);
    }

    private static String nameToId(String name) {
        return name.toLowerCase().replace(" ", "-");
    }
}
