import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import faerite.io.AssetPaths;
import faerite.model.MapModel;
import faerite.model.RegionDataModel;
import data.mapdata.ImageMetadataReader;
import data.datasets.BritishIslesDataset;
import data.datasets.ChannelIslandsDataset;
import data.datasets.IsleOfManDataset;
import data.datasets.MapDataset;
import data.datasets.BritishIslesRegionDataset;
import data.datasets.RegionDataset;

static final String RESOURCES_PATH = "src/main/resources";
static final ObjectMapper objectMapper = JsonMapper.builder()
    .enable(SerializationFeature.INDENT_OUTPUT)
    .configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true)
    .build();

void main() {
    createMapModels(objectMapper);
    createRegionData(objectMapper);
}

static void createMapModels(ObjectMapper objectMapper) {
    List<MapDataset> allDatasets = List.of(new BritishIslesDataset(), new ChannelIslandsDataset(), new IsleOfManDataset());

    for (MapDataset dataset : allDatasets) {
        int[] imageSize = ImageMetadataReader.getDimensions(Path.of(RESOURCES_PATH, AssetPaths.getMapImagePath(dataset.id())));
        MapModel mapModel = new MapModel(dataset.id(), imageSize[0], imageSize[1], dataset.buildRegions());
        writeData(mapModel, Path.of(RESOURCES_PATH, AssetPaths.getMapDataPath(dataset.id())), objectMapper);
    }
}

static void createRegionData(ObjectMapper objectMapper) {
    Set<RegionDataset> allDatasets = Set.of(new BritishIslesRegionDataset());

    for (RegionDataset regionDataset : allDatasets) {
        for (RegionDataModel regionData : regionDataset.getRegionData()) {
            Path jsonPath = Path.of(RESOURCES_PATH, AssetPaths.getRegionDataPath(regionData.id()));
            writeData(regionData, jsonPath, objectMapper);
        }
    }
}

static <T> void writeData(T data, Path jsonPath, ObjectMapper objectMapper) {
    try {
        objectMapper.writeValue(jsonPath.toFile(), data);
    } catch (IOException e) {
        throw new RuntimeException("Failed writing JSON for " + jsonPath, e);
    }
}
