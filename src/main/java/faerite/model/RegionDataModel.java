package faerite.model;

import java.util.List;
import org.jetbrains.annotations.Nullable;

/// Stores data of a specific region.
/// @param name The name of the region.
/// @param type The geographic type of the region.
public record RegionDataModel(
    String id,
    String name,
    RegionType type,
    @Nullable Double area,
    @Nullable Double elevation,
    @Nullable String elevationName,
    @Nullable Long population,
    List<ClimateClassification> climates,
    List<BiomeClassification> biomes
) {}
