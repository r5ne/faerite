package mapdata.datasets;

import faerite.model.RegionSelectionModel;
import faerite.model.RegionType;

import java.util.Set;

public interface MapDataset {
    String mapName();
    RegionType regionType();
    Set<RegionSelectionModel> buildRegions();
}
