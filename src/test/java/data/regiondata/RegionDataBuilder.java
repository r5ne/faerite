package data.regiondata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import faerite.model.BiomeClassification;
import faerite.model.ClimateClassification;
import faerite.model.RegionDataModel;
import faerite.model.RegionType;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegionDataBuilder {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String name;
    private final RegionType type;
    private String id;
    private String wikidataId;
    private String nativeName;
    private Double area;
    private Double elevation;
    private String elevationName;
    private Long population;
    private List<ClimateClassification> climates = new ArrayList<>();
    private List<BiomeClassification> biomes = new ArrayList<>();

    public RegionDataBuilder(String name, RegionType type) {
        this.name = name;
        this.type = type;
    }

    public RegionDataBuilder id(String id) {
        this.id = id;
        return this;
    }

    public RegionDataBuilder wikidataId(String wikidataId) {
        this.wikidataId = wikidataId;
        return this;
    }

    public RegionDataBuilder nativeName(String nativeName) {
        this.nativeName = nativeName;
        return this;
    }

    public RegionDataBuilder area(double area) {
        this.area = area;
        return this;
    }

    public RegionDataBuilder elevation(double elevation) {
        this.elevation = elevation;
        return this;
    }

    public RegionDataBuilder elevationName(String elevationName) {
        this.elevationName = elevationName;
        return this;
    }

    public RegionDataBuilder population(long population) {
        this.population = population;
        return this;
    }

    public RegionDataBuilder climates(ClimateClassification... climateClassification) {
        this.climates = Arrays.asList(climateClassification);
        return this;
    }

    public RegionDataBuilder addClimates(ClimateClassification... climateClassification) {
        this.climates.addAll(Arrays.asList(climateClassification));
        return this;
    }


    public RegionDataBuilder biomes(BiomeClassification... biomeClassifications) {
        this.biomes = Arrays.asList(biomeClassifications);
        return this;
    }

    public RegionDataBuilder addBiomes(BiomeClassification... biomeClassifications) {
        this.biomes.addAll(Arrays.asList(biomeClassifications));
        return this;
    }

    public RegionDataModel build() {
        id = id != null ? id : name.toLowerCase().replace(" ", "-");
        if (wikidataId != null) {
            HttpResponse<String> response = WikidataFetcher.fetch(wikidataId);
            System.out.println("Got response: " + response.body() + "Status code: " + response.statusCode());
            if (response != null) {
                if (response.statusCode() != 200) {
                    System.err.println("API Error: " + response.body());
                } else {
                    try {
                        JsonNode root = objectMapper.readTree(response.body());
                        JsonNode bindings = root.path("results").path("bindings");

                        if (bindings.isArray() && !bindings.isEmpty()) {
                            JsonNode data = bindings.get(0);

                            if (data.has("area")) {
                                area = data.get("area").get("value").asDouble();
                            }
                            System.out.println("Parsed area");
                            if (data.has("population")) {
                                population = data.get("population").get("value").asLong();
                            }
                            System.out.println("Parsed population");
                        }
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return new RegionDataModel(id, name, type, nativeName, area, elevation, elevationName, population, climates, biomes);
    }
}