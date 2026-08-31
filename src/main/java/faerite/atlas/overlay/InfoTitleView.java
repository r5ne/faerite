package faerite.atlas.overlay;

import faerite.atlas.AtlasStyle;
import faerite.atlas.AtlasViewModel;
import faerite.model.RegionData;
import faerite.model.RegionInfoSection;
import faerite.model.RegionSelectionModel;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class InfoTitleView extends VBox {

    AtlasViewModel viewModel;

    private final Label titleLabel = new Label();

    public InfoTitleView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        AtlasStyle style = viewModel.getStyle();
        setAlignment(style.infoBoxTitleAlignment());
        int paddingX = style.infoBoxHorisontalPadding();
        int paddingY = style.infoBoxVerticalPadding();
        setPadding(new Insets(paddingY, paddingX, 0, paddingX));

        titleLabel.getStyleClass().add("nav-title");
        viewModel.selectedRegionProperty().addListener((_, _, newRegion) -> updateLabels(newRegion));
        updateLabels(viewModel.getSelectedRegion());

        HBox navigationBar = new HBox();
        navigationBar.setAlignment(style.infoBoxTitleAlignment());
        navigationBar.getStyleClass().add("nav-bar");

        Button overviewButton = new Button("Overview");
        Button historyButton = new Button("History");
        Button geographyButton = new Button("Geography");

        overviewButton.setOnAction(_ -> viewModel.selectedInfoSectionProperty().set(RegionInfoSection.OVERVIEW));
        historyButton.setOnAction(_ -> viewModel.selectedInfoSectionProperty().set(RegionInfoSection.HISTORY));
        geographyButton.setOnAction(_ -> viewModel.selectedInfoSectionProperty().set(RegionInfoSection.GEOGRAPHY));

        overviewButton.setMaxWidth(Integer.MAX_VALUE);
        historyButton.setMaxWidth(Integer.MAX_VALUE);
        geographyButton.setMaxWidth(Integer.MAX_VALUE);
        HBox.setHgrow(overviewButton, Priority.ALWAYS);
        HBox.setHgrow(historyButton, Priority.ALWAYS);
        HBox.setHgrow(geographyButton, Priority.ALWAYS);
        overviewButton.getStyleClass().add("nav-button");
        historyButton.getStyleClass().add("nav-button");
        geographyButton.getStyleClass().add("nav-button");

        navigationBar.getChildren().addAll(overviewButton, historyButton, geographyButton);

        getChildren().addAll(titleLabel, navigationBar);
    }

    private void updateLabels(RegionSelectionModel newRegion) {
        RegionData regionData;
        if (newRegion != null) {
            regionData = newRegion.regionData();
        } else {
            regionData = viewModel.getActiveLayer().mapModel.regionData();
        }
        titleLabel.setText(regionData.name());
    }
}
