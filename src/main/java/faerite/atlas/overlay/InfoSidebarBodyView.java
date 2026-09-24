package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.model.RegionInfoSection;
import faerite.model.RegionSelectionModel;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/// Contains the information UI for the selected region.
public class InfoSidebarBodyView extends StackPane {

    private final AtlasViewModel viewModel;

    private final OverviewTabView overviewTab;
    private final HistoryTabView historyTab;
    private final GeographyTabView geographyTab;

    /// Creates the information UI using data from the view model.
    /// @param viewModel The global view model instance.
    public InfoSidebarBodyView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        // Stack to allow switching between tab contents.
        StackPane tabStack = new StackPane();

        overviewTab = new OverviewTabView(viewModel);
        historyTab = new HistoryTabView(viewModel);
        geographyTab = new GeographyTabView(viewModel);

        overviewTab.getStyleClass().add("info-sidebar-body");
        historyTab.getStyleClass().add("info-sidebar-body");
        geographyTab.getStyleClass().add("info-sidebar-body");

        overviewTab.managedProperty().bind(overviewTab.visibleProperty());
        historyTab.managedProperty().bind(historyTab.visibleProperty());
        geographyTab.managedProperty().bind(geographyTab.visibleProperty());

        viewModel.selectedRegionProperty().addListener((_, _, newRegion) -> updateTabs(newRegion));
        updateTabs(null);

        historyTab.setVisible(false);
        geographyTab.setVisible(false);

        viewModel.selectedInfoSectionProperty().addListener((_, _, newTab) -> {
            overviewTab.setVisible(newTab == RegionInfoSection.OVERVIEW);
            historyTab.setVisible(newTab == RegionInfoSection.HISTORY);
            geographyTab.setVisible(newTab == RegionInfoSection.GEOGRAPHY);
        });

        tabStack.getChildren().addAll(overviewTab, historyTab, geographyTab);

        ScrollPane scrollPane = createScrollElements();
        scrollPane.setContent(tabStack);
    }

    private ScrollPane createScrollElements() {
        // Everything in the body of the info sidebar is scrollable.
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        // Gradient overlay at the bottom to indicate you can scroll down
        Region scrollGradient = new Region();
        scrollGradient.setMouseTransparent(true);
        scrollGradient.getStyleClass().add("scroll-gradient");
        scrollGradient.maxHeightProperty().bind(this.heightProperty().multiply(0.15));

        // Hide the gradient when there is no more content to scroll.
        scrollPane.vvalueProperty().addListener((_, _, newVal) ->
                scrollGradient.setVisible(newVal.doubleValue() < scrollPane.getVmax())
        );
        scrollGradient.setVisible(false);

        setAlignment(scrollPane, Pos.TOP_CENTER);
        setAlignment(scrollGradient, Pos.BOTTOM_CENTER);

        getChildren().addAll(scrollPane, scrollGradient);

        return scrollPane;
    }

    private void updateTabs(RegionSelectionModel newRegion) {
        overviewTab.updateLabels(newRegion);
        historyTab.updateLabels(newRegion);
        geographyTab.updateLabels(newRegion);
    }
}
