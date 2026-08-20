package faerite.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MapDataLoader {
    private static final String MAP_MODEL_FILE_NAME = "british-isles.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MapDataLoader() {}

    public static void writeMapModel(MapModel mapModel) {
        Path filePath = Path.of("src/main/resources/mapdata/" + MAP_MODEL_FILE_NAME);
        try (var writer = Files.newBufferedWriter(filePath)) {
            objectMapper.writeValue(filePath.toFile(), mapModel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MapModel loadMapModel() {
        Path filePath = Path.of("src/main/resources/mapdata/" + MAP_MODEL_FILE_NAME);
        try (var reader = Files.newBufferedReader(filePath)) {
            return objectMapper.readValue(reader, MapModel.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
