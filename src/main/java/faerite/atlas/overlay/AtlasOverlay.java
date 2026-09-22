package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import javafx.scene.layout.*;

/// Contains all the UI elements overlaid over the map.
public class AtlasOverlay extends AnchorPane {

    private static final int SIDEBAR_MIN_WIDTH = 300;
    private static final int SIDEBAR_MAX_WIDTH = 1000;
    private static final int SIDEBAR_MIN_HEIGHT = 480;
    private static final double SIDEBAR_PADDING = 40.0;

    private static final int MIN_FONT_SIZE = 8;

    AtlasViewModel viewModel;

    BorderPane infoSidebar = new BorderPane();

    /// Creates the overlay using data from the view model.
    /// @param viewModel The global view model instance.
    public AtlasOverlay(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        // passes events to the pane behind if not directly over this pane's components
        pickOnBoundsProperty().set(false);

        double uiScale = viewModel.uiContext.getUiScale();

        // positioning
        infoSidebar.setMaxWidth(SIDEBAR_MAX_WIDTH * uiScale);
        infoSidebar.managedProperty().bind(infoSidebar.visibleProperty());

        this.widthProperty().addListener((_, _, newWidth) -> {
            double currentWidth = newWidth.doubleValue();

            int fontSize = Math.max(viewModel.uiContext.getUiElementFontSize(currentWidth), MIN_FONT_SIZE);
            this.setStyle("-fx-font-size: " + fontSize + "px;");

            double minSidebarWidthThreshold = SIDEBAR_MIN_WIDTH * uiScale * 4;

            if (currentWidth < minSidebarWidthThreshold) {
                infoSidebar.setVisible(false);
            } else {
                infoSidebar.setVisible(true);
                infoSidebar.setPrefWidth(currentWidth * 0.25);
            }
        });
        this.heightProperty().addListener((_, _, newHeight) -> {
            double currentHeight = newHeight.doubleValue();

            double minSidebarHeightThreshold = SIDEBAR_MIN_HEIGHT * uiScale;

            infoSidebar.setVisible(!(currentHeight < minSidebarHeightThreshold));
        });

        setTopAnchor(infoSidebar, SIDEBAR_PADDING * uiScale);
        setBottomAnchor(infoSidebar, SIDEBAR_PADDING * uiScale);
        setRightAnchor(infoSidebar, SIDEBAR_PADDING * uiScale);

        infoSidebar.getStyleClass().add("info-sidebar");

        // layout
        VBox infoSidebarHeader = new InfoSidebarHeaderView(viewModel);
        infoSidebar.setTop(infoSidebarHeader);

        StackPane infoSidebarBody = new InfoSidebarBodyView(viewModel);
        infoSidebar.setCenter(infoSidebarBody);

        getChildren().add(infoSidebar);
    }
}
