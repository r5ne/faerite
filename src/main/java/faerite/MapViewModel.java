package faerite;

import faerite.model.*;
import javafx.beans.property.*;
import javafx.scene.paint.Color;

import java.util.*;

public class MapViewModel {
    private final Map<Integer, RegionModel> regionMaskMap = new HashMap<>();

    private final ReadOnlyObjectWrapper<MapModel> currentMap = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<RegionModel> hoveredRegion = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<RegionModel> selectedRegion = new ReadOnlyObjectWrapper<>();
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
        RegionModel region = regionMaskMap.get(color);

        if (!Objects.equals(hoveredRegion.get(), region)) {
            hoveredRegion.set(region);
        }
    }

    public void updateSelectedRegion() {
        RegionModel currentHover = hoveredRegion.get();

        if (!Objects.equals(selectedRegion.get(), currentHover)) {
            selectedRegion.set(currentHover);

            if (currentHover != null) {
                currentRegionData.set(currentHover.regionData());
            } else {
                currentRegionData.set(currentMap.get().mapRegionData());
            }
        }
    }

    private void updateRegionMaskMap(MapModel map) {
        regionMaskMap.clear();
        if (map == null) return;

        for (RegionModel region : map.regions()) {
            regionMaskMap.put(region.maskColor(), region);
        }
    }

    public ReadOnlyObjectProperty<MapModel> getCurrentMapProperty() { return currentMap.getReadOnlyProperty(); }
    public MapModel getCurrentMap() { return currentMap.get(); }

    public ReadOnlyObjectProperty<RegionModel> getHoveredRegionProperty() { return hoveredRegion.getReadOnlyProperty(); }
    public RegionModel getHoveredRegion() { return hoveredRegion.get(); }


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

    public Map<Integer, RegionModel> getRegionMaskMap() { return regionMaskMap; }
}
