package faerite.model;

import java.util.Collections;
import java.util.Set;

public record RegionModel(RegionData regionData, int maskColor, Set<RegionModel> children) {
    public RegionModel {
        if (children == null) {
            children = Collections.emptySet();
        }
    }
}