package data.datasets;

import data.regiondata.RegionDataBuilder;
import faerite.model.RegionDataModel;
import java.util.Set;

public class BritishIslesRegionDataset implements RegionDataset {

    @Override
    public Set<RegionDataModel> getRegionData() {
        return Set.of(
            new RegionDataBuilder("British Isles").wikidataId("Q38272").build(),

            new RegionDataBuilder("Great Britain").build(),
            new RegionDataBuilder("Isle of Wight").build(),

            new RegionDataBuilder("Ireland").build(),

            new RegionDataBuilder("Isle of Man").build(),
            new RegionDataBuilder("Isle of Man").id("isle-of-man-island").build(),
            new RegionDataBuilder("Calf of Man").build(),

            new RegionDataBuilder("Channel Islands").build(),
            new RegionDataBuilder("Jersey").build(),
            new RegionDataBuilder("Guernsey").build(),
            new RegionDataBuilder("Sark").build(),
            new RegionDataBuilder("Herm").build(),
            new RegionDataBuilder("Jethou").build(),
            new RegionDataBuilder("Les Écrehous").id("les-ecrehous").build(),
            new RegionDataBuilder("Les Minquiers").build(),
            new RegionDataBuilder("Les Casquets").build(),

            new RegionDataBuilder("Isles of Scilly").build(),
            new RegionDataBuilder("Shetland").build(),
            new RegionDataBuilder("Orkney").build()
        );
    }
}
