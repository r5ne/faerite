package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.atlas.map.RegionDataCache;
import faerite.model.RegionSelectionModel;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class OverviewTabView extends VBox {

    AtlasViewModel viewModel;

    Label populationNumberLabel = new Label();

    public OverviewTabView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        InfoSection populationSection = new InfoSection("Population:", populationNumberLabel);
    }

    public void updateLabels(RegionSelectionModel newRegion) {
        String id = newRegion != null ? newRegion.id() : viewModel.getActiveLayer().mapModel.id();

        populationNumberLabel.setText(String.valueOf(RegionDataCache.get(id).population()));
    }
}
