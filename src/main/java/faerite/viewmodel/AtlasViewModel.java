package faerite.viewmodel;

import faerite.model.MapModel;
import faerite.model.RegionSelectionModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class AtlasViewModel {
    private final ObservableList<MapViewModel> layerHistory = FXCollections.observableArrayList();
    private final IntegerProperty currentLayerIndex = new SimpleIntegerProperty(-1);
    private final ObjectProperty<MapViewModel> activeLayer = new SimpleObjectProperty<>();

    private final ObjectProperty<Color> oceanColor = new SimpleObjectProperty<>(Color.web("#213840"));
    private final ObjectProperty<Color> hoveredBorderColor = new SimpleObjectProperty<>(oceanColor.get().deriveColor(1, 0.7, 3, 1));
    private final ObjectProperty<Color> selectedBorderColor = new SimpleObjectProperty<>(Color.web("#ff3d3d"));

    public AtlasViewModel(MapModel rootMapModel) {
        currentLayerIndex.addListener((_, _, newIndex) -> {
            if (newIndex.intValue() >= 0 && newIndex.intValue() < layerHistory.size()) {
                activeLayer.set(layerHistory.get(newIndex.intValue()));
            }
        });

        zoomIn(rootMapModel);
    }

    public void zoomIn(MapModel mapModel) {
        int nextIndex = currentLayerIndex.get() + 1;

        if (nextIndex < layerHistory.size()) {
            if (layerHistory.get(nextIndex).mapModel.name().equals(mapModel.name())) {
                currentLayerIndex.set(nextIndex);
                return;
            } else {
                layerHistory.subList(nextIndex, layerHistory.size()).clear();
            }
        }

        layerHistory.add(new MapViewModel(mapModel));
        currentLayerIndex.set(nextIndex);
    }

    public void zoomOut() {
        if (currentLayerIndex.get() > 0) {
            MapViewModel oldMap = activeLayer.get();
            currentLayerIndex.set(currentLayerIndex.get() - 1);
            MapViewModel newMap = activeLayer.get();

            RegionSelectionModel oldSelectedRegion = oldMap.getSelectedRegion();
            if (oldSelectedRegion != null && oldSelectedRegion.parentMapMaskColor() != null) {
                newMap.setSelectedRegionByColor(oldSelectedRegion.parentMapMaskColor());
            }
        }
    }

    public MapViewModel getActiveLayer() {
        return activeLayer.get();
    }

    public ReadOnlyObjectProperty<MapViewModel> activeLayerProperty() {
        return activeLayer;
    }

    public Color getOceanColor() {
        return oceanColor.get();
    }

    public ObjectProperty<Color> oceanColorProperty() {
        return oceanColor;
    }

    public Color getHoveredBorderColor() {
        return hoveredBorderColor.get();
    }

    public ObjectProperty<Color> hoveredBorderColorProperty() {
        return hoveredBorderColor;
    }

    public Color getSelectedBorderColor() {
        return selectedBorderColor.get();
    }

    public ObjectProperty<Color> selectedBorderColorProperty() {
        return selectedBorderColor;
    }
}
