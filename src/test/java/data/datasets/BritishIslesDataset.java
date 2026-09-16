package data.datasets;

import faerite.model.RegionSelectionModel;
import java.util.Set;

import data.mapdata.RegionBuilder;

public class BritishIslesDataset implements MapDataset {

    @Override
    public String id() {
        return "british-isles";
    }

    @Override
    public Set<RegionSelectionModel> buildRegions() {
        return Set.of(
            new RegionBuilder("great-britain", 0xff000000).build(),
            new RegionBuilder("ireland", 0xffffffff).build(),
            new RegionBuilder("isle-of-wight", 0xff2d85f7).build(),

            new RegionBuilder("isle-of-man", 0xffff0000).hasSubMap().build(),

            new RegionBuilder("channel-islands", 0xffffd700).hasSubMap().build(),
            new RegionBuilder("isles-of-scilly", 0xfff7af1b).build(),
            new RegionBuilder("shetland", 0xff0000ff).build(),
            new RegionBuilder("orkney", 0xffffff00).build()
        );
    }
}
