package data.datasets;

import data.regiondata.RegionDataBuilder;

import java.util.Set;

public interface RegionDataset {
    Set<RegionDataBuilder> getRegionData();
}
