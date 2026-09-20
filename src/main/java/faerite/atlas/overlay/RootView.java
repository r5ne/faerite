package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.atlas.map.MapView;
import faerite.util.UIScaler;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.StackPane;

/// The root pane on which all content is drawn on.
public class RootView extends StackPane {

    /// Creates the root pane passing the global view model to all of its children.
    /// @param viewModel The global view model to pass to all other panes.
    public RootView(AtlasViewModel viewModel) {
        setStyle("-fx-font-size: " + UIScaler.getFontSize() + "px;");

        MapView map = new MapView(viewModel);
        AtlasOverlay info = new AtlasOverlay(viewModel);
        this.getChildren().addAll(map, info);

        this.setFocusTraversable(true);

        this.setOnKeyPressed(event -> {

            if (this.getScene() != null && this.getScene().getFocusOwner() instanceof TextInputControl) {
                return;
            }

            map.getOnKeyPressed().handle(event);
        });

        this.requestFocus();
    }
}
