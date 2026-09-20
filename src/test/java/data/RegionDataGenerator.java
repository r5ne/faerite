package data;

import com.fasterxml.jackson.databind.JsonNode;
import data.datasets.BritishIslesRegionDataset;
import data.datasets.RegionDataset;
import data.regiondata.RegionDataBuilder;
import data.regiondata.RegionDataBuilderConfig;
import data.regiondata.WikidataFetcher;
import faerite.io.AssetPaths;
import faerite.io.MapDataLoader;
import faerite.model.RegionDataModel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class RegionDataGenerator {

    private RegionDataGenerator() {}

    public static void createRegionData(RegionDataSyncMode syncMode) {
        Set<RegionDataset> allDatasets = Set.of(new BritishIslesRegionDataset());

        for (RegionDataset regionDataset : allDatasets) {
            for (RegionDataBuilderConfig config : regionDataset.getRegionData()) {
                RegionDataBuilder builder;

                String stringJsonPath = DataWriter.getRelativePathOf(AssetPaths.getRegionDataPath(config.id()));
                Path jsonPath = Path.of(stringJsonPath);
                boolean configExists = Files.exists(jsonPath);

                // Loading in the existing RegionDataModel if it exists.
                if (configExists) {
                    RegionDataModel existingModel = MapDataLoader.loadRegionDataModel(config.id());
                    builder = new RegionDataBuilder(existingModel);
                } else {
                    builder = new RegionDataBuilder(config.id(), config.name());
                }

                // Filling in data returned by APIs.
                if (
                    syncMode == RegionDataSyncMode.ALL || (syncMode == RegionDataSyncMode.IF_MISSING && !configExists)
                ) {
                    JsonNode wikidata = WikidataFetcher.fetch(config.id());
                    if (wikidata != null) {
                        addWikidata(wikidata, builder);
                    } else {
                        throw new RuntimeException("Returned wikidata for " + config.name() + " was null.");
                    }
                }

                // Applying the overrides.
                if (config.overrides() != null) {
                    config.overrides().accept(builder);
                }

                DataWriter.writeData(builder.build(), jsonPath);
            }
        }
    }

    private static void addWikidata(JsonNode data, RegionDataBuilder builder) {
        String nativeNames = data.get("nativeNamesList").get("value").asText();
        if (!nativeNames.isBlank()) {
            String[] pairs = nativeNames.split("\\|");
            for (String languageNativeNamePair : pairs) {
                String[] languageNativeName = languageNativeNamePair.split(":", 2);
                builder.addNativeName(languageNativeName[0].trim(), languageNativeName[1].trim());
            }
        }
        if (data.has("area")) {
            builder.area(data.get("area").get("value").asDouble());
        }
        if (data.has("population")) {
            builder.population(data.get("population").get("value").asLong());
        }
        if (data.has("elevationQualifier")) {
            builder.highestElevation(data.get("elevationQualifier").get("value").asDouble());
        }
        if (data.has("highestPointLabel")) {
            builder.highestElevationName(data.get("highestPointLabel").get("value").asText());
        }

        System.out.println("Parsed wikidata into builder" + builder.toString());
    }
}
