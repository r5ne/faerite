import data.DataSyncMode;
import data.MapDataGenerator;
import data.RegionDataGenerator;


void main() {
    MapDataGenerator.createMapModels();
    RegionDataGenerator.createRegionData(DataSyncMode.IF_MISSING);
}
