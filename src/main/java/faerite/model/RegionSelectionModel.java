package faerite.model;

import faerite.Point;
import org.jetbrains.annotations.Nullable;

/// Represents a selectable region within a map.
/// @param regionData The region data of the region.
/// @param maskColor The mask color of the region in the mask images associated with the map the region is part of.
/// @param subMapFileName The file name of a seperate, standalone map of the region if it has one.
/// @param parentMapPointCoordinates The coordinates of a point that should be drawn when exiting from the current
///                                  region's map into the parent map with the region selected, for when the region is
///                                  too small to be seen.
/// @param parentMapMaskColor The mask color of the region to have a border drawn when exiting from the current
///                           region's map into the parent map with the region selected.
public record RegionSelectionModel(
    RegionData regionData,
    int maskColor,
    @Nullable String subMapFileName,
    @Nullable Point parentMapPointCoordinates,
    @Nullable Integer parentMapMaskColor
) implements RegionModel {}
