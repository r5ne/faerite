package mapdata.datasets;

import faerite.model.RegionSelectionModel;
import faerite.model.RegionType;
import java.util.Set;

import mapdata.RegionBuilder;

public class BritishIslesDataset implements MapDataset {

    @Override
    public String mapName() {
        return "British Isles";
    }

    @Override
    public RegionType regionType() {
        return RegionType.ARCHIPELAGO;
    }

    @Override
    public Set<RegionSelectionModel> buildRegions() {
        return Set.of(
            new RegionBuilder("Great Britain", RegionType.ISLAND, 0xff000000).build(),
            new RegionBuilder("Ireland", RegionType.ISLAND, 0xffffffff).build(),
            new RegionBuilder("Isle of Wight", RegionType.ISLAND, 0xff2d85f7).build(),

            new RegionBuilder("Isle of Man", RegionType.ISLAND_GROUP, 0xffff0000).subMap().build(),

            new RegionBuilder("Channel Islands", RegionType.ARCHIPELAGO, 0xffffd700).subMap().build(),
            new RegionBuilder("Isles of Scilly", RegionType.ARCHIPELAGO, 0xfff7af1b).build(),
            new RegionBuilder("Shetland", RegionType.ARCHIPELAGO, 0xff0000ff).build(),
            new RegionBuilder("Orkney", RegionType.ARCHIPELAGO, 0xffffff00).build()
        );
    }
}
