import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MapView extends Pane {
    private final MapViewModel viewModel;

    private final ImageView mapImageView = new ImageView();
    private final Canvas mapBorderCanvas;
    private final Scale mapScale = new Scale();
    private final Translate mapTranslate = new Translate();

    private Image hitboxMaskImage;
    private Image borderMaskImage;
    private Map<Integer, boolean[]> borderCache;

    public MapView(MapViewModel viewModel) {
        this.viewModel = viewModel;

        // nearest-neighbour interp for pixel art
        mapImageView.setSmooth(false);
        mapImageView.getTransforms().addAll(mapScale, mapTranslate);
        getChildren().add(mapImageView);

        mapBorderCanvas = new Canvas(viewModel.getCurrentMap().width(), viewModel.getCurrentMap().height());
        mapBorderCanvas.getTransforms().addAll(mapScale, mapTranslate);
        getChildren().add(mapBorderCanvas);

        updateMapData(viewModel.getCurrentMap());
        borderCache = cacheBorders(borderMaskImage, 2);

        // Keep the background synced with the oceanColor.
        backgroundProperty().bind(Bindings.createObjectBinding(() ->
                        new Background(new BackgroundFill(
                                viewModel.getOceanColorProperty().get(),
                                CornerRadii.EMPTY,
                                Insets.EMPTY
                        )),
                viewModel.getOceanColorProperty()
        ));

        // Update the map, border and hitbox masks when the currentMap changes.
        viewModel.getCurrentMapProperty().addListener((_, _, newMap) -> {
            if (newMap != null) {
                updateMapData(newMap);
            }
        });

        viewModel.getHoveredRegionProperty().addListener((_, _, newRegion) -> {
            updateMapBorder(newRegion);
        });

        this.setOnMouseMoved(event -> {
            // Gets absolute position regardless of Scale & Transform objects applied.
            Point2D point = mapImageView.sceneToLocal(event.getSceneX(), event.getSceneY());
            int pixelX = (int) Math.floor(point.getX());
            int pixelY = (int) Math.floor(point.getY());

            // Out of bounds check.
            if (pixelX >= 0 && pixelX < hitboxMaskImage.getWidth() && pixelY >= 0 && pixelY < hitboxMaskImage.getHeight()) {
                int argb = hitboxMaskImage.getPixelReader().getArgb(pixelX, pixelY);
                viewModel.updateHoveredColor(argb);
            } else {
                viewModel.updateHoveredColor(0);
            }
        });
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        if (viewModel.getCurrentMap() == null) return;

        int mapWidth = viewModel.getCurrentMap().width();
        int mapHeight = viewModel.getCurrentMap().height();
        double paneWidth = getWidth();
        double paneHeight = getHeight();

        if (paneWidth <= 0 || paneHeight <= 0) return;

        double newScale = Math.min(paneWidth / mapWidth, paneHeight / mapHeight);
        mapScale.setX(newScale);
        mapScale.setY(newScale);

        double scaledWidth = mapWidth * newScale;
        double scaledHeight = mapHeight * newScale;
        mapTranslate.setX((paneWidth - scaledWidth) / 2);
        mapTranslate.setY((paneHeight - scaledHeight) / 2);
    }

    private void updateMapData(MapModel map) {
        mapImageView.setImage(loadImage(map.fileName()));
        borderMaskImage = loadImage(map.borderMaskFileName());
        hitboxMaskImage = loadImage(map.hitboxMaskFileName());
        // Force recalculation of scale and translate properties.
        requestLayout();
    }

    private void updateMapBorder(RegionModel region) {
        mapBorderCanvas.getGraphicsContext2D().clearRect(0, 0, mapBorderCanvas.getWidth(), mapBorderCanvas.getHeight());
        if (region == null) return;

        PixelWriter writer = mapBorderCanvas.getGraphicsContext2D().getPixelWriter();

        int width = (int) borderMaskImage.getWidth();
        int height = (int) borderMaskImage.getHeight();

        boolean[] borderMask = borderCache.get(region.maskColor());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int currentCoords = y * width + x;
                if (borderMask[currentCoords]) {
                    writer.setColor(x, y, viewModel.getBorderColor());
                }
            }
        }
    }

    private Map<Integer, boolean[]> cacheBorders(Image maskImage, int borderSize) {
        int width = (int) maskImage.getWidth();
        int height = (int) maskImage.getHeight();

        PixelReader reader = maskImage.getPixelReader();

        Map<Integer, boolean[]> maskBorderMap = new HashMap<>();

        for (int maskColor : viewModel.getRegionMaskMap().keySet()) {
            boolean[] colorMask = readColorMask(reader, maskColor, width, height);
            boolean[] borderMask = createBorderAroundMask(colorMask, width, height, borderSize, borderSize);

            maskBorderMap.put(maskColor, borderMask);
        }

        return maskBorderMap;
    }

    private static boolean[] readColorMask(PixelReader reader, int maskColor, int maskWidth, int maskHeight) {
        boolean[] mask = new boolean[maskWidth * maskHeight];

        for (int y = 0; y < maskHeight; y++) {
            for (int x = 0; x < maskWidth; x++) {
                int pixelColor = reader.getArgb(x, y);
                mask[y * maskWidth + x] = (pixelColor == maskColor);
            }
        }

        return mask;
    }

    private static boolean[] createBorderAroundMask(boolean[] mask, int maskWidth, int maskHeight, int borderWidth, int borderHeight) {
        boolean[] borderMask = new boolean[maskWidth * maskHeight];


        for (int y = 0; y < maskHeight; y++) {
            for (int x = 0; x < maskWidth; x++) {
                int currentCoords = y * maskWidth + x;

                if (!mask[currentCoords]) {
                    boolean border = false;

                    for (int dy = -borderHeight; dy <= borderHeight && !border; dy++) {
                        for (int dx = -borderWidth; dx <= borderWidth; dx++) {
                            int newX = x + dx;
                            int newY = y + dy;

                            if (newX >= 0 && newX < maskWidth && newY >= 0 && newY < maskHeight) {
                                if (mask[newY * maskWidth + newX]) {
                                    border = true;
                                    break;
                                }
                            }
                        }
                    }
                    borderMask[currentCoords] = border;
                }
            }
        }
        return borderMask;
    }

    private static Image loadImage(String fileName) {
        String path = String.format("/%s.png", fileName);
        InputStream stream = MapView.class.getResourceAsStream(path);

        if (stream == null) {
            throw new IllegalArgumentException("No file exists at: " + path);
        }
        return new Image(stream);
    }
}
