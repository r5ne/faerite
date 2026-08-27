package faerite.atlas.overlay;

import faerite.io.MapAssetCache;
import faerite.model.RegionData;
import faerite.model.RegionModel;
import faerite.model.RegionSelectionModel;
import faerite.viewmodel.AtlasViewModel;
import faerite.viewmodel.MapViewModel;
import faerite.atlas.AtlasViewModel;
import faerite.atlas.MapViewModel;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/// Contains the information UI for the selected region.
public class InfoView extends VBox {
    private final AtlasViewModel viewModel;

    private final Label titleLabel = new Label();
    private final Label typeLabel = new Label();

    private final ChangeListener<RegionSelectionModel> selectedRegionListener;

    public StatsView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        titleLabel.getStyleClass().add("h1");
        typeLabel.getStyleClass().add("body-text");

        selectedRegionListener = (_, _, newRegion) -> updateLabels(newRegion);

        getChildren().addAll(titleLabel, typeLabel);

        viewModel.activeLayerProperty().addListener((_, oldMap, newMap) -> updateListeners(oldMap, newMap));
        updateListeners(null, viewModel.getActiveLayer());
    }

    private void updateLabels(RegionSelectionModel newRegion) {
        RegionData regionData;
        if (newRegion != null) {
            regionData = newRegion.regionData();
        } else {
            regionData = viewModel.getActiveLayer().mapModel.regionData();
        }
        titleLabel.setText(regionData.name());
        typeLabel.setText(String.format("Type: %s", regionData.type().getDisplayName()));
    }

    private void updateListeners(MapViewModel oldMap, MapViewModel newMap) {
        if (oldMap != null) {
            oldMap.getSelectedRegionProperty().removeListener(selectedRegionListener);
        }
        newMap.getSelectedRegionProperty().addListener(selectedRegionListener);
        updateLabels(newMap.getSelectedRegion());
    }
}
