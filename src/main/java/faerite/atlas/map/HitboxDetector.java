package faerite.atlas.map;

import javafx.scene.image.Image;

public final class HitboxDetector {
    private HitboxDetector() {}

    /// Gets the color of a pixel on a map at a specific point.
    /// @param mapHitbox The map to check.
    /// @param mapX The x coordinate on the map to check.
    /// @param mapY The y coordinate on the map to check.
    /// @return The ARGB color of the pixel.
    public static int getColorAtPoint(Image mapHitbox, int mapX, int mapY) {
        int color;
        if (mapX >= 0 && mapX < mapHitbox.getWidth() && mapY >= 0 && mapY < mapHitbox.getHeight()) {
            color = mapHitbox.getPixelReader().getArgb(mapX, mapY);
        } else {
            color = 0;
        }
        return color;
    }
}
