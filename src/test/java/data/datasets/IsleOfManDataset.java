package data.datasets;

import faerite.model.RegionSelectionModel;

import java.util.Set;
import data.mapdata.RegionBuilder;

public class IsleOfManDataset implements MapDataset {

    @Override
    public String id() {
        return "isle-of-man";
    }

    @Override
    public Set<RegionSelectionModel> buildRegions() {
        return Set.of(
            new RegionBuilder("isle-of-man-island", 0xff000000).build(),
            new RegionBuilder("calf-of-man", 0xffffffff).build()
        );
    }
}
