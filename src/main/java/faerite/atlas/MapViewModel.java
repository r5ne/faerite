package faerite.atlas;

import faerite.model.MapModel;
import faerite.model.RegionSelectionModel;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;

/// A view model storing data for a singular map model
public class MapViewModel {

    public final MapModel mapModel;

    private final ObjectProperty<RegionSelectionModel> hoveredRegion = new ReadOnlyObjectWrapper<>();
    private final ObjectProperty<RegionSelectionModel> selectedRegion = new SimpleObjectProperty<>();

    private final Map<Integer, RegionSelectionModel> colorToRegionMap = new HashMap<>();

    /// Creates the view model using data from the map model.
    /// @param mapModel The map model to use.
    public MapViewModel(MapModel mapModel) {
        this.mapModel = mapModel;

        for (RegionSelectionModel region : mapModel.regions()) {
            colorToRegionMap.put(region.maskColor(), region);
        }
    }

    /// Updates the stored hovered region data.
    /// @param color The ARGB mask color of the region to mark as hovered.
    public void updateHoveredRegion(int color) {
        RegionSelectionModel region = colorToRegionMap.get(color);

        if (!Objects.equals(hoveredRegion.get(), region)) {
            hoveredRegion.set(region);
        }
    }

    /// Updates the stored selected region data to the currently hovered region.
    public void updateSelectedRegion() {
        RegionSelectionModel currentHoveredRegion = hoveredRegion.get();

        if (!Objects.equals(selectedRegion.get(), currentHoveredRegion)) {
            selectedRegion.set(currentHoveredRegion);
        }
    }

    /// Updates the stored selected regino data
    /// @param color The ARGB mask color of the region update as selected.
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
