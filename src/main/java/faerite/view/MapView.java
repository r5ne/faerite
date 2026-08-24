package faerite.view;

import faerite.model.MapDataLoader;
import faerite.model.MapModel;
import faerite.model.RegionSelectionModel;
import faerite.viewmodel.AtlasViewModel;
import faerite.viewmodel.MapViewModel;
import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
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
    private final MapRendererView rendererView = new MapRendererView();

    private final Group mapCanvasGroup = new Group();

    private final Canvas mapImageCanvas = new Canvas();
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

        hoveredMapBorderCanvas.setLayoutX(-BORDER_SIZE);
        hoveredMapBorderCanvas.setLayoutY(-BORDER_SIZE);
        selectedMapBorderCanvas.setLayoutX(-BORDER_SIZE);
        selectedMapBorderCanvas.setLayoutY(-BORDER_SIZE);

        rendererView.setMapImage(MapAssetCache.getBufferedImage("british-isles.png"));
        rendererView.setZoomFactor(1);
        getChildren().add(rendererView);

        mapCanvasGroup.getChildren().addAll(mapImageCanvas, hoveredMapBorderCanvas, selectedMapBorderCanvas);
        mapCanvasGroup.getTransforms().addAll(mapScale, mapTranslate);

        createBindings();

        viewModel.activeLayerProperty().addListener((_, oldLayer, newLayer) -> {
            updateActiveMap(oldLayer, newLayer);
        });

        updateActiveMap(null, viewModel.getActiveLayer());
        createEvents();
    }

    private void createBindings() {
        // Keep the background synced with the oceanColor.
        //backgroundProperty().bind(
        //    Bindings.createObjectBinding(() -> {
        //        Color oceanColor = viewModel.getOceanColor();
        //        BackgroundFill bgFill = new BackgroundFill(oceanColor, CornerRadii.EMPTY, Insets.EMPTY);
        //        return new Background(bgFill);
        //    }, viewModel.oceanColorProperty())
        //);
    }

    private void createEvents() {
        setOnMouseMoved(event -> {
            if (hitboxMaskImage == null) return;

            // Gets absolute position regardless of Scale & Transform objects applied.
            Point2D point = mapCanvasGroup.sceneToLocal(event.getSceneX(), event.getSceneY());
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

            MapViewModel currentLayer = viewModel.getActiveLayer();

            if (event.getClickCount() == 1) {
                currentLayer.updateSelectedRegion();
            } else if (event.getClickCount() > 1) {
                RegionSelectionModel currentHoveredRegion = currentLayer.getHoveredRegion();

                if (currentHoveredRegion != null && currentHoveredRegion.subMapFileName() != null) {
                    MapModel newMap = MapDataLoader.loadMapModel(currentLayer.getSelectedRegion().subMapFileName());
                    viewModel.zoomIn(newMap);
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

        mapImageCanvas.setWidth(mapModel.width());
        mapImageCanvas.setHeight(mapModel.height());

        // ensure canvas size accounts for borders being added to the map
        double paddedWidth = mapModel.width() + BORDER_SIZE * 2;
        double paddedHeight = mapModel.height() + BORDER_SIZE * 2;
        hoveredMapBorderCanvas.setWidth(paddedWidth);
        hoveredMapBorderCanvas.setHeight(paddedHeight);
        selectedMapBorderCanvas.setWidth(paddedWidth);
        selectedMapBorderCanvas.setHeight(paddedHeight);


        GraphicsContext graphicsContext = mapImageCanvas.getGraphicsContext2D();
        graphicsContext.setImageSmoothing(false);

        int mapWidth = viewModel.getActiveLayer().mapModel.width();
        int mapHeight = viewModel.getActiveLayer().mapModel.height();

        double paddedPaneWidth = getWidth() - PADDING;
        double paddedPaneHeight = getHeight() - PADDING;

        double newScale = Math.min(paddedPaneWidth / mapWidth, paddedPaneHeight / mapHeight);

        double scaledWidth = mapWidth * newScale;
        double scaledHeight = mapHeight * newScale;
        graphicsContext.drawImage(
                mapImage,
                0, 0,
                mapImage.getWidth(),
                mapImage.getHeight(),
                0, 0,
                1000,
                1000
        );
        graphicsContext.clearRect(0, 0, mapModel.width(), mapModel.height());
        graphicsContext.drawImage(mapImage, 0, 0);

        hoveredMapBorderCanvas.getGraphicsContext2D().clearRect(0, 0, paddedWidth, paddedHeight);
        selectedMapBorderCanvas.getGraphicsContext2D().clearRect(0, 0, paddedWidth, paddedHeight);


        hoveredRegionListener = (_, _, newRegion) -> {
            if (newLayer.getHoveredRegion() != newLayer.getSelectedRegion() || newLayer.getHoveredRegion() == null) {
                //updateMapBorder(newRegion, hoveredMapBorderCanvas, viewModel.getHoveredBorderColor());
            }
        };
        selectedRegionListener = (_, _, newRegion) -> {
            //updateMapBorder(newRegion, selectedMapBorderCanvas, viewModel.getSelectedBorderColor());
            hoveredMapBorderCanvas.getGraphicsContext2D().clearRect(0, 0, hoveredMapBorderCanvas.getWidth(), hoveredMapBorderCanvas.getHeight());
        };

        newLayer.getHoveredRegionProperty().addListener(hoveredRegionListener);
        newLayer.getSelectedRegionProperty().addListener(selectedRegionListener);

        requestLayout();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        rendererView.resizeRelocate(0, 0, getWidth(), getHeight());

        MapModel currentMapModel = viewModel.getActiveLayer().mapModel;

        if (currentMapModel == null || getWidth() <= 0 || getHeight() <= 0) return;

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
