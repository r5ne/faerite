package faerite.io;

public final class AssetPaths {
    private static final String MAP_IMAGE_FOLDER = "maps";

    private AssetPaths() {}

    public static String getMapImagePath(String mapModelId) {
        return String.format("/%s/%s.png", MAP_IMAGE_FOLDER, mapModelId);
    }

    public static String getMapHitboxMaskPath(String mapModelId) {
        return String.format("/%s/%s-hitboxmask-0.png", MAP_IMAGE_FOLDER, mapModelId);
    }

    public static String getMapHitboxMaskPath(String mapModelId, int level) {
        return String.format("/%s/%s-hitboxmask-%d.png", MAP_IMAGE_FOLDER, mapModelId, level);
    }

    public static String getMapBorderMaskPath(String mapModelId) {
        return String.format("/%s/%s-bordermask-0.png", MAP_IMAGE_FOLDER, mapModelId);
    }

    public static String getMapBorderMaskPath(String mapModelId, int level) {
        return String.format("/%s/%s-bordermask-%d.png", MAP_IMAGE_FOLDER,mapModelId, level);
    }

    public static String getMapDataPath(String id) {
        return "/mapdata/" + id + ".json";
    }

    public static String getRegionDataPath(String id) {
        return "/regiondata/" + id + ".json";
    }

    public static String getMarkdown(String regionSelectionModelId, String section) {
        return String.format("/regiondata/markdown/%s-%s.md", regionSelectionModelId, section);
    }
}
