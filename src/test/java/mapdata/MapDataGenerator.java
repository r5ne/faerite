package mapdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import faerite.model.MapModel;
import faerite.model.RegionData;
import faerite.model.RegionSelectionModel;
import faerite.model.RegionType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import mapdata.datasets.BritishIslesDataset;
import mapdata.datasets.ChannelIslandsDataset;
import mapdata.datasets.IsleOfManDataset;
import mapdata.datasets.MapDataset;
import org.jetbrains.annotations.Nullable;

public class MapDataGenerator {

    private static final Path MAP_IMAGE_PATH = Path.of("src/main/resources/maps/");
    private static final Path OUTPUT_PATH = Path.of("src/main/resources/mapdata/");
    private static final ObjectMapper objectMapper = JsonMapper.builder().enable(SerializationFeature.INDENT_OUTPUT).build();

    static void main() throws IOException {
        createMapModels();
    }

    private static void createMapModels() throws IOException {
        Files.createDirectories(OUTPUT_PATH);

        List<MapDataset> allDatasets = List.of(
                new BritishIslesDataset(),
                new ChannelIslandsDataset(),
                new IsleOfManDataset()
        );

        for (MapDataset dataset : allDatasets) {
            writeMapModel(dataset.mapName(), dataset.regionType(), dataset.buildRegions());
        }
    }

    private static void writeMapModel(String name, RegionType type, @Nullable Set<RegionSelectionModel> regions) {
        String fileName = name.replace(" ", "-").toLowerCase();
        Path imagePath = MAP_IMAGE_PATH.resolve(fileName + ".png");
        int[] imageSize = ImageMetadataReader.getDimensions(imagePath);

        RegionData mapRegionData = new RegionData(name, type);
        MapModel mapModel = new MapModel(fileName, imageSize[0], imageSize[1], mapRegionData, regions);

        Path jsonPath = OUTPUT_PATH.resolve(fileName + ".json");
        try {
            objectMapper.writeValue(jsonPath.toFile(), mapModel);
        } catch (IOException e) {
            throw new RuntimeException("Failed writing JSON for " + name, e);
        }
    }
}
