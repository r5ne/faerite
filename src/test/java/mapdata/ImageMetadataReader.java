package mapdata;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class ImageMetadataReader {

    public static int[] getDimensions(Path imagePath) {
        if (!Files.exists(imagePath)) {
            throw new IllegalArgumentException("Image file not found on filesystem: " + imagePath.toAbsolutePath());
        }
        try (
            InputStream stream = Files.newInputStream(imagePath);
            ImageInputStream input = ImageIO.createImageInputStream(stream)
        ) {
            ImageReader reader = ImageIO.getImageReaders(input).next();
            try {
                reader.setInput(input);
                return new int[] { reader.getWidth(0), reader.getHeight(0) };
            } finally {
                reader.dispose();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read image dimensions: " + imagePath, e);
        }
    }
}
