package faerite.atlas;

import faerite.model.MapModel;
import faerite.model.RegionSelectionModel;
import faerite.util.Colors;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/// A composite view model storing a stack of individual map view models for each displayed map.
public class AtlasViewModel {

    private final ObservableList<MapViewModel> layerHistory = FXCollections.observableArrayList();
    private final IntegerProperty currentLayerIndex = new SimpleIntegerProperty(-1);
    private final ObjectProperty<MapViewModel> activeLayer = new SimpleObjectProperty<>();

    private final IntegerProperty oceanColor = new SimpleIntegerProperty(0x213840); // rgb
    private final IntegerProperty hoveredBorderColor = new SimpleIntegerProperty(); // argb
    private final IntegerProperty selectedBorderColor = new SimpleIntegerProperty(0xffff3d3d); // argb

    /// Creates the view model and adds the first map view model to the stack using the map model data.
    /// @param rootMapModel The map model used to create the first map view model.
    public AtlasViewModel(MapModel rootMapModel) {
        hoveredBorderColor.set(Colors.deriveColorARGB(oceanColor.get(), 0.7, 3));

        currentLayerIndex.addListener((_, _, newIndex) -> {
            if (newIndex.intValue() >= 0 && newIndex.intValue() < layerHistory.size()) {
                activeLayer.set(layerHistory.get(newIndex.intValue()));
            }
        });

        zoomIn(rootMapModel);
    }

    /// Creates a new map view model using the map model and adds it to the stack.
    /// @param mapModel The map model used to create the map view model.
    public void zoomIn(MapModel mapModel) {
        int nextIndex = currentLayerIndex.get() + 1;

        if (nextIndex < layerHistory.size()) {
            if (layerHistory.get(nextIndex).mapModel.fileName().equals(mapModel.fileName())) {
                currentLayerIndex.set(nextIndex);
                return;
            } else {
                layerHistory.subList(nextIndex, layerHistory.size()).clear();
            }
        }

        layerHistory.add(new MapViewModel(mapModel));
        currentLayerIndex.set(nextIndex);
    }

    /// Removes the map view model at the top of the stack.
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

    public MapViewModel getRootLayer() { return layerHistory.getFirst(); }

    public MapViewModel getActiveLayer() {
        return activeLayer.get();
    }

    public ReadOnlyObjectProperty<MapViewModel> activeLayerProperty() {
        return activeLayer;
    }

    public int getOceanColor() {
        return oceanColor.get();
    }

    public IntegerProperty oceanColorProperty() {
        return oceanColor;
    }

    public int getHoveredBorderColor() {
        return hoveredBorderColor.get();
    }

    public IntegerProperty hoveredBorderColorProperty() {
        return hoveredBorderColor;
    }

    public int getSelectedBorderColor() {
        return selectedBorderColor.get();
    }

    public IntegerProperty selectedBorderColorProperty() {
        return selectedBorderColor;
    }

}
