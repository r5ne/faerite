package mapdata;

import faerite.Point;
import faerite.model.RegionData;
import faerite.model.RegionSelectionModel;
import faerite.model.RegionType;


public class RegionBuilder {
    private String regionId;
    private final String name;
    private final RegionType type;
    private final int maskColor;
    private boolean hasSubMap = false;
    private Point parentCoordinates;
    private Integer parentMaskColor;

    public RegionBuilder(String name, RegionType type, int maskColor) {
        this.name = name;
        this.type = type;
        this.maskColor = maskColor;
    }

    public RegionBuilder regionId(String id) {
        this.regionId = id;
        return this;
    }

    public RegionBuilder subMap() {
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
        var regionData = new RegionData(name, type);
        String subMapFile = hasSubMap ? name.replace(" ", "-").toLowerCase() + ".json" : null;
        return new RegionSelectionModel(regionData, maskColor, subMapFile, parentCoordinates, parentMaskColor);

    }
}
