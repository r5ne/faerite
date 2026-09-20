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
import faerite.model.RegionType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class RegionDataGenerator {

    private static final String QUERY_TEMPLATE = """
    SELECT
      ?typeLabel
      ?area
      ?population
      ?coordinates
      ?elevation
      ?elevationLabel
      ?elevationValue
      (GROUP_CONCAT(CONCAT(LANG(?nativeName), ":", STR(?nativeName)); SEPARATOR = "|") AS ?nativeNamesList)
    WHERE {
      BIND(wd:%s AS ?region)
      OPTIONAL { ?region wdt:P31 ?type . }
      OPTIONAL { ?region wdt:P2046 ?area . }
      OPTIONAL { ?region wdt:P1082 ?population . }
      OPTIONAL { ?region wdt:P625 ?coordinates . }
      OPTIONAL { ?region wdt:P1705 ?nativeName . }

      OPTIONAL {
        ?region wdt:P610 ?elevation.
        ?elevation wdt:P2044 ?elevationValue.
      }
      SERVICE wikibase:label { bd:serviceParam wikibase:language "en". }
    }
    GROUP BY ?typeLabel ?area ?population ?coordinates ?elevation ?elevationValue ?elevationLabel
    LIMIT 1
    """;

    private static final Map<String, RegionType> REGION_TYPE_MAP = Map.of(
        "archipelago",
        RegionType.ARCHIPELAGO,
        "island group",
        RegionType.ISLAND_GROUP,
        "island",
        RegionType.ISLAND,
        "rock",
        RegionType.ROCK_GROUP
    );

    private RegionDataGenerator() {}

    public static void createRegionData(RegionDataSyncMode syncMode) {
        Set<RegionDataset> allDatasets = Set.of(new BritishIslesRegionDataset());

        for (RegionDataset regionDataset : allDatasets) {
            for (RegionDataBuilderConfig config : regionDataset.getRegionData()) {
                RegionDataBuilder builder;

                String resourcePath = AssetPaths.getRegionDataPath(config.id());
                Path relativePath = Path.of(DataWriter.getRelativePathOf(resourcePath));
                boolean configExists = Files.exists(relativePath);

                // Loading in the existing RegionDataModel if it exists.
                if (configExists) {
                    RegionDataModel existingModel = MapDataLoader.loadRegionDataModel(resourcePath);
                    builder = new RegionDataBuilder(existingModel);
                } else {
                    builder = new RegionDataBuilder(config.id(), config.name());
                }

                // Filling in data returned by APIs.
                if (
                    syncMode == RegionDataSyncMode.ALL || (syncMode == RegionDataSyncMode.IF_MISSING && !configExists)
                ) {
                    JsonNode wikidata = WikidataFetcher.fetch(String.format(QUERY_TEMPLATE, config.wikidataId()));
                    if (wikidata != null) {
                        addWikidata(wikidata, builder);
                    } else {
                        System.err.printf(
                            "Returned wikidata for %s (wikidata=%s) was null. Aborting object creation.%n",
                            config.name(),
                            config.wikidataId()
                        );
                        continue;
                    }
                }

                // Applying the overrides.
                if (config.overrides() != null) {
                    config.overrides().accept(builder);
                }

                DataWriter.writeData(builder.build(), relativePath);
            }
        }
    }

    private static void addWikidata(JsonNode data, RegionDataBuilder builder) {
        System.out.println(data.toPrettyString());
        if (data.has("typeLabel")) {
            RegionType type = REGION_TYPE_MAP.get(data.get("typeLabel").get("value").asText());
            builder.type(type);
        }
        if (data.has("area")) {
            builder.area(data.get("area").get("value").asDouble());
        }
        if (data.has("population")) {
            builder.population(data.get("population").get("value").asLong());
        }
        if (data.has("elevationValue")) {
            builder.highestElevation(data.get("elevationValue").get("value").asDouble());
        }
        if (data.has("elevationLabel")) {
            builder.highestElevationName(data.get("elevationLabel").get("value").asText());
        }
        if (data.has("nativeNamesList")) {
            String nativeNames = data.get("nativeNamesList").get("value").asText();
            if (!nativeNames.isBlank()) {
                String[] pairs = nativeNames.split("\\|");
                for (String languageNativeNamePair : pairs) {
                    String[] languageNativeName = languageNativeNamePair.split(":", 2);

                    String languageCode = languageNativeName[0].trim();
                    System.out.println(languageCode);
                    if (languageCode.equals("en")) {
                        continue;
                    }
                    java.util.Locale languageLocale = java.util.Locale.forLanguageTag(languageCode);
                    String language = languageLocale.getDisplayLanguage(Locale.ENGLISH);

                    builder.addNativeName(language.isEmpty() ? languageCode : language, languageNativeName[1].trim());
                }
            }
        }

        System.out.println("Parsed wikidata into builder " + builder.toString());
    }
}
