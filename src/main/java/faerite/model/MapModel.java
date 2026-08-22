package faerite.model;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

public record MapModel(
    String name,
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

    public String imageFileName() {
        return name + ".png";
    }

    public String borderMaskFileName() {
        return name + "-bordermask.png";
    }

    public String hitboxMaskFileName() {
        return name + "-hitboxmask.png";
    }
}
