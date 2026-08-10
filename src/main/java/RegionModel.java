import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

public record RegionModel(String name, String type, String maskColor, Path magnifiedImage, Set<RegionModel> children,
                          RegionBounds bounds) {
    public RegionModel {
        if (children == null) {
            children = Collections.emptySet();
        }
    }
}