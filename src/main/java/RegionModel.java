import java.util.Collections;
import java.util.Set;

public record RegionModel(String name, RegionType type, int maskARGBColor, Set<RegionModel> children) {
    public RegionModel {
        if (children == null) {
            children = Collections.emptySet();
        }
    }
}