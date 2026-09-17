package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import javafx.scene.layout.VBox;

public class HistoryTabView extends VBox {
    AtlasViewModel viewModel;

    public HistoryTabView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;
    }
}