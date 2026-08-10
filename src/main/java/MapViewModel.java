import javafx.beans.property.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

import java.util.HashSet;

public class MapViewModel {
    private ObjectProperty<RegionModel> hoveredRegion;

    public final String mapTileNames;
    public final int mapWidth;
    public final int mapHeight;
    public final int tileSize;
    public final int tileNumWidth;
    public final int tileNumHeight;
    private ObjectProperty<Rectangle2D> currentMapBounds = new SimpleObjectProperty<>();
    private final ObjectProperty<Color> oceanColor = new SimpleObjectProperty<>(Color.web("#213840"));

    public MapViewModel() {

        RegionBounds britishIslesBounds = new RegionBounds(9716, 5954, 941, 1254);
        RegionModel britishIsles = new RegionModel("British Isles", "archipelago", "000000",
                null, null, britishIslesBounds);
        HashSet<RegionModel> regions = new HashSet<>();
        regions.add(britishIsles);
        MapModel currentMap = new MapModel("tile", 20966, 20966, 4096, regions);
        mapTileNames = currentMap.tileNames();
        mapWidth = currentMap.width();
        mapHeight = currentMap.height();
        tileSize = currentMap.tileSize();
        tileNumWidth = currentMap.tileNumWidth();
        tileNumHeight = currentMap.tileNumHeight();
        focusOnRegion(britishIsles);
    }

    public void focusOnRegion(RegionModel regionModel) {
        RegionBounds bounds = regionModel.bounds();
        currentMapBounds.set(new Rectangle2D(bounds.x(), bounds.y(), bounds.width(), bounds.height()));
    }

    public final ObjectProperty<Color> getOceanColorProperty() {return oceanColor;}
    public final ObjectProperty<Rectangle2D> getCurrentMapBoundsProperty() {return currentMapBounds;}
}
