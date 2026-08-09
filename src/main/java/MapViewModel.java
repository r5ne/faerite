import javafx.beans.property.*;
import javafx.scene.paint.Color;

import java.util.HashSet;

public class MapViewModel {
    private final HashSet<Region> regions = new HashSet<>();
    private ObjectProperty<Region> hoveredRegion;

    private final IntegerProperty mapWidth = new SimpleIntegerProperty();
    private final IntegerProperty mapHeight = new SimpleIntegerProperty();
    private final DoubleProperty scaleX = new SimpleDoubleProperty(1);
    private final DoubleProperty scaleY = new SimpleDoubleProperty(1);

    private final ObjectProperty<Color> oceanColor = new SimpleObjectProperty<>(Color.web("#213840"));

    public MapViewModel() {
        int[] britishIslesPos = {9716, 5954};
        int[] britishIslesSize = {1254, 941};
        Region britishIsles = new Region("British Isles", "archipelago", "000000",
                null, null, britishIslesPos, britishIslesSize);
        regions.add(britishIsles);
    }

    public final ObjectProperty<Color> getOceanColorProperty() {return oceanColor;}
    public final ObjectProperty<Rectangle2D> getCurrentMapBoundsProperty() {return currentMapBounds;}
}
