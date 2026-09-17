package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.atlas.map.RegionDataCache;
import faerite.model.RegionDataModel;
import faerite.model.RegionSelectionModel;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class OverviewTabView extends VBox {

    AtlasViewModel viewModel;

    Label populationNumberLabel = new Label();

    public OverviewTabView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        populationNumberLabel.getStyleClass().add("info-section-value");

        InfoSection populationSection = new InfoSection("Population:", populationNumberLabel);
        populationSection.managedProperty().bind(populationSection.visibleProperty());

        getChildren().addAll(populationSection);
    }

    public void updateLabels(RegionSelectionModel newRegion) {
        String id = newRegion != null ? newRegion.id() : viewModel.getActiveLayer().mapModel.id();
        RegionDataModel regionData = RegionDataCache.get(id);

        updatePopulationLabel(regionData);
    }

    private void updatePopulationLabel(RegionDataModel regionData) {
        Long population = regionData.population();

        if (population == null) {
            populationNumberLabel.setVisible(false);
            return;
        }

        populationNumberLabel.setVisible(true);
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
