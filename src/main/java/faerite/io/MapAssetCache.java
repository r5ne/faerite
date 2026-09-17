package faerite.io;

import faerite.atlas.map.BorderGenerator;
import faerite.model.RegionSelectionModel;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.scene.image.Image;

/// Stores and allows for interacting with caches for expensive map assets.
public final class MapAssetCache {

    private static final Map<String, Image> imageCache = new HashMap<>();
    private static final Map<String, BufferedImage> bufferedImageCache = new HashMap<>();
    private static final Map<String, Map<Integer, int[]>> mapBordersCache = new HashMap<>();

    private static final Map<String, String> markdownCache = new HashMap<>();

    private MapAssetCache() {}

    /// Gets the JavaFX Image from an image file.
    /// @param path The path of the image to query from the cache or load.
    /// @return The image object of the file.
    public static Image getImage(String path) {
        return imageCache.computeIfAbsent(path, MapDataLoader::loadImage);
    }

    /// Gets the AWT BufferedImage from an image file.
    /// @param path The path of the image to query from the cache of load.
    /// @return The buffered image object of the file.
    public static BufferedImage getBufferedImage(String path) {
        return bufferedImageCache.computeIfAbsent(path, MapDataLoader::loadBufferedImage);
    }

    /// Gets the complete map of sparse indices for all the borders of the regions in a map model.
    /// @param mapModelId The id of the map model defining the mask colors from which to get the sparse indices.
    /// @param borderMaskImage The image containing the border mask.
    /// @return A map of sparse indices for all the borders.
    public static Map<Integer, int[]> getMapBorders(
        String mapModelId,
        Set<RegionSelectionModel> mapRegions,
        Image borderMaskImage,
        int borderSize
    ) {
        return mapBordersCache.computeIfAbsent(mapModelId, k ->
            BorderGenerator.createBorderMasks(
                borderMaskImage,
                mapRegions.stream().map(RegionSelectionModel::maskColor).collect(Collectors.toSet()),
                borderSize
            )
        );
    }

    public static String getMarkdown(String fileName) {
        return markdownCache.computeIfAbsent(fileName, MapDataLoader::loadMarkdown);
    }
}
