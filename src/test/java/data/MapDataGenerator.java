package data;

import data.datasets.BritishIslesDataset;
import data.datasets.ChannelIslandsDataset;
import data.datasets.IsleOfManDataset;
import data.datasets.MapDataset;
import data.mapdata.ImageMetadataReader;
import faerite.io.AssetPaths;
import faerite.model.MapModel;
import java.nio.file.Path;
import java.util.List;

public class MapDataGenerator {

    private MapDataGenerator() {}

    public static void createMapModels() {
        List<MapDataset> allDatasets = List.of(
            new BritishIslesDataset(),
            new ChannelIslandsDataset(),
            new IsleOfManDataset()
        );

        for (MapDataset dataset : allDatasets) {
            String imagePath = AssetPaths.getRelativePathOf(AssetPaths.getMapImagePath(dataset.id()));
            int[] imageSize = ImageMetadataReader.getDimensions(Path.of(imagePath));

            MapModel mapModel = new MapModel(dataset.id(), imageSize[0], imageSize[1], dataset.buildRegions());

            String mapModelPath = AssetPaths.getRelativePathOf(AssetPaths.getMapDataPath(dataset.id()));
            DataWriter.writeData(mapModel, Path.of(mapModelPath));
        }
    }
}
