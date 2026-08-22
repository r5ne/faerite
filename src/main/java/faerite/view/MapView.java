package faerite.view;

import faerite.model.MapModel;
import faerite.model.RegionSelectionModel;
import faerite.viewmodel.AtlasViewModel;
import faerite.viewmodel.MapViewModel;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class MapView extends Pane {

    private static final int PADDING = 40;
    private static final int BORDER_SIZE = 2;

    private final AtlasViewModel viewModel;
    private final ImageView mapImageView = new ImageView();
    private final Canvas hoveredMapBorderCanvas = new Canvas();
    private final Canvas selectedMapBorderCanvas = new Canvas();

    private final Scale mapScale = new Scale();
    private final Translate mapTranslate = new Translate();

    private Image hitboxMaskImage;
    private Image borderMaskImage;
    private Map<Integer, boolean[]> borderCache;

    private ChangeListener<RegionSelectionModel> hoveredRegionListener;
    private ChangeListener<RegionSelectionModel> selectedRegionListener;

    public MapView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        // nearest-neighbour interp for pixel art
        mapImageView.setSmooth(false);
        mapImageView.getTransforms().addAll(mapScale, mapTranslate);
        getChildren().add(mapImageView);

        // re-align canvas with map image
        hoveredMapBorderCanvas.setLayoutX(-BORDER_SIZE);
        hoveredMapBorderCanvas.setLayoutY(-BORDER_SIZE);
        hoveredMapBorderCanvas.getTransforms().addAll(mapScale, mapTranslate);

        // re-align canvas with map image
        selectedMapBorderCanvas.setLayoutX(-BORDER_SIZE);
        selectedMapBorderCanvas.setLayoutY(-BORDER_SIZE);
        selectedMapBorderCanvas.getTransforms().addAll(mapScale, mapTranslate);

        createBindings();

        viewModel.activeLayerProperty().addListener((_, oldLayer, newLayer) -> {
            updateActiveMap(oldLayer, newLayer);
        });

        updateActiveMap(null, viewModel.getActiveLayer());

        createEvents();
    }

    private void createBindings() {
        // Keep the background synced with the oceanColor.
        backgroundProperty().bind(
            Bindings.createObjectBinding(() -> {
                Color oceanColor = viewModel.getOceanColor();
                BackgroundFill bgFill = new BackgroundFill(oceanColor, CornerRadii.EMPTY, Insets.EMPTY);
                return new Background(bgFill);
            }, viewModel.oceanColorProperty())
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
            if (
                pixelX >= 0 &&
                pixelX < hitboxMaskImage.getWidth() &&
                pixelY >= 0 &&
                pixelY < hitboxMaskImage.getHeight()
            ) {
                int color = hitboxMaskImage.getPixelReader().getArgb(pixelX, pixelY);
                viewModel.getActiveLayer().updateHoveredRegion(color);
            } else {
                viewModel.getActiveLayer().updateHoveredRegion(0);
            }
        });

        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                viewModel.getActiveLayer().updateSelectedRegion();
            }
        });
        setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) {
                return;
            }

            if (event.getClickCount() == 1) {
                viewModel.getActiveLayer().updateSelectedRegion();
            } else if (event.getClickCount() > 1) {
                if (viewModel.getActiveLayer().getHoveredRegion().subMapFileName() != null) {
                    System.out.println("submap switch");
                }
            }
        });
    }

    private void updateActiveMap(MapViewModel oldLayer, MapViewModel newLayer) {
        if (newLayer == null) return;

        if (oldLayer != null) {
            oldLayer.getHoveredRegionProperty().removeListener(hoveredRegionListener);
            oldLayer.getSelectedRegionProperty().removeListener(selectedRegionListener);
        }

        MapModel mapModel = newLayer.mapModel;
        Image mapImage = MapAssetCache.getImage(mapModel.imageFileName());
        borderMaskImage = MapAssetCache.getImage(mapModel.borderMaskFileName());
        hitboxMaskImage = MapAssetCache.getImage(mapModel.hitboxMaskFileName());

        borderCache = MapAssetCache.getMapBorders(mapModel, borderMaskImage);
        mapImageView.setImage(mapImage);

        // ensure canvas size accounts for borders being added to the map
        double canvasWidth = mapModel.width() + BORDER_SIZE * 2;
        double canvasHeight = mapModel.height() + BORDER_SIZE * 2;
        hoveredMapBorderCanvas.setWidth(canvasWidth);
        hoveredMapBorderCanvas.setHeight(canvasHeight);
        selectedMapBorderCanvas.setWidth(canvasWidth);
        selectedMapBorderCanvas.setHeight(canvasHeight);

        hoveredMapBorderCanvas.getGraphicsContext2D().clearRect(0, 0, canvasWidth, canvasHeight);
        selectedMapBorderCanvas.getGraphicsContext2D().clearRect(0, 0, canvasWidth, canvasHeight);

        hoveredRegionListener = (_, _, newRegion) -> {
            if (newLayer.getHoveredRegion() != newLayer.getSelectedRegion() || newLayer.getHoveredRegion() == null) {
                updateMapBorder(newRegion, hoveredMapBorderCanvas, viewModel.getHoveredBorderColor());
            }
        };
        selectedRegionListener = (_, _, newRegion) -> {
            updateMapBorder(newRegion, selectedMapBorderCanvas, viewModel.getSelectedBorderColor());
        };

        newLayer.getHoveredRegionProperty().addListener(hoveredRegionListener);
        newLayer.getSelectedRegionProperty().addListener(selectedRegionListener);

        getChildren().addAll(hoveredMapBorderCanvas, selectedMapBorderCanvas);

        requestLayout();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        MapModel currentMapModel = viewModel.getActiveLayer().mapModel;

        if (currentMapModel == null || getWidth() <= 0 || getHeight() <= 0) return;

        int mapWidth = currentMapModel.width();
        int mapHeight = currentMapModel.height();

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

    private void updateMapBorder(RegionSelectionModel region, Canvas canvas, Color borderColor) {
        canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        if (region == null || borderCache == null) return;

        boolean[] borderMask = borderCache.get(region.maskColor());
        if (borderMask == null) return;

        PixelWriter writer = canvas.getGraphicsContext2D().getPixelWriter();

        int paddedWidth = (int) borderMaskImage.getWidth() + BORDER_SIZE * 2;
        int paddedHeight = (int) borderMaskImage.getHeight() + BORDER_SIZE * 2;

        for (int y = 0; y < paddedHeight; y++) {
            for (int x = 0; x < paddedWidth; x++) {
                if (borderMask[y * paddedWidth + x]) {
                    writer.setColor(x, y, borderColor);
                }
            }
        }
    }
}
