package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.atlas.map.RegionDataCache;
import faerite.model.RegionDataModel;
import faerite.model.RegionSelectionModel;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class GeographyTabView extends VBox {
    AtlasViewModel viewModel;

    InfoSection elevationSection;
    Label elevationLabel = new Label();
    Label elevationNameLabel = new Label();

    public GeographyTabView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        elevationLabel.getStyleClass().add("info-section-value");
        elevationNameLabel.getStyleClass().add("info-section-description");
        elevationNameLabel.managedProperty().bind(elevationNameLabel.visibleProperty());
        elevationSection = new InfoSection("Highest elevation:", elevationLabel, elevationNameLabel);
        elevationSection.managedProperty().bind(elevationSection.visibleProperty());

        getChildren().addAll(elevationSection);
    }

    public void updateLabels(RegionSelectionModel newRegion) {
        String id = newRegion != null ? newRegion.id() : viewModel.getActiveLayer().mapModel.id();
        RegionDataModel regionData = RegionDataCache.get(id);

        updateElevationLabel(regionData);
    }

    private void updateElevationLabel(RegionDataModel regionData) {
        Double elevation = regionData.highestElevation();
        String elevationName = regionData.highestElevationName();

        if (elevation == null) {
            elevationSection.setVisible(false);
            return;
        }
        elevationSection.setVisible(true);
        elevationLabel.setText(RegionDataFormatter.formatDouble(elevation) + " m²");

        if (elevationName == null) {
            elevationNameLabel.setVisible(false);
            return;
        }
        elevationNameLabel.setVisible(true);
        elevationNameLabel.setText(elevationName);
    }
}