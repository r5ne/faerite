package mapdata.datasets;

import faerite.model.RegionSelectionModel;
import faerite.model.RegionType;
import java.util.Set;
import mapdata.RegionBuilder;

public class ChannelIslandsDataset implements MapDataset {

    @Override
    public String mapName() {
        return "Channel Islands";
    }

    @Override
    public RegionType regionType() {
        return RegionType.ARCHIPELAGO;
    }

    @Override
    public Set<RegionSelectionModel> buildRegions() {
        return Set.of(
            new RegionBuilder("Jersey", RegionType.ISLAND_GROUP, 0xff000000).build(),
            new RegionBuilder("Guernsey", RegionType.ISLAND_GROUP, 0xffffffff).build(),
            new RegionBuilder("Alderney", RegionType.ISLAND_GROUP, 0xffff0000).build(),
            new RegionBuilder("Chausey", RegionType.ISLAND_GROUP, 0xffffff00).build(),

            new RegionBuilder("Sark", RegionType.ISLAND_GROUP, 0xff55c449).build(),
            new RegionBuilder("Herm", RegionType.ISLAND_GROUP, 0xffffffb6).build(),
            new RegionBuilder("Jethou", RegionType.ISLAND_GROUP, 0xff36982b).build(),

            new RegionBuilder("Les Écrehous", RegionType.ISLAND_GROUP, 0xff00ffff).build(),
            new RegionBuilder("Les Minquiers", RegionType.ISLAND_GROUP, 0xff0000ff).build(),

            new RegionBuilder("Les Casquets", RegionType.ROCK_GROUP, 0xff343434).build()
        );
    }
}
