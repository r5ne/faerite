import data.DataSyncMode;
import data.MapDataGenerator;
import data.RegionDataGenerator;

void main() {
    MapDataGenerator mapData = new MapDataGenerator();
    RegionDataGenerator.createRegionData(DataSyncMode.IF_MISSING, mapData.getRegionHierarchyTree());
}
