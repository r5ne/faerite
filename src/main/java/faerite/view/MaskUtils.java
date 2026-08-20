package faerite.view;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class MaskUtils {
    private MaskUtils() {}

    public static Map<Integer, boolean[]> createBorderMasks(Image maskImage, Set<Integer> maskColors, int borderSize) {
        int width = (int) maskImage.getWidth();
        int height = (int) maskImage.getHeight();

        PixelReader reader = maskImage.getPixelReader();

        Map<Integer, boolean[]> maskBorderMap = new HashMap<>();

        for (int maskColor : maskColors) {
            boolean[] colorMask = extractColorMask(reader, maskColor, width, height);
            boolean[] borderMask = generateBorderMask(colorMask, width, height, borderSize);

            maskBorderMap.put(maskColor, borderMask);
        }
        return maskBorderMap;
    }

    private static boolean[] extractColorMask(PixelReader reader, int color, int maskWidth, int maskHeight) {
        boolean[] mask = new boolean[maskWidth * maskHeight];

        for (int y = 0; y < maskHeight; y++) {
            for (int x = 0; x < maskWidth; x++) {
                int pixelColor = reader.getArgb(x, y);
                mask[y * maskWidth + x] = (pixelColor == color);
            }
        }
        return mask;
    }

    private static boolean[] generateBorderMask(boolean[] mask, int maskWidth, int maskHeight, int borderSize) {
        // Make the generated border mask larger to allow borders to spill out of the original mask.
        int paddedWidth = maskWidth + (borderSize * 2);
        int paddedHeight = maskHeight + (borderSize * 2);
        boolean[] borderMask = new boolean[paddedWidth * paddedHeight];

        for (int y = 0; y < maskHeight; y++) {
            for (int x = 0; x < maskWidth; x++) {
                if (mask[y * maskWidth + x]) {
                    int paddedX = x + borderSize;
                    int paddedY = y + borderSize;

                    for (int dy = -borderSize; dy <= borderSize; dy++) {
                        for (int dx = -borderSize; dx <= borderSize; dx++) {
                            int originalMaskX = x + dx;
                            int originalMaskY = y + dy;

                            boolean isOutOfBounds = originalMaskX < 0 || originalMaskX >= maskWidth
                                    || originalMaskY < 0 || originalMaskY >= maskHeight;

                            if (isOutOfBounds || !mask[originalMaskY * maskWidth + originalMaskX]) {
                                borderMask[(paddedY + dy) * paddedWidth + paddedX + dx] = true;
                            }
                        }
                    }
                }
            }
        }
        return borderMask;
    }
}
