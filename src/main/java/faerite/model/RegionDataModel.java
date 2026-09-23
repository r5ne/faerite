package faerite.model;

import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

/// Stores data of a specific region.
/// @param name The name of the region.
/// @param type The geographic type of the region.
public record RegionDataModel(
    String id,
    String name,
    RegionType type,
    Map<String, String> nativeNames,
    @Nullable Double area,
    @Nullable Double highestElevation,
    @Nullable String highestElevationName,
    @Nullable Long population,
    Set<KoeppenClimateClassification> climates,
    Set<BiomeClassification> biomes
) {}
