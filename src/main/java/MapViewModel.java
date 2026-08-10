import javafx.beans.property.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class MapViewModel {
    private final Map<String, RegionModel> colorRegionMap = new HashMap<>();
    private final ObjectProperty<RegionModel> hoveredRegion = new SimpleObjectProperty<>();

    public final String mapTileName;
    public final String borderTileName;
    public final String hitboxTileName;
    public final int mapWidth;
    public final int mapHeight;
    public final int tileSize;
    public final int tileNumWidth;
    public final int tileNumHeight;

    private final MapModel currentMap;
    private ObjectProperty<Rectangle2D> currentMapBounds = new SimpleObjectProperty<>();
    private final ObjectProperty<Color> oceanColor = new SimpleObjectProperty<>(Color.web("#213840"));

    public MapViewModel() {
        RegionBounds britishIslesBounds = new RegionBounds(9716, 5954, 941, 1254);
        RegionModel britishIsles = new RegionModel("British Isles", "archipelago", "000000FF",
                null, null, britishIslesBounds);
        colorRegionMap.put("00000000", null);
        colorRegionMap.put(britishIsles.maskColor(), britishIsles);
        HashSet<RegionModel> regions = new HashSet<>();
        regions.add(britishIsles);

        currentMap = new MapModel("map", "borders_map_0", "hitbox_map_0", 20966, 20966, 4096, regions);
        mapTileName = currentMap.tileName();
        borderTileName = currentMap.bordersTileName();
        hitboxTileName = currentMap.hitboxTileName();
        mapWidth = currentMap.width();
        mapHeight = currentMap.height();
        tileSize = currentMap.tileSize();
        tileNumWidth = currentMap.tileNumWidth();
        tileNumHeight = currentMap.tileNumHeight();

        setViewedMapRegion(britishIsles);
    }

    public void setViewedMapRegion(RegionModel regionModel) {
        RegionBounds bounds = regionModel.bounds();
        currentMapBounds.set(new Rectangle2D(bounds.x(), bounds.y(), bounds.width(), bounds.height()));
    }

    public void updateHoveredColor(Color color) {
        String hexCode = (format(color.getRed()) + format(color.getGreen()) +
                format(color.getBlue()) + format(color.getOpacity())).toUpperCase();

        RegionModel region = colorRegionMap.get(hexCode);

        if (hoveredRegion != null && hoveredRegion.get() != region) {
            hoveredRegion.set(region);
            System.out.println(hexCode);
        }
    }

    public final ObjectProperty<Color> getOceanColorProperty() {return oceanColor;}
    public final ObjectProperty<Rectangle2D> getCurrentMapBoundsProperty() {return currentMapBounds;}


    private static String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }
}
