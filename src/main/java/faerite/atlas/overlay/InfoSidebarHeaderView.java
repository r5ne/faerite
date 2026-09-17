package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.atlas.map.RegionDataCache;
import faerite.model.RegionInfoSection;
import faerite.model.RegionSelectionModel;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class InfoSidebarHeaderView extends VBox {

    AtlasViewModel viewModel;

    private final Label titleLabel = new Label();

    public InfoSidebarHeaderView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        titleLabel.getStyleClass().add("nav-title");
        viewModel.selectedRegionProperty().addListener((_, _, newRegion) -> updateLabels(newRegion));
        updateLabels(viewModel.getSelectedRegion());

        HBox navigationBar = new HBox();
        navigationBar.getStyleClass().add("nav-bar");

        ToggleButton overviewButton = new ToggleButton("Overview");
        ToggleButton historyButton = new ToggleButton("History");
        ToggleButton geographyButton = new ToggleButton("Geography");

        overviewButton.setOnAction(_ -> viewModel.selectedInfoSectionProperty().set(RegionInfoSection.OVERVIEW));
        historyButton.setOnAction(_ -> viewModel.selectedInfoSectionProperty().set(RegionInfoSection.HISTORY));
        geographyButton.setOnAction(_ -> viewModel.selectedInfoSectionProperty().set(RegionInfoSection.GEOGRAPHY));

        overviewButton.getStyleClass().add("nav-button");
        historyButton.getStyleClass().add("nav-button");
        geographyButton.getStyleClass().add("nav-button");

        viewModel.selectedInfoSectionProperty().addListener((_, _, selectedSection) -> {
            overviewButton.setSelected(selectedSection == RegionInfoSection.OVERVIEW);
            historyButton.setSelected(selectedSection == RegionInfoSection.HISTORY);
            geographyButton.setSelected(selectedSection == RegionInfoSection.GEOGRAPHY);
        });

        overviewButton.setSelected(true);

        navigationBar.getChildren().addAll(overviewButton, historyButton, geographyButton);

        getChildren().addAll(titleLabel, navigationBar);
    }

    private void updateLabels(RegionSelectionModel newRegion) {
        String id = newRegion != null ? newRegion.id() : viewModel.getActiveLayer().mapModel.id();
        titleLabel.setText(RegionDataCache.get(id).name());
    }
}
