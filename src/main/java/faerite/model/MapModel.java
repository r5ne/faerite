package faerite.model;

import java.util.Collections;
import java.util.Set;
import org.jetbrains.annotations.Nullable;

public record MapModel(
    String fileName,
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
        return fileName + ".png";
    }

    public String borderMaskFileName() {
        return fileName + "-bordermask.png";
    }

    public String hitboxMaskFileName() {
        return fileName + "-hitboxmask.png";
    }
}
