package faerite.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import faerite.view.MapView;
import java.io.IOException;
import java.io.InputStream;

/// Loads various forms of data associated with a map.
public final class MapDataLoader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MapDataLoader() {}

    /// Loads the specified .json file as a MapModel object.
    /// @param mapModelFileName The full file name of the .json file to load.
    /// @return A MapModel object representation of the .json file.
    public static MapModel loadMapModel(String mapModelFileName) {
        String resourcePath = "/mapdata/" + mapModelFileName;
        try (InputStream stream = MapView.class.getResourceAsStream(resourcePath)) {
            if (stream == null) {
                throw new IllegalArgumentException("Resource not found at: " + resourcePath);
            }
            return objectMapper.readValue(stream, MapModel.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load map model: " + resourcePath, e);
        }
    }
}
