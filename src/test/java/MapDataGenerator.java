import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import faerite.model.MapModel;
import faerite.model.RegionData;
import faerite.model.RegionSelectionModel;
import faerite.model.RegionType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class MapDataGenerator {

    private static final String OUTPUT_PATH = "src/main/resources/mapdata/";
    private static final Set<MapModel> mapModels = new HashSet<>();

    static void main() throws IOException {
        Files.createDirectories(Path.of(OUTPUT_PATH));

        ObjectMapper objectMapper = JsonMapper.builder()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .build();

        createMapModels();

        for (MapModel mapModel : mapModels) {
            writeMapModel(objectMapper, mapModel);
        }
    }

    public static void writeMapModel(ObjectMapper objectMapper, MapModel mapModel) {
        Path filePath = Path.of(MapDataGenerator.OUTPUT_PATH + mapModel.fileName() + ".json");
        try {
            objectMapper.writeValue(filePath.toFile(), mapModel);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write map model: " + filePath, e);
        }
    }

    private static void createMapModels() {
        var ireland = regionSelectionModelFactory("Ireland", RegionType.ISLAND, 0xffffffff, false, null);
        var greatBritain = regionSelectionModelFactory("Great Britain", RegionType.ISLAND, 0xff000000, false, null);
        var isleOfMan = regionSelectionModelFactory("Isle of Man", RegionType.ISLAND, 0xffff0000, true, null);

        mapModelFactory("British Isles", RegionType.ARCHIPELAGO, Set.of(greatBritain, ireland, isleOfMan));
    }

    private static RegionSelectionModel regionSelectionModelFactory(
        String name,
        RegionType regionType,
        int maskColor,
        boolean hasSubMap,
        Set<RegionSelectionModel> regions
    ) {
        var regionData = new RegionData(name, regionType);
        MapModel mapModel = hasSubMap ? mapModelFactory(name, regionType, regions) : null;
        return new RegionSelectionModel(regionData, maskColor, hasSubMap ? mapModel.fileName() : null);
    }

    private static MapModel mapModelFactory(String name, RegionType regionType, Set<RegionSelectionModel> regions) {
        String fileName = name.replace(" ", "-").toLowerCase();
        String filePath = String.format("%s%s.png", OUTPUT_PATH, fileName);

        int[] imageDimensions = getImageDimensions(filePath);

        RegionData mapRegionData = new RegionData(name, regionType);

        var mapModel = new MapModel(fileName, imageDimensions[0], imageDimensions[1], mapRegionData, regions);
        mapModels.add(mapModel);

        return mapModel;
    }

    private static int[] getImageDimensions(String imageFilePath) {
        Path imagePath = Path.of(imageFilePath);
        if (!Files.exists(imagePath)) {
            throw new RuntimeException("Image file not found on filesystem: " + imagePath.toAbsolutePath());
        }

        try (InputStream stream = Files.newInputStream(imagePath)) {
            try (ImageInputStream input = ImageIO.createImageInputStream(stream)) {
                ImageReader reader = ImageIO.getImageReaders(input).next();
                try {
                    reader.setInput(input);
                    int width = reader.getWidth(0);
                    int height = reader.getHeight(0);
                    return new int[] { width, height };
                } finally {
                    reader.dispose();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image dimensions for " + imageFilePath, e);
        }
    }
}
