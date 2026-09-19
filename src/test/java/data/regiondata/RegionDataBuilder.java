package data.regiondata;

import com.fasterxml.jackson.databind.ObjectMapper;
import faerite.model.BiomeClassification;
import faerite.model.ClimateClassification;
import faerite.model.RegionDataModel;
import faerite.model.RegionType;
import java.util.*;

public class RegionDataBuilder {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final String id;
    private final String name;
    private RegionType type;
    private Map<String, String> nativeNames;
    private Double area;
    private Double elevation;
    private String elevationName;
    private Long population;
    private Set<ClimateClassification> climates;
    private Set<BiomeClassification> biomes;

    public RegionDataBuilder(String id, String name) {
        this.id = id;
        this.name = name;

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

    public RegionDataBuilder type(RegionType type) {
        this.type = type;
        return this;
    }

    public RegionDataBuilder nativeNames(Map<String, String> nativeNames) {
        this.nativeNames = nativeNames;
        return this;
    }

    public RegionDataBuilder addNativeName(String language, String name) {
        this.nativeNames.put(language, name);
        return this;
    }

    public RegionDataBuilder addAllNativeNames(String... nativeNameEntries) {
        if (nativeNameEntries.length % 2 != 0) {
            throw new IllegalArgumentException("Keys and values must be paired up evenly.");
        }

        for (int i = 0; i < nativeNameEntries.length; i += 2) {
            nativeNames.put(nativeNameEntries[i], nativeNameEntries[i + 1]);
        }
        return this;
    }

    public RegionDataBuilder keepNativeNames(String... languages) {
        nativeNames.keySet().retainAll(Set.of(languages));
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
        this.climates = new HashSet<>(Arrays.asList(climateClassification));
        return this;
    }

    public RegionDataBuilder addClimates(ClimateClassification... climateClassification) {
        this.climates.addAll(Arrays.asList(climateClassification));
        return this;
    }

    public RegionDataBuilder biomes(BiomeClassification... biomeClassifications) {
        this.biomes = new HashSet<>(Arrays.asList(biomeClassifications));
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
