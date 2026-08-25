package faerite.view;

import faerite.Point;
import javafx.scene.image.Image;

public final class MapGeometry {

    private MapGeometry() {}

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
