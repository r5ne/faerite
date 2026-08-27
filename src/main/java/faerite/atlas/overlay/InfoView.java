package faerite.atlas.overlay;

import faerite.atlas.AtlasStyle;
import faerite.io.MapAssetCache;
import faerite.model.RegionData;
import faerite.model.RegionModel;
import faerite.model.RegionSelectionModel;
import faerite.atlas.AtlasViewModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
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

    /// Creates the information UI using data from the view model.
    /// @param viewModel The global view model instance.
    public InfoView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        setSpacing(15.0);
        titleLabel.getStyleClass().add("h1");
        AtlasStyle style = viewModel.getStyle();
        int paddingX = style.infoBoxHorisontalPadding();
        int paddingY = style.infoBoxVerticalPadding();
        setPadding(new Insets(paddingY, paddingX, paddingY, paddingX));
        typeLabel.getStyleClass().add("body-text");

        historyInfo.setVisible(false);
        geographyInfo.setVisible(false);
        viewStack.getChildren().add(overview);

        getChildren().addAll(titleLabel, titleBodySeparator, viewStack);

        viewModel.selectedRegionProperty().addListener((_, _, newRegion) -> {updateLabels(newRegion); updateMarkdown(newRegion);});
        updateLabels(viewModel.getSelectedRegion());
        updateMarkdown(viewModel.getSelectedRegion());
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
}
