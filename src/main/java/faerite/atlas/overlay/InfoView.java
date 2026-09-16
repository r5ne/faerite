package faerite.atlas.overlay;

import faerite.atlas.AtlasStyle;
import faerite.atlas.map.RegionDataCache;
import faerite.model.RegionInfoSection;
import faerite.model.RegionSelectionModel;
import faerite.atlas.AtlasViewModel;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

/// Contains the information UI for the selected region.
public class InfoView extends VBox {
    private final AtlasViewModel viewModel;

    private final StackPane viewStack = new StackPane();

    private final Label typeLabel = new Label();

    MarkdownView overview = new MarkdownView();
    MarkdownView historyInfo = new MarkdownView();
    MarkdownView geographyInfo = new MarkdownView();
    private final Map<RegionInfoSection, MarkdownView> infoSectionViewMap = new HashMap<>(Map.of(
            RegionInfoSection.OVERVIEW, overview,
            RegionInfoSection.HISTORY, historyInfo,
            RegionInfoSection.GEOGRAPHY, geographyInfo
    ));

    /// Creates the information UI using data from the view model.
    /// @param viewModel The global view model instance.
    public InfoView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;


        AtlasStyle style = viewModel.getStyle();
        int paddingX = style.infoBoxHorisontalPadding();
        int paddingY = style.infoBoxVerticalPadding();
        setPadding(new Insets(paddingY, paddingX, paddingY, paddingX));
        typeLabel.getStyleClass().add("body-text");

        viewModel.selectedInfoSectionProperty().addListener((_, oldSection, newSection) -> {
            infoSectionViewMap.get(oldSection).setVisible(false);
            MarkdownView newSectionView = infoSectionViewMap.get(newSection);
            newSectionView.setVisible(true);
            viewStack.getChildren().setAll(newSectionView);
        });

        historyInfo.setVisible(false);
        geographyInfo.setVisible(false);
        viewStack.getChildren().add(overview);

        getChildren().add(viewStack);

        viewModel.selectedRegionProperty().addListener((_, _, newRegion) -> {updateLabels(newRegion); updateMarkdown(newRegion);});
        updateLabels(viewModel.getSelectedRegion());
        updateMarkdown(viewModel.getSelectedRegion());
    }

    private void updateLabels(RegionSelectionModel newRegion) {
        String id = (newRegion != null)
                ? newRegion.id()
                : viewModel.getActiveLayer().mapModel.id();
        typeLabel.setText(String.format("Type: %s", RegionDataCache.get(id).type().getDisplayName()));
    }

    private void updateMarkdown(RegionSelectionModel newRegion) {
    }
}
