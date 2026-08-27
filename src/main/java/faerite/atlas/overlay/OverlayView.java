package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/// Contains all the UI elements overlaid over the map.
public class OverlayView extends AnchorPane {

    private static final double PADDING = 40.0;
    private static final int INFO_CARD_MIN_WIDTH = 300;
    private static final int INFO_CARD_MAX_WIDTH = 1000;

    AtlasViewModel viewModel;

    /// Creates the overlay using data from the view model.
    /// @param viewModel The global view model instance.
    public OverlayView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        // passes events to the pane behind if not directly over this pane's components
        pickOnBoundsProperty().set(false);

        BorderPane overlayPane = new BorderPane();
        // positioning
        overlayPane.prefWidthProperty().bind(this.widthProperty().multiply(0.25));
        overlayPane.setMinWidth(INFO_CARD_MIN_WIDTH);
        overlayPane.setMaxWidth(INFO_CARD_MAX_WIDTH);

        setTopAnchor(overlayPane, PADDING);
        setBottomAnchor(overlayPane, PADDING);
        setRightAnchor(overlayPane, PADDING);

        // styling
        overlayPane.getStyleClass().add("info-card");

        DropShadow dropShadow = new DropShadow(15, 0, 5, Color.color(0, 0, 0, 0.3));
        overlayPane.setEffect(dropShadow);

        // layout
        HBox navigationBar = new HBox();
        overlayPane.setTop(navigationBar);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);

        VBox infoBox = new InfoView(viewModel);
        scrollPane.setContent(infoBox);

        overlayPane.setCenter(scrollPane);

        getChildren().add(overlayPane);
    }
}
