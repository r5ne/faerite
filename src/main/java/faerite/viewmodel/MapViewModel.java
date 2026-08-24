package faerite.viewmodel;

import faerite.model.MapModel;
import faerite.model.RegionSelectionModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;

public class MapViewModel {

    public final MapModel mapModel;

    private final ObjectProperty<RegionSelectionModel> hoveredRegion = new ReadOnlyObjectWrapper<>();
    private final ObjectProperty<RegionSelectionModel> selectedRegion = new SimpleObjectProperty<>();

    private final Map<Integer, RegionSelectionModel> colorToRegionMap = new HashMap<>();

    public MapViewModel(MapModel mapModel) {
        this.mapModel = mapModel;

        for (RegionSelectionModel region : mapModel.regions()) {
            colorToRegionMap.put(region.maskColor(), region);
        }
    }

    public void updateHoveredRegion(int color) {
        RegionSelectionModel region = colorToRegionMap.get(color);

        if (!Objects.equals(hoveredRegion.get(), region)) {
            hoveredRegion.set(region);
        }
    }

    public void updateSelectedRegion() {
        RegionSelectionModel currentHoveredRegion = hoveredRegion.get();

        if (!Objects.equals(selectedRegion.get(), currentHoveredRegion)) {
            selectedRegion.set(currentHoveredRegion);
        }
    }

    public void setSelectedRegionByColor(int color) {
        RegionSelectionModel region = colorToRegionMap.get(color);
        selectedRegion.set(region);
    }

    public Map<Integer, RegionSelectionModel> getColorToRegionMap() {
        return colorToRegionMap;
    }

    public ReadOnlyObjectProperty<RegionSelectionModel> getHoveredRegionProperty() {
        return hoveredRegion;
    }

    public RegionSelectionModel getHoveredRegion() {
        return hoveredRegion.get();
    }

    public ReadOnlyObjectProperty<RegionSelectionModel> getSelectedRegionProperty() {
        return selectedRegion;
    }

    public RegionSelectionModel getSelectedRegion() {
        return selectedRegion.get();
    }
}
