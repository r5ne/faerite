package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.atlas.map.RegionDataCache;
import faerite.model.RegionDataModel;
import faerite.model.RegionSelectionModel;
import faerite.model.RegionType;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class OverviewTabView extends VBox {

    AtlasViewModel viewModel;

    InfoSection regionTypeSection;
    Label regionTypeLabel = new Label();

    InfoSection populationSection;
    Label populationNumberLabel = new Label();

    InfoSection areaSection;
    Label areaLabel = new Label();

    InfoSection populationDensitySection;
    Label populationDensityLabel = new Label();


    public OverviewTabView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        regionTypeLabel.getStyleClass().add("info-section-value");
        regionTypeSection = new InfoSection("Region type:", regionTypeLabel);
        regionTypeSection.managedProperty().bind(regionTypeSection.visibleProperty());

        populationNumberLabel.getStyleClass().add("info-section-value");
        populationSection = new InfoSection("Population:", populationNumberLabel);
        populationSection.managedProperty().bind(populationSection.visibleProperty());

        areaLabel.getStyleClass().add("info-section-value");
        areaSection = new InfoSection("Land area:", areaLabel);
        areaSection.managedProperty().bind(areaSection.visibleProperty());

        populationDensityLabel.getStyleClass().add("info-section-value");
        populationDensityLabel.setWrapText(true);
        populationDensitySection = new InfoSection("Population density:", populationDensityLabel);
        populationDensitySection.managedProperty().bind(populationDensitySection.visibleProperty());

        getChildren().addAll(regionTypeSection, populationSection, areaSection, populationDensitySection);
    }

    public void updateLabels(RegionSelectionModel newRegion) {
        String id = newRegion != null ? newRegion.id() : viewModel.getActiveLayer().mapModel.id();
        RegionDataModel regionData = RegionDataCache.get(id);

        regionTypeLabel.setText(regionData.type().getDisplayName());
        updatePopulationLabel(regionData);
        updateAreaLabel(regionData);
        updatePopulationDensityLabel(regionData);
    }

    private void updatePopulationLabel(RegionDataModel regionData) {
        Long population = regionData.population();

        if (population == null) {
            populationSection.setVisible(false);
            return;
        }

        String populationString;

        if (population >= 1000000000) {
            populationString = population / 1000000000 + " billion";
        } else if (population >= 1000000) {
            populationString = population / 1000000 + " million";
        } else {
            populationString = String.format("%,d", population);
        }

        populationNumberLabel.setText(populationString);
        populationSection.setVisible(true);
    }

    private void updateAreaLabel(RegionDataModel regionData) {
        Double area = regionData.area();

        if (area == null) {
            areaSection.setVisible(false);
            return;
        }
        areaLabel.setText(RegionDataFormatter.formatDouble(area) + " km²");
        areaSection.setVisible(true);
    }

    private void updatePopulationDensityLabel(RegionDataModel regionData) {
        Long population = regionData.population();
        Double area = regionData.area();

        if (area == null || population == null || population == 0) {
            populationDensitySection.setVisible(false);
            return;
        }

        long populationDensity = Math.round(population / area);
        populationDensityLabel.setText(RegionDataFormatter.formatLong(populationDensity) + " people per km²");
        populationDensitySection.setVisible(true);
    }
}