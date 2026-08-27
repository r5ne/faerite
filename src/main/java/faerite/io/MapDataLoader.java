package faerite.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import faerite.model.MapModel;
import faerite.atlas.map.MapView;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/// Loads various forms of data associated with a map.
public final class MapDataLoader {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private MapDataLoader() {}

    /// Loads the specified .json file as a MapModel object.
    /// @param mapId The id of the map model to load.
    /// @return A MapModel object representation of the .json file.
    public static MapModel loadMapModel(String mapId) {
        String resourcePath = "/mapdata/" + mapId + ".json";
        try (InputStream stream = MapView.class.getResourceAsStream(resourcePath)) {
            if (stream == null) {
                throw new IllegalArgumentException("Resource not found at: " + resourcePath);
            }
            return objectMapper.readValue(stream, MapModel.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load map model: " + resourcePath, e);
        }
    }

    /// Loads the JavaFX Image from an image file.
    /// @param fileName The file name of the image to load from the disk.
    /// @return The image object of the file.
    public static Image loadImage(String fileName) {
        String path = "/maps/" + fileName;
        InputStream stream = MapView.class.getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalArgumentException("No file exists at: " + path);
        }
        return new Image(stream);
    }

    /// Loads the AWT BufferedImage from an image file.
    /// @param fileName The file name of the image to load from the disk.
    /// @return The buffered image object of the file.
    public static BufferedImage loadBufferedImage(String fileName) {
        String path = "/maps/" + fileName;
        try (var stream = MapView.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalArgumentException("No file exists at: " + path);
            }
            return ImageIO.read(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String loadMarkdown(String fileName) {
        String path = "/mapinfo/" + fileName;

        try (var stream = MapView.class.getResourceAsStream(path)) {
            if (stream == null) {
                System.err.println("Failed to load Markdown: " + path);
                return "";
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Failed to load Markdown: " + path);
            return "Error loading content from " + path;
        }
    }

    public static String nameToFileName(String name) {
        return name.replace(" ", "-").toLowerCase();
    }
}
