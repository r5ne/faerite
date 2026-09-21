package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.atlas.map.RegionDataCache;
import faerite.model.RegionDataModel;
import faerite.model.RegionSelectionModel;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HistoryTabView extends VBox {

    AtlasViewModel viewModel;

    InfoSection nativeNameSection;

    public HistoryTabView(AtlasViewModel viewModel) {
        this.viewModel = viewModel;

        nativeNameSection = new InfoSection("Native names: ");

        getChildren().addAll(nativeNameSection);
    }

    public void updateLabels(RegionSelectionModel newRegion) {
        String id = newRegion != null ? newRegion.id() : viewModel.getActiveLayer().mapModel.id();
        RegionDataModel regionData = RegionDataCache.get(id);

        updateNativeNameLabels(regionData);
    }

    private void updateNativeNameLabels(RegionDataModel regionData) {
        Map<String, String> nativeNames = regionData.nativeNames();

        if (nativeNames.isEmpty()) {
            nativeNameSection.setVisible(false);
            return;
        }

        nativeNameSection.clearContent();

        Map<String, List<String>> languagesByNativeName = nativeNames
            .entrySet()
            .stream()
            .collect(
                Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList()))
            );
        for (Map.Entry<String, List<String>> entry : languagesByNativeName.entrySet()) {
            String sharedNativeName = entry.getKey();
            List<String> languageKeys = entry.getValue();

            Label nativeNameLabel = new Label(sharedNativeName);
            nativeNameLabel.setWrapText(true);
            nativeNameLabel.getStyleClass().addAll("info-section-value");

            String combinedLanguages = String.join(", ", languageKeys);
            Label languageLabel = new Label(combinedLanguages);
            languageLabel.getStyleClass().add("info-section-description");
            languageLabel.setWrapText(true);

            nativeNameSection.addContent(nativeNameLabel, languageLabel);
        }
        nativeNameSection.setVisible(true);
    }
}
