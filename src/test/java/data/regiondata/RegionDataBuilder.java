package data.regiondata;

import com.fasterxml.jackson.databind.ObjectMapper;
import faerite.model.BiomeClassification;
import faerite.model.ClimateClassification;
import faerite.model.RegionDataModel;
import faerite.model.RegionType;
import java.util.*;

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
        this.id = name.toLowerCase().replace(" ", "-");

        this.nativeNames = new HashMap<>();
        this.climates = new HashSet<>();
        this.biomes = new HashSet<>();
    }

    public RegionDataBuilder(RegionDataModel existingModel) {
        this.id = existingModel.id();
        this.name = existingModel.name();
        this.type = existingModel.type();
        this.nativeNames = existingModel.nativeNames();
        this.area = existingModel.area();
        this.elevation = existingModel.elevation();
        this.elevationName = existingModel.elevationName();
        this.population = existingModel.population();
        this.climates = existingModel.climates();
        this.biomes = existingModel.biomes();
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
        return new RegionDataModel(
            id,
            name,
            type,
            nativeNames,
            area,
            elevation,
            elevationName,
            population,
            climates,
            biomes
        );
    }
}
