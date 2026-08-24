package faerite.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public final class MapRenderer extends JPanel {
    private BufferedImage mapImage;

    private double zoomFactor;

    public void setMapImage(BufferedImage mapImage) {
        this.mapImage = mapImage;
        repaint();
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        int scaledWidth = (int) (mapImage.getWidth() * zoomFactor);
        int scaledHeight = (int) (mapImage.getHeight() * zoomFactor);

        int drawX = (getWidth() - scaledWidth) / 2;
        int drawY = (getHeight() - scaledHeight) / 2;

        g2D.drawImage(mapImage, drawX, drawY, scaledWidth, scaledHeight, null);
    }
}
