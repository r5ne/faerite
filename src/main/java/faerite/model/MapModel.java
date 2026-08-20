package faerite.model;

import java.util.Collections;
import java.util.Set;

public record MapModel(
    String fileName,
    int width,
    int height,
    RegionData regionData,
    Set<RegionSelectionModel> regions
) implements RegionModel {
    public MapModel {
        if (regions == null) {
            regions = Collections.emptySet();
        }
    }

    public String borderMaskFileName() {
        return fileName + "-bordermask";
    }

    public String hitboxMaskFileName() {
        return fileName + "-hitboxmask";
    }
}
