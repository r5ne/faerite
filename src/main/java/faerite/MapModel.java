package faerite;

import java.util.Collections;
import java.util.Set;

public record MapModel(String fileName, String name, int width, int height, RegionType type, Set<RegionModel> regions) {
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
