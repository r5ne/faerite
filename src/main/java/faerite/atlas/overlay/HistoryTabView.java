package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.model.RegionSelectionModel;
import javafx.scene.layout.VBox;

public class HistoryTabView extends VBox {
    AtlasViewModel viewModel;

    public HistoryTabView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public void updateLabels(RegionSelectionModel newRegion) {
    }
}