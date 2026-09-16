package faerite.atlas.map;

import faerite.io.AssetPaths;
import faerite.io.MapDataLoader;
import faerite.model.RegionDataModel;

import java.util.HashMap;
import java.util.Map;

public class RegionDataCache {
    private static final Map<String, RegionDataModel> cache = new HashMap<>();

    public static RegionDataModel get(String id) {
        return cache.computeIfAbsent(AssetPaths.getRegionDataPath(id), MapDataLoader::loadRegionDataModel);
    }
}
