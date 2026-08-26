package faerite.view;

import faerite.Point;
import javafx.scene.image.Image;

/// Contains helper methods for interacting with map geometry.
public final class MapGeometry {

    private MapGeometry() {}

    /// Maps mouse coordinates relative to the window to coordinates relative to the original map image.
    /// @param windowMouseX The x position of the mouse relative to the window.
    /// @param windowMouseY The y position of the mouse relative to the window.
    /// @param sourceMapWidth The width of the original map image.
    /// @param sourceMapHeight The height of the original map image.
    /// @param mapScale The current scale of the map displayed in the window.
    /// @param windowWidth The width of the window within which the map is displayed.
    /// @param windowHeight The height of the window within which the map is displayed.
    /// @return The mouse coordinates relative to the original map image.
    public static Point screenToMapPixel(
        double windowMouseX,
        double windowMouseY,
        double sourceMapWidth,
        double sourceMapHeight,
        double mapScale,
        double windowWidth,
        double windowHeight
    ) {
        double scaledWidth = sourceMapWidth * mapScale;
        double scaledHeight = sourceMapHeight * mapScale;
        double drawX = (windowWidth - scaledWidth) / 2;
        double drawY = (windowHeight - scaledHeight) / 2;

        int pixelX = (int) (Math.floor(windowMouseX - drawX) / mapScale);
        int pixelY = (int) (Math.floor(windowMouseY - drawY) / mapScale);

        return new Point(pixelX, pixelY);
    }

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
