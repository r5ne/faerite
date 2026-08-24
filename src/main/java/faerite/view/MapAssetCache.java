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

public final class MapAssetCache {

    private static final int BORDER_SIZE = 2;

    private static final Map<String, Image> imageCache = new HashMap<>();
    private static final Map<String, BufferedImage> bufferedImageCache = new HashMap<>();
    private static final Map<String, Map<Integer, boolean[]>> mapBordersCache = new HashMap<>();

    private MapAssetCache() {}

    public static Image getImage(String fileName) {
        return imageCache.computeIfAbsent(fileName, MapAssetCache::loadImage);
    }

    public static BufferedImage getBufferedImage(String fileName) {
        return bufferedImageCache.computeIfAbsent(fileName, MapAssetCache::loadBufferedImage);
    }

    public static Map<Integer, boolean[]> getMapBorders(MapModel mapModel, Image borderMaskImage) {
        return mapBordersCache.computeIfAbsent(mapModel.name(), k ->
            MaskUtils.createBorderMasks(
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
