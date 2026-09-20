package faerite.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import faerite.model.MapModel;
import faerite.atlas.map.MapView;
import faerite.model.RegionDataModel;
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
    /// @param path The path of the map model to load.
    /// @return A MapModel object representation of the .json file.
    public static MapModel loadMapModel(String path) {
        return loadJson(path, MapModel.class);
    }

    public static RegionDataModel loadRegionDataModel(String path) {
        return loadJson(path, RegionDataModel.class);
    }

    /// Loads the JavaFX Image from an image file.
    /// @param path The path of the image to load from the disk.
    /// @return The image object of the file.
    public static Image loadImage(String path) {
        InputStream stream = MapView.class.getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalArgumentException("No file exists at: " + path);
        }
        return new Image(stream);
    }

    /// Loads the AWT BufferedImage from an image file.
    /// @param path The path of the image to load from the disk.
    /// @return The buffered image object of the file.
    public static BufferedImage loadBufferedImage(String path) {
        try (var stream = MapView.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalArgumentException("No file exists at: " + path);
            }
            return ImageIO.read(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String loadMarkdown(String path) {
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

    private static <T> T loadJson(String path, Class<T> classType) {
        try (InputStream stream = MapView.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalArgumentException("Resource not found at: " + path);
            }
            return objectMapper.readValue(stream, classType);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load JSON: " + path, e);
        }
    }
}
