package faerite.model;

import org.jetbrains.annotations.Nullable;

/// Represents a selectable region within a map.
/// @param id A normalized name used as a base for all file data.
/// @param regionData The region data of the region.
/// @param maskColor The mask color of the region in the mask images associated with the map the region is part of.
/// @param parentMapPointCoordinates The coordinates of a point that should be drawn when exiting from the current
///                                  region's map into the parent map with the region selected, for when the region is
///                                  too small to be seen.
/// @param parentMapMaskColor The mask color of the region to have a border drawn when exiting from the current
///                           region's map into the parent map with the region selected.
public record RegionSelectionModel(
    String id,
    RegionData regionData,
    int maskColor,
    boolean hasSubMap,
    @Nullable Point parentMapPointCoordinates,
    @Nullable Integer parentMapMaskColor
) implements RegionModel {}
