package mapdata.datasets;

import faerite.model.RegionSelectionModel;
import faerite.model.RegionType;
import java.util.Set;
import mapdata.RegionBuilder;

public class IsleOfManDataset implements MapDataset {

    @Override
    public String mapName() {
        return "Isle of Man";
    }

    @Override
    public RegionType regionType() {
        return RegionType.ISLAND_GROUP;
    }

    @Override
    public Set<RegionSelectionModel> buildRegions() {
        return Set.of(
            new RegionBuilder("Isle of Man", RegionType.ISLAND, 0xff000000).build(),
            new RegionBuilder("Calf of Man", RegionType.ISLAND, 0xffffffff).build()
        );
    }
}
