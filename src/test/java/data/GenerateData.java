import data.MapDataGenerator;
import data.RegionDataGenerator;
import data.RegionDataSyncMode;


void main() {
    MapDataGenerator.createMapModels();
    RegionDataGenerator.createRegionData(RegionDataSyncMode.IF_MISSING);
}
