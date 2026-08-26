package faerite.view;

import faerite.model.MapModel;
import faerite.model.RegionSelectionModel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/// Stores and allows for interacting with caches for expensive map assets.
public final class MapAssetCache {

    private static final int BORDER_SIZE = 2;

    private static final Map<String, Image> imageCache = new HashMap<>();
    private static final Map<String, BufferedImage> bufferedImageCache = new HashMap<>();
    private static final Map<String, Map<Integer, int[]>> mapBordersCache = new HashMap<>();

    private MapAssetCache() {}

    /// Gets the JavaFX Image from an image file from the cache or disk.
    /// @param fileName The file name of the image to query from the cache or load.
    /// @return The image object of the file.
    public static Image getImage(String fileName) {
        return imageCache.computeIfAbsent(fileName, MapAssetCache::loadImage);
    }

    /// Gets the AWT BufferedImage from the cache or disk.
    /// @param fileName The file name of the image to query from the cache of load.
    /// @return The buffered image object of the file.
    public static BufferedImage getBufferedImage(String fileName) {
        return bufferedImageCache.computeIfAbsent(fileName, MapAssetCache::loadBufferedImage);
    }

    /// Gets the complete map of sparse indices for the borders of the regions in a map model from the cache or disk.
    /// @param mapModel The map model defining the mask colors from which to get the sparse indices.
    /// @param borderMaskImage The image containing the border mask.
    /// @return A map of sparse indices for all the borders.
    public static Map<Integer, int[]> getMapBorders(MapModel mapModel, Image borderMaskImage) {
        return mapBordersCache.computeIfAbsent(mapModel.fileName(), k ->
            BorderGenerator.createBorderMasks(
                borderMaskImage,
                mapModel.regions().stream().map(RegionSelectionModel::maskColor).collect(Collectors.toSet()),
                BORDER_SIZE
            )
        );
    }

    private static Image loadImage(String fileName) {
        String path = String.format("/mapdata/" + fileName);
        InputStream stream = MapView.class.getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalArgumentException("No file exists at: " + path);
        }
        return new Image(stream);
    }

    private static BufferedImage loadBufferedImage(String fileName) {
        String path = String.format("/mapdata/" + fileName);
        try (var stream = MapView.class.getResourceAsStream(path)) {
            if (stream == null) {
                throw new IllegalArgumentException("No file exists at: " + path);
            }
            return ImageIO.read(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
