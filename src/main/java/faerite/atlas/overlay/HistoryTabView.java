package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.atlas.map.RegionDataCache;
import faerite.model.RegionDataModel;
import faerite.model.RegionSelectionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        Map<String, List<String>> languagesByNativeName = new HashMap<>();

        for (Map.Entry<String, String> entry : nativeNames.entrySet()) {
            languagesByNativeName
                    .computeIfAbsent(entry.getValue(), k -> new ArrayList<>())
                    .add(entry.getKey());
        }

        for (Map.Entry<String, List<String>> entry : languagesByNativeName.entrySet()) {
            Label nativeNameLabel = new Label(entry.getKey());
            nativeNameLabel.setWrapText(true);
            nativeNameLabel.getStyleClass().addAll("info-section-value");

            String combinedLanguages = String.join(", ", entry.getValue());
            Label languageLabel = new Label(combinedLanguages);
            languageLabel.getStyleClass().add("info-section-description");
            languageLabel.setWrapText(true);

            nativeNameSection.addContent(nativeNameLabel, languageLabel);
        }

        nativeNameSection.setVisible(true);
    }
}
