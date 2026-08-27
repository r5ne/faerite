package faerite.atlas.overlay;

import faerite.io.MapAssetCache;
import faerite.model.RegionData;
import faerite.model.RegionModel;
import faerite.model.RegionSelectionModel;
import faerite.atlas.AtlasViewModel;
import faerite.atlas.MapViewModel;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/// Contains the information UI for the selected region.
public class InfoView extends VBox {
    private final AtlasViewModel viewModel;

    private final StackPane viewStack = new StackPane();

    private final Label titleLabel = new Label();
    private final Label typeLabel = new Label();

    MarkdownView overview = new MarkdownView();
    MarkdownView historyInfo = new MarkdownView();
    MarkdownView geographyInfo = new MarkdownView();

    private final ChangeListener<RegionSelectionModel> selectedRegionListener;

    /// Creates the information UI using data from the view model.
    /// @param viewModel The global view model instance.
    public InfoView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        setSpacing(15.0);
        titleLabel.getStyleClass().add("h1");
        typeLabel.getStyleClass().add("body-text");
        selectedRegionListener = (_, _, newRegion) -> { updateLabels(newRegion); updateMarkdown(newRegion); };

        Separator titleBodySeparator = new Separator();

        historyInfo.setVisible(false);
        geographyInfo.setVisible(false);
        viewStack.getChildren().add(overview);

        getChildren().addAll(titleLabel, titleBodySeparator, viewStack);

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

    private void updateMarkdown(RegionSelectionModel newRegion) {
        RegionModel region = newRegion;
        if (newRegion == null) {
            region = viewModel.getActiveLayer().mapModel;
        }
        overview.setMarkdown(MapAssetCache.getMarkdown(region.markdownFileName("overview")));
        geographyInfo.setMarkdown(MapAssetCache.getMarkdown(region.markdownFileName("geography")));
        historyInfo.setMarkdown(MapAssetCache.getMarkdown(region.markdownFileName("history")));
    }

    private void updateListeners(MapViewModel oldMap, MapViewModel newMap) {
        if (oldMap != null) {
            oldMap.getSelectedRegionProperty().removeListener(selectedRegionListener);
        }
        newMap.getSelectedRegionProperty().addListener(selectedRegionListener);
        updateLabels(newMap.getSelectedRegion());
        updateMarkdown(newMap.getSelectedRegion());
    }
}
