import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

public record Region (String name, String type, String maskColor, Path magnifiedImage, Set<Region> children) {
    public Region {
        if (children == null) {
            children = Collections.emptySet();
        }
    }
}