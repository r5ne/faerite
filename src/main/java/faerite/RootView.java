package faerite;

import javafx.scene.layout.StackPane;

public class RootView extends StackPane {
    public RootView(MapViewModel viewModel) {
        MapView map = new MapView(viewModel);
        InfoView info = new InfoView(viewModel);
        this.getChildren().addAll(map, info);
    }
}
