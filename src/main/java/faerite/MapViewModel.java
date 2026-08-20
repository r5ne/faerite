package faerite;

import faerite.model.MapDataLoader;
import faerite.model.MapModel;
import faerite.model.RegionData;
import faerite.model.RegionSelectionModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

import java.util.*;

public class MapViewModel {

    private final Map<Integer, RegionSelectionModel> regionMaskMap = new HashMap<>();

    private final ReadOnlyObjectWrapper<MapModel> currentMap = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<RegionSelectionModel> hoveredRegion = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<RegionSelectionModel> selectedRegion = new ReadOnlyObjectWrapper<>();
    private final ObjectProperty<RegionData> currentRegionData = new SimpleObjectProperty<>();

    private final ObjectProperty<Color> oceanColor = new SimpleObjectProperty<>(Color.web("#213840"));
    private final ObjectProperty<Color> hoveredBorderColor = new SimpleObjectProperty<>();
    private final ObjectProperty<Color> selectedBorderColor = new SimpleObjectProperty<>(Color.web("#ff3d3d"));

    public MapViewModel() {
        hoveredBorderColor.set(oceanColor.get().deriveColor(1, 0.7, 3, 1));

        currentMap.addListener((_, _, newMap) -> updateRegionMaskMap(newMap));

        MapModel britishIsles = MapDataLoader.loadMapModel();

        currentMap.set(britishIsles);
    }

    public void updateHoveredRegion(int color) {
        RegionSelectionModel region = regionMaskMap.get(color);

        if (!Objects.equals(hoveredRegion.get(), region)) {
            hoveredRegion.set(region);
        }
    }

    public void updateSelectedRegion() {
        RegionSelectionModel currentHover = hoveredRegion.get();

        if (!Objects.equals(selectedRegion.get(), currentHover)) {
            selectedRegion.set(currentHover);

            if (currentHover != null) {
                currentRegionData.set(currentHover.regionData());
            } else {
                currentRegionData.set(currentMap.get().regionData());
            }
        }
    }

    private void updateRegionMaskMap(MapModel map) {
        regionMaskMap.clear();
        if (map == null) return;

        for (RegionSelectionModel region : map.regions()) {
            regionMaskMap.put(region.maskColor(), region);
        }
    }

    public ReadOnlyObjectProperty<MapModel> getCurrentMapProperty() {
        return currentMap.getReadOnlyProperty();
    }

    public MapModel getCurrentMap() {
        return currentMap.get();
    }

    public ReadOnlyObjectProperty<RegionSelectionModel> getHoveredRegionProperty() {
        return hoveredRegion.getReadOnlyProperty();
    }

    public RegionSelectionModel getHoveredRegion() {
        return hoveredRegion.get();
    }

    public ReadOnlyObjectProperty<RegionSelectionModel> getSelectedRegionProperty() {
        return selectedRegion.getReadOnlyProperty();
    }

    public RegionSelectionModel getSelectedRegion() {
        return selectedRegion.get();
    }


    public ReadOnlyObjectProperty<RegionModel> getSelectedRegionProperty() { return selectedRegion.getReadOnlyProperty(); }
    public RegionModel getSelectedRegion() { return selectedRegion.get(); }

    public ObjectProperty<Color> getOceanColorProperty() { return oceanColor; }
    public Color getOceanColor() { return oceanColor.get(); }

    public ObjectProperty<Color> getHoveredBorderColorProperty() { return hoveredBorderColor; }
    public Color getHoveredBorderColor() { return hoveredBorderColor.get(); }

    public ObjectProperty<Color> getSelectedBorderColorProperty() { return selectedBorderColor; }
    public Color getSelectedBorderColor() { return selectedBorderColor.get(); }

    public ObjectProperty<RegionData> getCurrentRegionDataProperty() { return currentRegionData; }
    public RegionData getCurrentRegionData() { return currentRegionData.get(); }

    public ObjectProperty<RegionData> getCurrentRegionDataProperty() {
        return currentRegionData;
    }

    public RegionData getCurrentRegionData() {
        return currentRegionData.get();
    }

    public Map<Integer, RegionSelectionModel> getRegionMaskMap() {
        return regionMaskMap;
    }
}
