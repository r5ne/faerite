package faerite.view;

import faerite.viewmodel.AtlasViewModel;
import javafx.scene.layout.StackPane;

/// The root pane on which all content is drawn on.
public class RootView extends StackPane {

    /// Creates the root pane passing the global view model to all of its children.
    /// @param viewModel The global view model to pass to all other panes.
    public RootView(AtlasViewModel viewModel) {
        MapView map = new MapView(viewModel);
        InfoView info = new InfoView(viewModel);
        this.getChildren().addAll(map, info);
    }
}
