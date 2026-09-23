package data.mapdata;

import faerite.model.Point;
import faerite.model.RegionSelectionModel;

public class RegionBuilder {

    private final String id;
    private final int maskColor;
    private boolean hasSubMap = false;
    private Point parentCoordinates;
    private Integer parentMaskColor;

    public RegionBuilder(String regionDataId, int maskColor) {
        this.id = regionDataId;
        this.maskColor = maskColor;
    }

    public RegionBuilder hasSubMap() {
        this.hasSubMap = true;
        return this;
    }

    public RegionBuilder parentCoordinates(int x, int y) {
        this.parentCoordinates = new Point(x, y);
        return this;
    }

    public RegionBuilder parentMaskColor(int color) {
        this.parentMaskColor = color;
        return this;
    }

    public RegionSelectionModel build() {
        return new RegionSelectionModel(id, maskColor, hasSubMap, parentCoordinates, parentMaskColor);
    }
}
