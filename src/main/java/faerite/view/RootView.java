package faerite.view;

import faerite.viewmodel.AtlasViewModel;
import javafx.scene.layout.StackPane;

public class RootView extends StackPane {

    public RootView(AtlasViewModel viewModel) {
        MapView map = new MapView(viewModel);
        InfoView info = new InfoView(viewModel);
        this.getChildren().addAll(map, info);
    }
}
