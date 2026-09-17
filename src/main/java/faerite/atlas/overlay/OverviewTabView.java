package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.atlas.map.RegionDataCache;
import faerite.model.RegionDataModel;
import faerite.model.RegionSelectionModel;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class OverviewTabView extends VBox {

    AtlasViewModel viewModel;

    InfoSection regionTypeSection;
    Label regionTypeLabel = new Label();

    InfoSection populationSection;
    Label populationNumberLabel = new Label();

    public OverviewTabView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        regionTypeLabel.getStyleClass().add("info-section-value");
        populationNumberLabel.getStyleClass().add("info-section-value");

        populationSection = new InfoSection("Population:", populationNumberLabel);
        populationSection.managedProperty().bind(populationSection.visibleProperty());

        regionTypeSection = new InfoSection("Region type:", regionTypeLabel);
        regionTypeSection.managedProperty().bind(regionTypeSection.visibleProperty());

        getChildren().addAll(regionTypeSection, populationSection);
    }

    public void updateLabels(RegionSelectionModel newRegion) {
        String id = newRegion != null ? newRegion.id() : viewModel.getActiveLayer().mapModel.id();
        RegionDataModel regionData = RegionDataCache.get(id);

        regionTypeLabel.setText(regionData.type().getDisplayName());
        updatePopulationLabel(regionData);
    }

    private void updatePopulationLabel(RegionDataModel regionData) {
        Long population = regionData.population();

        if (population == null) {
            populationSection.setVisible(false);
            return;
        }

        populationSection.setVisible(true);
        String populationString;

        if (population >= 1000000000) {
            populationString = population / 1000000000 + " billion";
        } else if (population >= 1000000) {
            populationString = population / 1000000 + " million";
        } else {
            populationString = String.format("%,d", population);
        }

        populationNumberLabel.setText(populationString);
    }
}
