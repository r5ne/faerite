package faerite.atlas;

import faerite.model.MapModel;
import faerite.model.RegionInfoSection;
import faerite.model.RegionSelectionModel;
import faerite.util.Colors;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/// A composite view model storing a stack of individual map view models for each displayed map.
public class AtlasViewModel {

    private final ObservableList<MapViewModel> layerHistory = FXCollections.observableArrayList();
    private final IntegerProperty currentLayerIndex = new SimpleIntegerProperty(-1);
    private final ObjectProperty<MapViewModel> activeLayer = new SimpleObjectProperty<>();

    private final ObservableValue<RegionSelectionModel> selectedRegion = activeLayer.flatMap(
        MapViewModel::getSelectedRegionProperty
    );
    private final ObservableValue<RegionSelectionModel> hoveredRegion = activeLayer.flatMap(
        MapViewModel::getHoveredRegionProperty
    );
    private final ObjectProperty<RegionInfoSection> selectedInfoSection = new SimpleObjectProperty<>(RegionInfoSection.OVERVIEW);

    private final ObjectProperty<AtlasStyle> style = new SimpleObjectProperty<>(AtlasStyle.DEFAULTS);
    private final IntegerProperty oceanColor = new SimpleIntegerProperty(0x213840); // rgb
    private final IntegerProperty hoveredBorderColor = new SimpleIntegerProperty(
        Colors.deriveColorARGB(oceanColor.get(), 0.7, 3)
    ); // argb
    private final IntegerProperty selectedBorderColor = new SimpleIntegerProperty(0xffff3d3d); // argb

    /// Creates the view model and adds the first map view model to the stack using the map model data.
    /// @param rootMapModel The map model used to create the first map view model.
    public AtlasViewModel(MapModel rootMapModel) {
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
            if (layerHistory.get(nextIndex).mapModel.id().equals(mapModel.id())) {
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

    public RegionInfoSection getSelectedInfoSection() {
        return selectedInfoSection.get();
    }

    public ObjectProperty<RegionInfoSection> selectedInfoSectionProperty() {
        return selectedInfoSection;
    }

    public IntegerProperty oceanColorProperty() {
        return oceanColor;
    }

    public AtlasStyle getStyle() {
        return style.get();
    }

    public ObjectProperty<AtlasStyle> styleProperty() {
        return style;
    }

    public RegionSelectionModel getSelectedRegion() {
        return selectedRegion.getValue();
    }

    public ObservableValue<RegionSelectionModel> selectedRegionProperty() {
        return selectedRegion;
    }

    public RegionSelectionModel getHoveredRegion() {
        return hoveredRegion.getValue();
    }

    public ObservableValue<RegionSelectionModel> hoveredRegionProperty() {
        return hoveredRegion;
    }

    public MapViewModel getRootLayer() {
        return layerHistory.getFirst();
    }

    public MapViewModel getActiveLayer() {
        return activeLayer.get();
    }

    public ReadOnlyObjectProperty<MapViewModel> activeLayerProperty() {
        return activeLayer;
    }

    public int getOceanColor() {
        return oceanColor.get();
    }

    public int getHoveredBorderColor() {
        return hoveredBorderColor.get();
    }

    public int getSelectedBorderColor() {
        return selectedBorderColor.get();
    }
}
