package data.datasets;

import faerite.model.RegionSelectionModel;

import java.util.Set;

public interface MapDataset {
    String id();
    Set<RegionSelectionModel> buildRegions();
}
