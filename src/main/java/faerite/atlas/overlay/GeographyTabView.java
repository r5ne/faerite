package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.atlas.map.RegionDataCache;
import faerite.model.RegionDataModel;
import faerite.model.RegionSelectionModel;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;


public class GeographyTabView extends VBox {
    AtlasViewModel viewModel;

    InfoSection areaSection;
    Label areaLabel = new Label();

    DecimalFormat formatter = new DecimalFormat("#,###.##");

    public GeographyTabView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        areaLabel.getStyleClass().add("info-section-value");

        areaSection = new InfoSection("Land area:", areaLabel);
        areaSection.managedProperty().bind(areaSection.visibleProperty());

        getChildren().addAll(areaSection);
    }


    public void updateLabels(RegionSelectionModel newRegion) {
        String id = newRegion != null ? newRegion.id() : viewModel.getActiveLayer().mapModel.id();
        RegionDataModel regionData = RegionDataCache.get(id);

        updateAreaLabel(regionData);
    }

    private void updateAreaLabel(RegionDataModel regionData) {
        Double area = regionData.area();

        if (area == null) {
            areaSection.setVisible(false);
            return;
        }

        areaSection.setVisible(true);
        String areaString;

        if (area >= 1000000000) {
            areaString = area / 1000000000 + " billion";
        } else if (area >= 1000000) {
            areaString = area / 1000000 + " million";
        } else {
            areaString = formatter.format(area);
        }

        areaLabel.setText(areaString + " km²");
    }
}