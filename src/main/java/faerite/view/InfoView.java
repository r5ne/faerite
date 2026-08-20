package faerite.view;

import faerite.MapViewModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class InfoView extends AnchorPane {
    private static final double PADDING = 40.0;
    private static final int INFO_CARD_MIN_WIDTH = 300;
    private static final int INFO_CARD_MAX_WIDTH = 1000;

    MapViewModel viewModel;

    public InfoView(MapViewModel viewModel) {
        this.viewModel = viewModel;

        // pass events to the pane behind if not directly over this pane's components
        pickOnBoundsProperty().set(false);

        BorderPane infoCard = new BorderPane();
        // positioning
        infoCard.prefWidthProperty().bind(this.widthProperty().multiply(0.25));
        infoCard.setMinWidth(INFO_CARD_MIN_WIDTH);
        infoCard.setMaxWidth(INFO_CARD_MAX_WIDTH);

        setTopAnchor(infoCard, PADDING);
        setBottomAnchor(infoCard, PADDING);
        setRightAnchor(infoCard, PADDING);

        // styling
        infoCard.getStyleClass().add("info-card");

        DropShadow dropShadow = new DropShadow(15, 0, 5, Color.color(0, 0, 0, 0.3));
        infoCard.setEffect(dropShadow);

        // layout
        HBox navigationBar = new HBox();
        infoCard.setTop(navigationBar);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);
        infoCard.setCenter(scrollPane);

        StackPane viewStack = new StackPane();
        scrollPane.setContent(viewStack);

        StatsView statsTab = new StatsView(viewModel);
        viewStack.getChildren().add(statsTab);

        getChildren().add(infoCard);
    }
}
