package faerite.atlas.overlay;

import faerite.atlas.AtlasViewModel;
import faerite.atlas.map.RegionDataCache;
import faerite.model.RegionDataModel;
import faerite.model.RegionSelectionModel;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Locale;
import java.util.Map;

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

        for (Map.Entry<String, String> nativeNameEntry : nativeNames.entrySet()) {
            Label nativeNameLabel = new Label(nativeNameEntry.getValue());
            nativeNameLabel.getStyleClass().addAll("info-section-value");

            String language = nativeNameEntry.getKey();
            java.util.Locale languageLocale = java.util.Locale.forLanguageTag(language);
            Label languageLabel = new Label(languageLocale.getDisplayLanguage(Locale.ENGLISH));
            languageLabel.getStyleClass().add("info-section-description");

            nativeNameSection.addContent(nativeNameLabel, languageLabel);
        }
        nativeNameSection.setVisible(true);
    }
}