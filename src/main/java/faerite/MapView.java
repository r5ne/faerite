package faerite;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import java.io.InputStream;
import java.util.Map;

public class MapView extends Pane {
    private static final int PADDING = 40;
    private static final int BORDER_SIZE = 2;

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

        // ensure canvas size accounts for borders being added to the map
        double canvasWidth = viewModel.getCurrentMap().width() + (BORDER_SIZE * 2);
        double canvasHeight = viewModel.getCurrentMap().height() + (BORDER_SIZE * 2);
        mapBorderCanvas = new Canvas(canvasWidth, canvasHeight);
        // re-align canvas with map image
        mapBorderCanvas.setLayoutX(-BORDER_SIZE);
        mapBorderCanvas.setLayoutY(-BORDER_SIZE);
        mapBorderCanvas.getTransforms().addAll(mapScale, mapTranslate);
        getChildren().add(mapBorderCanvas);

        createBindings();
        createListeners();
        createEvents();

        updateMapData(viewModel.getCurrentMap());
    }

    private void createBindings() {
        // Keep the background synced with the oceanColor.
        backgroundProperty().bind(Bindings.createObjectBinding(() ->
                        new Background(new BackgroundFill(
                                viewModel.getOceanColorProperty().get(),
                                CornerRadii.EMPTY,
                                Insets.EMPTY
                        )),
                viewModel.getOceanColorProperty()
        ));
    }

    private void createListeners() {
        // Update the map, border and hitbox masks when the currentMap changes.
        viewModel.getCurrentMapProperty().addListener((_, _, newMap) -> {
            if (newMap != null) {
                updateMapData(newMap);
            }
        });

        viewModel.getHoveredRegionProperty().addListener((_, _, newRegion) ->
            updateMapBorder(newRegion)
        );
    }

    private void createEvents() {
        setOnMouseMoved(event -> {
            if (hitboxMaskImage == null) return;

            // Gets absolute position regardless of Scale & Transform objects applied.
            Point2D point = mapImageView.sceneToLocal(event.getSceneX(), event.getSceneY());
            int pixelX = (int) Math.floor(point.getX());
            int pixelY = (int) Math.floor(point.getY());

            // Out of bounds check.
            if (pixelX >= 0 && pixelX < hitboxMaskImage.getWidth() && pixelY >= 0 && pixelY < hitboxMaskImage.getHeight()) {
                int color = hitboxMaskImage.getPixelReader().getArgb(pixelX, pixelY);
                viewModel.updateHoveredColor(color);
            } else {
                viewModel.updateHoveredColor(0);
            }
        });
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        if (viewModel.getCurrentMap() == null || getWidth() <= 0 || getHeight() <= 0) return;

        int mapWidth = viewModel.getCurrentMap().width();
        int mapHeight = viewModel.getCurrentMap().height();

        double paddedPaneWidth = getWidth() - PADDING;
        double paddedPaneHeight = getHeight() - PADDING;

        double newScale = Math.min(paddedPaneWidth / mapWidth, paddedPaneHeight / mapHeight);
        mapScale.setX(newScale);
        mapScale.setY(newScale);

        double scaledWidth = mapWidth * newScale;
        double scaledHeight = mapHeight * newScale;
        mapTranslate.setX((getWidth() - scaledWidth) / 2);
        mapTranslate.setY((getHeight() - scaledHeight) / 2);
    }

    private void updateMapData(MapModel map) {
        mapImageView.setImage(loadImage(map.fileName()));
        borderMaskImage = loadImage(map.borderMaskFileName());
        hitboxMaskImage = loadImage(map.hitboxMaskFileName());

        borderCache = MaskUtils.createBorderMasks(borderMaskImage, viewModel.getRegionMaskMap().keySet(), 2);
    }

    private void updateMapBorder(RegionModel region) {
        mapBorderCanvas.getGraphicsContext2D().clearRect(0, 0, mapBorderCanvas.getWidth(), mapBorderCanvas.getHeight());
        if (region == null || borderCache == null) return;

        boolean[] borderMask = borderCache.get(region.maskColor());
        if (borderMask == null) return;

        PixelWriter writer = mapBorderCanvas.getGraphicsContext2D().getPixelWriter();

        int paddedWidth = (int) borderMaskImage.getWidth() + (BORDER_SIZE * 2);
        int paddedHeight = (int) borderMaskImage.getHeight() + (BORDER_SIZE * 2);

        for (int y = 0; y < paddedHeight; y++) {
            for (int x = 0; x < paddedWidth; x++) {
                if (borderMask[y * paddedWidth + x]) {
                    writer.setColor(x, y, viewModel.getBorderColor());
                }
            }
        }
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
