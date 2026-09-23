package data;

import data.datasets.BritishIslesDataset;
import data.datasets.ChannelIslandsDataset;
import data.datasets.IsleOfManDataset;
import data.datasets.MapDataset;
import data.mapdata.ImageMetadataReader;
import data.regiondata.RegionHierarchyTree;
import faerite.io.AssetPaths;
import faerite.model.MapModel;
import faerite.model.RegionSelectionModel;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class MapDataGenerator {

    private final RegionHierarchyTree regionHierarchyTree = new RegionHierarchyTree();

    public MapDataGenerator() {
        List<MapDataset> allDatasets = List.of(
            new BritishIslesDataset(),
            new ChannelIslandsDataset(),
            new IsleOfManDataset()
        );

        for (MapDataset dataset : allDatasets) {
            String imagePath = DataWriter.getRelativePathOf(AssetPaths.getMapImagePath(dataset.id()));
            int[] imageSize = ImageMetadataReader.getDimensions(Path.of(imagePath));

            String parentMapModelId = dataset.id();
            Set<RegionSelectionModel> childRegions = dataset.buildRegions();
            for (RegionSelectionModel region : childRegions) {
                regionHierarchyTree.addRegionRelationship(parentMapModelId, region.id());
            }
            MapModel mapModel = new MapModel(dataset.id(), imageSize[0], imageSize[1], dataset.buildRegions());

            String mapModelPath = DataWriter.getRelativePathOf(AssetPaths.getMapDataPath(dataset.id()));
            DataWriter.writeData(mapModel, Path.of(mapModelPath));
        }
    }

    public RegionHierarchyTree getRegionHierarchyTree() {
        return regionHierarchyTree;
    }
}
