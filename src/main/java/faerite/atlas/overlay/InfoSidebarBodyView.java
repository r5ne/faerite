package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.model.RegionInfoSection;
import faerite.model.RegionSelectionModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/// Contains the information UI for the selected region.
public class InfoSidebarBodyView extends StackPane {

    private final AtlasViewModel viewModel;

    private final StackPane viewStack = new StackPane();
    private final OverviewTabView overviewTab;
    private final HistoryTabView historyTab;
    private final GeographyTabView geographyTab;

    /// Creates the information UI using data from the view model.
    /// @param viewModel The global view model instance.
    public InfoSidebarBodyView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        // Everything in the body of the info sidebar is scrollable.
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        // Stack to allow switching between tab contents.
        StackPane tabStack = new StackPane();
        tabStack.getStyleClass().add("info-sidebar-body");

        overviewTab = new OverviewTabView(viewModel);
        historyTab = new HistoryTabView(viewModel);
        geographyTab = new GeographyTabView(viewModel);

        overviewTab.managedProperty().bind(overviewTab.visibleProperty());
        historyTab.managedProperty().bind(historyTab.visibleProperty());
        geographyTab.managedProperty().bind(geographyTab.visibleProperty());

        viewModel.selectedRegionProperty().addListener((_, _, newRegion) -> updateTabs(newRegion));

        historyTab.setVisible(false);
        geographyTab.setVisible(false);

        viewModel.selectedInfoSectionProperty().addListener((_, _, newTab) -> {
            overviewTab.setVisible(newTab == RegionInfoSection.OVERVIEW);
            historyTab.setVisible(newTab == RegionInfoSection.HISTORY);
            geographyTab.setVisible(newTab == RegionInfoSection.GEOGRAPHY);
        });

        tabStack.getChildren().addAll(overviewTab, historyTab, geographyTab);

        scrollPane.setContent(tabStack);

        // Gradient overlay at the bottom to indicate you can scroll down
        Rectangle scrollGradient = new Rectangle();
        scrollGradient.widthProperty().bind(this.widthProperty());
        scrollGradient.heightProperty().bind(this.heightProperty().multiply(0.15));
        scrollGradient.getStyleClass().add("scroll-gradient");

        getChildren().addAll(scrollPane, scrollGradient);

        getChildren().add(viewStack);
    }

    private void updateTabs(RegionSelectionModel newRegion) {
        overviewTab.updateLabels(newRegion);
    }
}
