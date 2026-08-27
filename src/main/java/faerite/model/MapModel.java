package faerite.model;

import java.util.Collections;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

/// Represents a standalone map and its regions.
/// @param id A normalized name used as a base for all file data.
/// @param width The width of the map's image.
/// @param height The height of the map's image.
/// @param regionData The region data of the overall region displayed by the map.
/// @param regions A set of all the regions contained within the map.
public record MapModel(
    String id,
    int width,
    int height,
    RegionData regionData,
    @Nullable Set<RegionSelectionModel> regions
) implements RegionModel {
    public MapModel {
        if (regions == null) {
            regions = Collections.emptySet();
        }
    }

    /// Returns the full image file name associated with the map.
    public String imageFileName() {
        return id + ".png";
    }

    /// Returns the full border mask image file name associated with the map.
    public String borderMaskFileName() {
        return id + "-bordermask.png";
    }

    /// Returns the full hitbox mask image file name associated with the map.
    public String hitboxMaskFileName() {
        return id + "-hitboxmask.png";
    }

    @Override
    public String markdownFileName(String section) {
        return String.format("%s-%s.md", id, section);
    }
}
