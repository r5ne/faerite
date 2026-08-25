package faerite.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MapDataLoader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MapDataLoader() {}

    public static MapModel loadMapModel(String mapModelFileName) {
        Path filePath = Path.of("src/main/resources/mapdata/" + mapModelFileName);
        try (var reader = Files.newBufferedReader(filePath)) {
            return objectMapper.readValue(reader, MapModel.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Config file not found at:" + filePath.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
