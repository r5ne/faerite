package faerite.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;

/// Contains utilities for creating borders.
public final class BorderGenerator {

    private BorderGenerator() {}

    /// Creates a map of sparse indices for the borders from the mask image.
    /// @param maskImage The mask image to generate the borders from.
    /// @param maskColors The set of all colors to check for and generate borders from.
    /// @param borderSize The size of the border to generate.
    /// @return A map of sparse indices for the borders from the mask image.
    public static Map<Integer, int[]> createBorderMasks(Image maskImage, Set<Integer> maskColors, int borderSize) {
        int width = (int) maskImage.getWidth();
        int height = (int) maskImage.getHeight();

        PixelReader reader = maskImage.getPixelReader();

        Map<Integer, int[]> maskBorderMap = new HashMap<>();

        for (int maskColor : maskColors) {
            boolean[] colorMask = extractColorMask(reader, maskColor, width, height);
            boolean[] borderMask = generateBorderMask(colorMask, width, height, borderSize);

            int[] borderMaskSparseIndices = maskToSparseIndices(borderMask);
            maskBorderMap.put(maskColor, borderMaskSparseIndices);
        }
        return maskBorderMap;
    }

    private static int[] maskToSparseIndices(boolean[] mask) {
        int totalSetPixels = 0;
        for (boolean isPixelSet : mask) {
            if (isPixelSet) totalSetPixels++;
        }

        int[] indices = new int[totalSetPixels];
        int currentIndex = 0;
        for (int i = 0; i < mask.length; i++) {
            if (mask[i]) {
                indices[currentIndex++] = i;
            }
        }
        return indices;
    }

    private static boolean[] extractColorMask(PixelReader reader, int color, int maskWidth, int maskHeight) {
        boolean[] mask = new boolean[maskWidth * maskHeight];

        for (int y = 0; y < maskHeight; y++) {
            for (int x = 0; x < maskWidth; x++) {
                int pixelColor = reader.getArgb(x, y);
                mask[y * maskWidth + x] = pixelColor == color;
            }
        }
        return mask;
    }

    private static boolean[] generateBorderMask(boolean[] mask, int maskWidth, int maskHeight, int borderSize) {
        // Make the generated border mask larger to allow borders to spill out of the original mask.
        int paddedWidth = maskWidth + borderSize * 2;
        int paddedHeight = maskHeight + borderSize * 2;
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

                            boolean isOutOfBounds =
                                originalMaskX < 0 ||
                                originalMaskX >= maskWidth ||
                                originalMaskY < 0 ||
                                originalMaskY >= maskHeight;

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
