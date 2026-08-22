package faerite.view;

import faerite.viewmodel.AtlasViewModel;
import faerite.model.RegionData;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StatsView extends VBox {

    private final AtlasViewModel viewModel;

    private final Label titleLabel = new Label();
    private final Label typeLabel = new Label();

    public StatsView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        titleLabel.getStyleClass().add("h1");
        typeLabel.getStyleClass().add("body-text");

        // temporary
        // viewModel.getCurrentRegionDataProperty().addListener((_, _, newData) -> updateLabels(newData));
        // updateLabels(viewModel.getCurrentMap().regionData());

        getChildren().addAll(titleLabel, typeLabel);
    }

    private void updateLabels(RegionData data) {
        if (data != null) {
            titleLabel.setText(data.name());
            typeLabel.setText(String.format("Type: %s", data.type().getDisplayName()));
        }
    }
}
