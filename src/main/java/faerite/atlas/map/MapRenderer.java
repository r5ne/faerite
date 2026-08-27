package faerite.atlas.map;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.*;

/// A JPanel used to render the map on.
public final class MapRenderer extends JPanel {

    private static final double BILINEAR_ZOOM_CAP = 1.5;

    private BufferedImage mapImage;
    private BufferedImage hoveredBorderImage;
    private BufferedImage selectedBorderImage;

    private double zoomFactor = 1.0;
    private final int borderSize;

    /// Creates the renderer panel.
    /// @param borderSize The width in pixels of the border that will be placed around regions on the map.
    public MapRenderer(int borderSize) {
        this.borderSize = borderSize;
        setOpaque(true);
    }

    /// Updates the content displayed on the panel.
    /// @param map The new map image to display.
    /// @param hovered The new hovered map border to display.
    /// @param selected The new selected map border to display.
    public void setImages(BufferedImage map, BufferedImage hovered, BufferedImage selected) {
        if (mapImage != map || hoveredBorderImage != hovered || selectedBorderImage != selected) {
            mapImage = map;
            hoveredBorderImage = hovered;
            selectedBorderImage = selected;
            repaint();
        }
    }

    /// Updates the zoom factor of the content displayed on the panel.
    /// @param value The new zoom factor.
    public void setZoomFactor(double value) {
        if (Double.compare(value, zoomFactor) != 0) {
            zoomFactor = value;
            repaint();
        }
    }

    /// Updates the background color displayed on the panel.
    /// @param rgb The new RGB value of the background.
    public void setBackgroundColor(int rgb) {
        Color color = new Color(rgb, false);
        if (!color.equals(getBackground())) {
            setBackground(color);
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (mapImage == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            Object interpolationValue;
            if (zoomFactor > BILINEAR_ZOOM_CAP) {
                interpolationValue = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
            } else {
                interpolationValue = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
            }
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolationValue);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            double scaledWidth = mapImage.getWidth() * zoomFactor;
            double scaledHeight = mapImage.getHeight() * zoomFactor;
            double drawX = (getWidth() - scaledWidth) / 2;
            double drawY = (getHeight() - scaledHeight) / 2;

            AffineTransform transform = new AffineTransform();
            transform.translate(drawX, drawY);
            transform.scale(zoomFactor, zoomFactor);
            g2.transform(transform);

            if (selectedBorderImage != null) {
                g2.drawImage(selectedBorderImage, -borderSize, -borderSize, null);
            }
            if (hoveredBorderImage != null) {
                g2.drawImage(hoveredBorderImage, -borderSize, -borderSize, null);
            }
            g2.drawImage(mapImage, 0, 0, null);
        } finally {
            g2.dispose();
        }
    }
}
