package data.datasets;

import data.regiondata.RegionDataBuilder;
import faerite.model.RegionType;
import java.util.Set;

public class BritishIslesRegionDataset implements RegionDataset {

    @Override
    public Set<RegionDataBuilder> getRegionData() {
        return Set.of(
            new RegionDataBuilder("British Isles", RegionType.ARCHIPELAGO).wikidataId("Q38272"),

            new RegionDataBuilder("Great Britain", RegionType.ISLAND).wikidataId("Q23666"),
            new RegionDataBuilder("Isle of Wight", RegionType.ISLAND).wikidataId("Q9679"),

            new RegionDataBuilder("Ireland", RegionType.ISLAND).wikidataId("Q22890"),

            new RegionDataBuilder("Isle of Man", RegionType.ISLAND_GROUP).wikidataId("Q9676").area(570),
            new RegionDataBuilder("Isle of Man", RegionType.ISLAND)
                .id("isle-of-man-island")
                .wikidataId("Q27508141")
                .population(84521)
                .area(570),
            new RegionDataBuilder("Calf of Man", RegionType.ISLAND).wikidataId("Q125389"),

            new RegionDataBuilder("Channel Islands", RegionType.ARCHIPELAGO).wikidataId("Q42314"),
            new RegionDataBuilder("Jersey", RegionType.ISLAND_GROUP).wikidataId("Q15706498"),
            new RegionDataBuilder("Guernsey", RegionType.ISLAND_GROUP).wikidataId("Q3311985"),
            new RegionDataBuilder("Alderney", RegionType.ISLAND_GROUP).wikidataId("Q179313"),
            new RegionDataBuilder("Chausey", RegionType.ARCHIPELAGO).wikidataId("Q292600").population(30).area(1.825),
            new RegionDataBuilder("Sark", RegionType.ISLAND_GROUP).wikidataId("Q3405693"),
            new RegionDataBuilder("Herm", RegionType.ISLAND_GROUP).wikidataId("Q202023"),
            new RegionDataBuilder("Jethou", RegionType.ISLAND_GROUP).wikidataId("Q898856"),
            new RegionDataBuilder("Les Écrehous", RegionType.ISLAND_GROUP)
                .id("les-ecrehous")
                .wikidataId("Q776075")
                .area(0.2),
            new RegionDataBuilder("Les Minquiers", RegionType.ISLAND_GROUP).wikidataId("Q1435852").area(0.1),
            new RegionDataBuilder("Les Casquets", RegionType.ROCK_GROUP).wikidataId("Q1048187").area(0.072),

            new RegionDataBuilder("Isles of Scilly", RegionType.ARCHIPELAGO).wikidataId("Q180209"),
            new RegionDataBuilder("Shetland", RegionType.ARCHIPELAGO).wikidataId("Q47134"),
            new RegionDataBuilder("Orkney", RegionType.ARCHIPELAGO).wikidataId("Q100166")
        );
    }
}
