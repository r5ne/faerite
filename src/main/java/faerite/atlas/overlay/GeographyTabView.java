package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.atlas.map.RegionDataCache;
import faerite.model.Habitat;
import faerite.model.KoeppenClimateClassification;
import faerite.model.RegionDataModel;
import faerite.model.RegionSelectionModel;
import java.util.Set;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class GeographyTabView extends VBox {

    AtlasViewModel viewModel;

    InfoSection elevationSection;
    Label elevationLabel = new Label();
    Label elevationNameLabel = new Label();

    InfoSection climateSection;
    InfoSection habitatSection;

    public GeographyTabView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        elevationLabel.getStyleClass().add("info-section-value");
        elevationNameLabel.getStyleClass().add("info-section-description");
        elevationNameLabel.setWrapText(true);
        elevationNameLabel.managedProperty().bind(elevationNameLabel.visibleProperty());
        elevationSection = new InfoSection("Highest elevation:", elevationLabel, elevationNameLabel);
        elevationSection.managedProperty().bind(elevationSection.visibleProperty());

        climateSection = new InfoSection("Climates:");
        habitatSection = new InfoSection("Habitats:");

        getChildren().addAll(elevationSection, climateSection, habitatSection);
    }

    public void updateLabels(RegionSelectionModel newRegion) {
        String id = newRegion != null ? newRegion.id() : viewModel.getActiveLayer().mapModel.id();
        RegionDataModel regionData = RegionDataCache.get(id);

        updateElevationLabel(regionData);
        updateClimateLabels(regionData);
        updateHabitatLabels(regionData);
    }

    private void updateElevationLabel(RegionDataModel regionData) {
        Double elevation = regionData.highestElevation();
        String elevationName = regionData.highestElevationName();

        if (elevation == null) {
            elevationSection.setVisible(false);
            return;
        }
        elevationSection.setVisible(true);
        elevationLabel.setText(RegionDataFormatter.formatDouble(elevation) + " m");

        if (elevationName == null) {
            elevationNameLabel.setVisible(false);
            return;
        }
        elevationNameLabel.setVisible(true);
        elevationNameLabel.setText(elevationName);
    }

    private void updateClimateLabels(RegionDataModel regionData) {
        Set<KoeppenClimateClassification> climates = regionData.climates();

        if (climates.isEmpty()) {
            climateSection.setVisible(false);
            return;
        }

        climateSection.clearContent();

        for (KoeppenClimateClassification climate : climates) {
            Label climateCodeLabel = new Label(String.format("%s (%s)", climate.getOverview(), climate.getCode()));
            climateCodeLabel.getStyleClass().add("info-section-value");

            Label climateDescriptionLabel = new Label(climate.getDescription());
            climateDescriptionLabel.getStyleClass().add("info-section-description");

            climateSection.addContent(climateCodeLabel, climateDescriptionLabel);
        }

        climateSection.setVisible(true);
    }

    private void updateHabitatLabels(RegionDataModel regionData) {
        Set<Habitat> habitats = regionData.habitats();

        if (habitats.isEmpty()) {
            habitatSection.setVisible(false);
            return;
        }

        habitatSection.clearContent();

        for (Habitat habitat : habitats) {
            Label habitatNameLabel = new Label(habitat.getDisplayName());
            habitatNameLabel.getStyleClass().add("info-section-value");

            habitatSection.addContent(habitatNameLabel);

            if (!habitat.getDescription().isEmpty()) {
                Label habitatDescriptionLabel = new Label(habitat.getDescription());
                habitatDescriptionLabel.getStyleClass().add("info-section-description");

                habitatSection.addContent(habitatDescriptionLabel);
            }
        }

        habitatSection.setVisible(true);
    }
}
