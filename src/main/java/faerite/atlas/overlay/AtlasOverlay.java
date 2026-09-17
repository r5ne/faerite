package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import javafx.scene.layout.*;

/// Contains all the UI elements overlaid over the map.
public class AtlasOverlay extends AnchorPane {

    private static final int SIDEBAR_MIN_WIDTH = 300;
    private static final int SIDEBAR_MAX_WIDTH = 1000;
    private static final double SIDEBAR_PADDING = 40.0;

    AtlasViewModel viewModel;

    /// Creates the overlay using data from the view model.
    /// @param viewModel The global view model instance.
    public AtlasOverlay(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        // passes events to the pane behind if not directly over this pane's components
        pickOnBoundsProperty().set(false);

        BorderPane infoSidebar = new BorderPane();

        // positioning
        infoSidebar.prefWidthProperty().bind(this.widthProperty().multiply(0.25));
        infoSidebar.setMinWidth(SIDEBAR_MIN_WIDTH);
        infoSidebar.setMaxWidth(SIDEBAR_MAX_WIDTH);

        setTopAnchor(infoSidebar, SIDEBAR_PADDING);
        setBottomAnchor(infoSidebar, SIDEBAR_PADDING);
        setRightAnchor(infoSidebar, SIDEBAR_PADDING);

        infoSidebar.getStyleClass().add("info-sidebar");

        // layout
        VBox infoSidebarHeader = new InfoSidebarHeaderView(viewModel);
        infoSidebar.setTop(infoSidebarHeader);

        StackPane infoSidebarBody = new InfoSidebarBodyView(viewModel);
        infoSidebar.setCenter(infoSidebarBody);

        getChildren().add(infoSidebar);
    }
}
