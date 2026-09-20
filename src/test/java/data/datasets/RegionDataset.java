package data.datasets;

import data.regiondata.RegionDataBuilderConfig;

import java.util.Set;

public interface RegionDataset {
    Set<RegionDataBuilderConfig> getRegionData();
}
