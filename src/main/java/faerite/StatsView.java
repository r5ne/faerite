package faerite;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StatsView extends VBox {
    private final MapViewModel viewModel;

    public StatsView(MapViewModel viewModel) {
        this.viewModel = viewModel;

        Label titleLabel = new Label();
        titleLabel.getStyleClass().add("h1");

        Label typeLabel = new Label();
        typeLabel.getStyleClass().add("body-text");

        viewModel.getCurrentRegionDataProperty().addListener((_, _, newData) -> {
            if (newData != null) {
                titleLabel.setText(newData.name());
                typeLabel.setText(String.format("Type: %s", newData.type()));
            }
        });

        getChildren().addAll(titleLabel, typeLabel);
    }
}
