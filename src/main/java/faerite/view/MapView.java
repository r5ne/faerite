package faerite.view;

import static faerite.view.MapGeometry.getColorAtPoint;
import static faerite.view.MapGeometry.screenToMapPixel;

import faerite.Point;
import faerite.model.MapDataLoader;
import faerite.model.MapModel;
import faerite.model.RegionSelectionModel;
import faerite.viewmodel.AtlasViewModel;
import faerite.viewmodel.MapViewModel;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.SwingNode;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javax.swing.*;

/// Contains the map and any borders or tooltips displayed over its regions.
public class MapView extends StackPane {

    private static final int PADDING = 40;
    private static final int BORDER_SIZE = 2;
    private static final double TOOLTIP_FADE_TIME = 1000;

    private final AtlasViewModel viewModel;
    private final SwingNode swingNode = new SwingNode();
    private final MapRenderer renderer = new MapRenderer(BORDER_SIZE);

    private BufferedImage mapImage;
    private BufferedImage hoveredMapImage;
    private BufferedImage selectedMapImage;

    private final Label hoveredRegionTooltip = new Label();

    private Image hitboxMaskImage;
    private Image borderMaskImage;
    private Map<Integer, int[]> borderCache;

    private double mapScale = 1.0;
    private double minScale;

    private ChangeListener<RegionSelectionModel> hoveredRegionListener;
    private ChangeListener<RegionSelectionModel> selectedRegionListener;

    /// Creates the map using data from the view model.
    /// @param viewModel The global view model instance.
    public MapView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        hoveredRegionTooltip.getStyleClass().add("tooltip");
        hoveredRegionTooltip.setMouseTransparent(true);
        hoveredRegionTooltip.setVisible(false);
        hoveredRegionTooltip.setManaged(false);

        SwingUtilities.invokeLater(() -> {
            renderer.setBackgroundColor(viewModel.getOceanColor());
            swingNode.setContent(renderer);
            swingNode.setMouseTransparent(true);
        });
        getChildren().addAll(swingNode, hoveredRegionTooltip);

        viewModel.activeLayerProperty().addListener((_, oldLayer, newLayer) -> {
            loadNewMap(oldLayer, newLayer);
        });

        loadNewMap(null, viewModel.getActiveLayer());
        createEvents();
    }

    private void loadNewMap(MapViewModel oldMap, MapViewModel newMap) {
        if (oldMap != null) {
            oldMap.getHoveredRegionProperty().removeListener(hoveredRegionListener);
            oldMap.getSelectedRegionProperty().removeListener(selectedRegionListener);
        }

        MapModel mapModel = newMap.mapModel;
        mapImage = MapAssetCache.getBufferedImage(mapModel.imageFileName());
        hitboxMaskImage = MapAssetCache.getImage(mapModel.hitboxMaskFileName());
        borderMaskImage = MapAssetCache.getImage(mapModel.borderMaskFileName());
        borderCache = MapAssetCache.getMapBorders(mapModel, borderMaskImage);

        // ensure canvas size accounts for borders being added to the map
        int paddedWidth = mapModel.width() + BORDER_SIZE * 2;
        int paddedHeight = mapModel.height() + BORDER_SIZE * 2;
        hoveredMapImage = new BufferedImage(paddedWidth, paddedHeight, BufferedImage.TYPE_INT_ARGB);
        selectedMapImage = new BufferedImage(paddedWidth, paddedHeight, BufferedImage.TYPE_INT_ARGB);

        hoveredRegionListener = (_, _, _) -> syncHoverBorder(newMap);
        selectedRegionListener = (_, _, _) -> {
            syncSelectedBorder(newMap);
            syncHoverBorder(newMap);
        };

        newMap.getHoveredRegionProperty().addListener(hoveredRegionListener);
        newMap.getSelectedRegionProperty().addListener(selectedRegionListener);
        syncHoverBorder(newMap);
        syncSelectedBorder(newMap);

        mapScale = calculateGlobalScale();

        SwingUtilities.invokeLater(() -> {
            if (renderer != null) {
                renderer.setZoomFactor(mapScale); // Set scale FIRST
                renderer.setImages(mapImage, hoveredMapImage, selectedMapImage); // Then trigger repaint
            }
        });

        requestLayout();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        if (viewModel.getActiveLayer() == null || getWidth() <= 0 || getHeight() <= 0) return;

        mapScale = calculateGlobalScale();

        SwingUtilities.invokeLater(() -> {
            renderer.setZoomFactor(mapScale);
        });

        swingNode.resize(getWidth(), getHeight());
    }

    private void createEvents() {
        setOnMouseMoved(this::updateHoveredState);
        setOnMouseExited(_ -> hoveredRegionTooltip.setVisible(false));

        setOnMouseClicked(event -> {
            if (event.getButton() != MouseButton.PRIMARY) return;

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

        setOnKeyPressed(event -> {
            MapViewModel currentLayer = viewModel.getActiveLayer();
            if (event.getCode().equals(KeyCode.ESCAPE)) {
                viewModel.zoomOut();
            }

            if (event.getCode().equals(KeyCode.E)) {
                RegionSelectionModel currentSelectedRegion = currentLayer.getSelectedRegion();

                if (currentSelectedRegion != null && currentSelectedRegion.subMapFileName() != null) {
                    MapModel newMap = MapDataLoader.loadMapModel(currentLayer.getSelectedRegion().subMapFileName());
                    viewModel.zoomIn(newMap);
                }
            } else if (event.getCode().equals(KeyCode.X)) {
                viewModel.zoomOut();
            }
        });
    }

    private void updateHoveredState(MouseEvent event) {
        Point mapPoint = screenToMapPixel(
            event.getX(),
            event.getY(),
            mapImage.getWidth(),
            mapImage.getHeight(),
            mapScale,
            getWidth(),
            getHeight()
        );
        int colorAtPoint = getColorAtPoint(hitboxMaskImage, mapPoint.x(), mapPoint.y());
        viewModel.getActiveLayer().updateHoveredRegion(colorAtPoint);

        RegionSelectionModel hoveredRegion = viewModel.getActiveLayer().getHoveredRegion();
        updateHoveredRegionTooltip(hoveredRegion, event.getScreenX(), event.getScreenY());
    }

    private void updateHoveredRegionTooltip(RegionSelectionModel hoveredRegion, double screenX, double screenY) {
        if (hoveredRegion != null) {
            hoveredRegionTooltip.setText(
                String.format(
                    "%s (%s)",
                    hoveredRegion.regionData().name(),
                    hoveredRegion.regionData().type().getDisplayName()
                )
            );
            hoveredRegionTooltip.autosize();
            hoveredRegionTooltip.relocate(screenX + 15, screenY + 15);
            hoveredRegionTooltip.setVisible(true);
        } else {
            hoveredRegionTooltip.setVisible(false);
        }
    }

    private double calculateGlobalScale() {
        MapModel rootModel = viewModel.getRootLayer().mapModel;
        if (rootModel == null || getWidth() <= 0 || getHeight() <= 0) {
            return 1.0;
        }

        double paddedWidth = getWidth() - PADDING;
        double paddedHeight = getHeight() - PADDING;

        return Math.min(paddedWidth / rootModel.width(), paddedHeight / rootModel.height());
    }

    private void syncHoverBorder(MapViewModel currentLayer) {
        RegionSelectionModel hovered = currentLayer.getHoveredRegion();
        RegionSelectionModel selected = currentLayer.getSelectedRegion();

        if (hovered != null && hovered.equals(selected)) {
            updateBorderImage(null, hoveredMapImage, 0);
        } else {
            updateBorderImage(hovered, hoveredMapImage, viewModel.getHoveredBorderColor());
        }
        SwingUtilities.invokeLater(renderer::repaint);
    }

    private void syncSelectedBorder(MapViewModel currentLayer) {
        RegionSelectionModel selected = currentLayer.getSelectedRegion();
        updateBorderImage(selected, selectedMapImage, viewModel.getSelectedBorderColor());
        SwingUtilities.invokeLater(renderer::repaint);
    }

    private void updateBorderImage(RegionSelectionModel region, BufferedImage image, int color) {
        int[] pixelData = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        // Clears the image.
        Arrays.fill(pixelData, 0);

        if (region == null || borderCache == null) return;

        int[] borderMask = borderCache.get(region.maskColor());
        if (borderMask == null) return;

        for (int index : borderMask) {
            pixelData[index] = color;
        }
    }
}
