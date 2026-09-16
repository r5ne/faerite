package data.datasets;

import data.regiondata.RegionDataBuilder;
import faerite.model.RegionDataModel;
import faerite.model.RegionType;

import java.util.Set;

public class BritishIslesRegionDataset implements RegionDataset {

    @Override
    public Set<RegionDataModel> getRegionData() {
        return Set.of(
            new RegionDataBuilder("British Isles", RegionType.ARCHIPELAGO).wikidataId("Q38272").build(),

            new RegionDataBuilder("Great Britain", RegionType.ISLAND).wikidataId("Q749109").build(),
            new RegionDataBuilder("Isle of Wight", RegionType.ISLAND).wikidataId("Q9679").build(),

            new RegionDataBuilder("Ireland", RegionType.ISLAND).wikidataId("Q22890").build(),

            new RegionDataBuilder("Isle of Man", RegionType.ISLAND_GROUP).wikidataId("Q9676").build(),
            new RegionDataBuilder("Isle of Man", RegionType.ISLAND).id("isle-of-man-island").wikidataId("Q27508141").build(),
            new RegionDataBuilder("Calf of Man", RegionType.ISLAND).wikidataId("Q125389").build(),

            new RegionDataBuilder("Channel Islands", RegionType.ARCHIPELAGO).wikidataId("Q42314").build(),
            new RegionDataBuilder("Jersey", RegionType.ISLAND_GROUP).wikidataId("Q15706498").build(),
            new RegionDataBuilder("Guernsey", RegionType.ISLAND_GROUP).wikidataId("Q3311985").build(),
            new RegionDataBuilder("Sark", RegionType.ISLAND_GROUP).wikidataId("Q3405693").build(),
            new RegionDataBuilder("Herm", RegionType.ISLAND_GROUP).wikidataId("Q202023").build(),
            new RegionDataBuilder("Jethou", RegionType.ISLAND_GROUP).wikidataId("Q898856").build(),
            new RegionDataBuilder("Les Écrehous", RegionType.ISLAND_GROUP).id("les-ecrehous").wikidataId("Q776075").build(),
            new RegionDataBuilder("Les Minquiers", RegionType.ISLAND_GROUP).wikidataId("Q1435852").build(),
            new RegionDataBuilder("Les Casquets", RegionType.ROCK_GROUP).wikidataId("Q1048187").build(),

            new RegionDataBuilder("Isles of Scilly", RegionType.ARCHIPELAGO).wikidataId("Q180209").build(),
            new RegionDataBuilder("Shetland", RegionType.ARCHIPELAGO).wikidataId("Q47134").build(),
            new RegionDataBuilder("Orkney", RegionType.ARCHIPELAGO).wikidataId("Q100166").build()
        );
    }
}
