package faerite.view;

import javafx.embed.swing.SwingNode;
import javafx.scene.layout.StackPane;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MapRendererView extends StackPane {

    private final SwingNode swingNode = new SwingNode();
    private MapRenderer renderer;

    public MapRendererView() {
        getChildren().add(swingNode);

        SwingUtilities.invokeLater(() -> {
            renderer = new MapRenderer();
            swingNode.setContent(renderer);
        });
    }

    public void setMapImage(BufferedImage mapImage) {
        SwingUtilities.invokeLater(() -> {
            if (renderer != null) {
                renderer.setMapImage(mapImage);
            }
        });
    }

    public void setZoomFactor(double zoomFactor) {
        SwingUtilities.invokeLater(() -> {
            if (renderer != null) {
                renderer.setZoomFactor(zoomFactor);
            }
        });
    }

    public void setBackgroundColor(int color) {
        SwingUtilities.invokeLater(() -> {
            if (renderer != null) {
                renderer.setBackground(new Color(color));
            }
        });
    }
}
