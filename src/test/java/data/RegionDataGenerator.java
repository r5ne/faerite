package data;

import com.fasterxml.jackson.databind.JsonNode;
import data.datasets.BritishIslesRegionDataset;
import data.datasets.RegionDataset;
import data.regiondata.ApiFetcher;
import data.regiondata.RegionDataBuilder;
import data.regiondata.RegionDataBuilderConfig;
import data.regiondata.RegionHierarchyTree;
import faerite.io.AssetPaths;
import faerite.io.MapDataLoader;
import faerite.model.KoeppenClimateClassification;
import faerite.model.RegionDataModel;
import faerite.model.RegionType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private static final Pattern WKT_POINT_PATTERN = Pattern.compile(
        "Point\\(\\s*([+-]?\\d*\\.?\\d+)\\s+([+-]?\\d*\\.?\\d+)\\s*\\)"
    );

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

    public static void createRegionData(DataSyncMode syncMode, RegionHierarchyTree regionHierarchyTree) {
        Set<RegionDataset> allDatasets = Set.of(new BritishIslesRegionDataset());
        Map<String, RegionDataBuilderConfig> regionIdBuilderConfigMap = new HashMap<>();
        for (RegionDataset dataset : allDatasets) {
            for (RegionDataBuilderConfig config : dataset.getRegionData()) {
                regionIdBuilderConfigMap.put(config.id(), config);
            }
        }

        List<String> processingOrder = regionHierarchyTree.bottomUpTraversal();
        Map<String, RegionDataModel> memoryCache = new HashMap<>();

        for (String regionId : processingOrder) {
            RegionDataBuilderConfig config = regionIdBuilderConfigMap.get(regionId);

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
            if (syncMode == DataSyncMode.ALL || (syncMode == DataSyncMode.IF_MISSING && !configExists)) {
                JsonNode wikidata = ApiFetcher.fetchWikidata(String.format(QUERY_TEMPLATE, config.wikidataId()));
                if (wikidata != null) {
                    addWikidata(wikidata, builder);

                    if (wikidata.has("coordinates")) {
                        String wktString = wikidata.path("coordinates").path("value").asText();
                        Matcher matcher = WKT_POINT_PATTERN.matcher(wktString);

                        if (matcher.find()) {
                            // Group 1 is Longitude, Group 2 is Latitude
                            double lon = Double.parseDouble(matcher.group(1));
                            double lat = Double.parseDouble(matcher.group(2));

                            JsonNode climateData = ApiFetcher.fetchClimateData(lat, lon);
                            addMapressoClimate(climateData, builder);
                        }
                    }
                } else {
                    System.err.printf(
                        "Returned wikidata for %s (wikidata=%s) was null. Aborting object creation.%n",
                        config.name(),
                        config.wikidataId()
                    );
                    continue;
                }
            }

            // Not a leaf, so aggregate data from children
            if (!regionHierarchyTree.isLeaf(regionId)) {
                for (String childRegionId : regionHierarchyTree.getChildren(regionId)) {
                    RegionDataModel childRegionData = memoryCache.get(childRegionId);
                    if (childRegionData != null) {
                        aggregateFromChild(builder, childRegionData);
                    }
                }
            }

            // Applying the overrides.
            if (config.overrides() != null) {
                config.overrides().accept(builder);
            }

            DataWriter.writeData(builder.build(), relativePath);
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

    private static void addMapressoClimate(JsonNode data, RegionDataBuilder builder) {
        if (data == null || !data.isArray()) return;

        for (JsonNode entry : data) {
            String type = entry.path("type").asText();

            if ("Köppen-Geiger".equals(type)) {
                String codeStr = entry.path("code").asText();
                System.out.println("Koppen code: " + codeStr);

                try {
                    KoeppenClimateClassification code = KoeppenClimateClassification.valueOf(codeStr.toUpperCase());
                    builder.addClimates(code);
                } catch (IllegalArgumentException e) {
                    System.err.println("Unrecognized climate code returned by Mapresso: " + codeStr);
                }
                break;
            }
        }
    }

    private static void aggregateFromChild(RegionDataBuilder builder, RegionDataModel childData) {
        builder.addClimates(childData.climates());
    }
}
