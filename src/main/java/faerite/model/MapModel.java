package faerite.model;

import java.util.Collections;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

/// Represents a standalone map and its regions.
/// @param id A normalized name used as a base for all file data.
/// @param width The width of the map's image.
/// @param height The height of the map's image.
/// @param regions A set of all the regions contained within the map.
public record MapModel(String id, int width, int height, Set<RegionSelectionModel> regions) {}
