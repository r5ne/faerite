import javafx.beans.property.*;
import javafx.scene.paint.Color;

import java.util.HashSet;

public class MapViewModel {
    private ObjectProperty<RegionModel> hoveredRegion;

    private final IntegerProperty mapWidth = new SimpleIntegerProperty();
    private final IntegerProperty mapHeight = new SimpleIntegerProperty();
    private final DoubleProperty scaleX = new SimpleDoubleProperty(1);
    private final DoubleProperty scaleY = new SimpleDoubleProperty(1);

    private final ObjectProperty<Color> oceanColor = new SimpleObjectProperty<>(Color.web("#213840"));

    public MapViewModel() {

        RegionBounds britishIslesBounds = new RegionBounds(9716, 5954, 941, 1254);
        RegionModel britishIsles = new RegionModel("British Isles", "archipelago", "000000",
                null, null, britishIslesBounds);
        HashSet<RegionModel> regions = new HashSet<>();
        regions.add(britishIsles);
    }

    public final ObjectProperty<Color> getOceanColorProperty() {return oceanColor;}
    public final ObjectProperty<Rectangle2D> getCurrentMapBoundsProperty() {return currentMapBounds;}
}
