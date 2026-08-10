import java.util.Collections;
import java.util.Set;

public record MapModel(String tileName, String bordersTileName, String hitboxTileName, int width, int height, int tileSize, Set<RegionModel> regions) {
    public MapModel {
        if (regions == null) {
            regions = Collections.emptySet();
        }
    }

    public int tileNumWidth() {
        return Math.floorDiv(width, tileSize) + 1;
    }

    public int tileNumHeight() {
        return Math.floorDiv(height, tileSize) + 1;
    }
}
