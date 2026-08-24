package faerite.view;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public final class MapRenderer extends JPanel {

    private BufferedImage mapImage;
    private double zoomFactor = 1.0;

    public MapRenderer() {
        setOpaque(true);
    }

    public void setMapImage(BufferedImage mapImage) {
        this.mapImage = mapImage;
        repaint();
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mapImage == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();

        try {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            int scaledWidth = (int) Math.round(mapImage.getWidth() * zoomFactor);

            int scaledHeight = (int) Math.round(mapImage.getHeight() * zoomFactor);

            int drawX = (getWidth() - scaledWidth) / 2;

            int drawY = (getHeight() - scaledHeight) / 2;

            g2.drawImage(mapImage, drawX, drawY, scaledWidth, scaledHeight, null);
        } finally {
            g2.dispose();
        }
    }
}
