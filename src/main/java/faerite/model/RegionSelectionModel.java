package faerite.model;

import faerite.Point;
import org.jetbrains.annotations.Nullable;

public record RegionSelectionModel(
    RegionData regionData,
    int maskColor,
    @Nullable String subMapFileName,
    @Nullable Point parentMapPointCoordinates,
    @Nullable Integer parentMapMaskColor
) implements RegionModel {}
