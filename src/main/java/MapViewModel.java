import javafx.beans.property.*;
import javafx.scene.paint.Color;

import java.util.*;

public class MapViewModel {
    private Map<Integer, RegionModel> regionMaskMap = new HashMap<>();

    private final ReadOnlyObjectWrapper<MapModel> currentMap = new ReadOnlyObjectWrapper<>();
    private final ReadOnlyObjectWrapper<RegionModel> hoveredRegion = new ReadOnlyObjectWrapper<>();
    private final ObjectProperty<Color> oceanColor = new SimpleObjectProperty<>(Color.web("#213840"));

    public MapViewModel() {
        currentMap.addListener((_, _, newMap) -> updateRegionMaskMap(newMap));

        MapModel britishIsles = new MapModel("british-isles", "British Isles", 941, 1254, RegionType.ARCHIPELAGO, new HashSet<>());
        RegionModel greatBritain = new RegionModel("Great Britain", RegionType.ISLAND, 0xFF000000, new HashSet<>());
        britishIsles.regions().add(greatBritain);

        currentMap.set(britishIsles);
    }

    public void updateHoveredColor(int argb) {
        RegionModel region = regionMaskMap.get(argb);

        if (!Objects.equals(hoveredRegion.get(), region)) {
            hoveredRegion.set(region);
            System.out.println(argb);
        }
    }

    private void updateRegionMaskMap(MapModel map) {
        regionMaskMap.clear();
        if (map == null) return;

        for (RegionModel region : map.regions()) {
            regionMaskMap.put(region.maskARGBColor(), region);
        }
    }

    public ReadOnlyObjectProperty<MapModel> getCurrentMapProperty() { return currentMap.getReadOnlyProperty(); }
    public MapModel getCurrentMap() { return currentMap.get(); }

    public ReadOnlyObjectProperty<RegionModel> getHoveredRegionProperty() { return hoveredRegion.getReadOnlyProperty(); }
    public RegionModel getHoveredRegion() { return hoveredRegion.get(); }

    public ObjectProperty<Color> getOceanColorProperty() { return oceanColor; }
    public Color getOceanColor() { return oceanColor.get(); }
}
