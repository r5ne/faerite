import javafx.scene.layout.StackPane;

public class RootView extends StackPane {
    public RootView(MapViewModel viewModel) {
        MapView map = new MapView();
        this.getChildren().add(map);
    }
}
