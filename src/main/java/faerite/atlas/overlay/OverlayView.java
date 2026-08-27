package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

/// Contains all the UI elements overlaid over the map.
public class OverlayView extends AnchorPane {

    private static final int WINDOW_MIN_WIDTH = 300;
    private static final int WINDOW_MAX_WIDTH = 1000;

    AtlasViewModel viewModel;

    /// Creates the overlay using data from the view model.
    /// @param viewModel The global view model instance.
    public OverlayView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        // passes events to the pane behind if not directly over this pane's components
        pickOnBoundsProperty().set(false);

        BorderPane infoWindow = new BorderPane();

        // positioning
        infoWindow.prefWidthProperty().bind(this.widthProperty().multiply(0.25));
        infoWindow.setMinWidth(WINDOW_MIN_WIDTH);
        infoWindow.setMaxWidth(WINDOW_MAX_WIDTH);

        AtlasStyle style = viewModel.getStyle();
        setTopAnchor(infoWindow, style.mapPadding());
        setBottomAnchor(infoWindow, style.mapPadding());
        setRightAnchor(infoWindow, style.mapPadding());

        // styling
        infoWindow.getStyleClass().add("info-window");

        DropShadow dropShadow = new DropShadow(15, 0, 5, Color.color(0, 0, 0, 0.3));
        infoWindow.setEffect(dropShadow);

        // layout
        VBox titleBar = new InfoTitleView(viewModel);

        infoWindow.setTop(titleBar);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);

        VBox infoBox = new InfoView(viewModel);
        scrollPane.setContent(infoBox);

        infoWindow.setCenter(scrollPane);

        getChildren().add(infoWindow);
    }
}
