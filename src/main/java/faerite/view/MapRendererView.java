package faerite.view;

import javafx.embed.swing.SwingNode;
import javafx.scene.layout.StackPane;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MapRendererView extends StackPane {

    private final SwingNode swingNode = new SwingNode();

    public MapRendererView() {
        getChildren().add(swingNode);

        SwingUtilities.invokeLater(() -> {
            swingNode.setContent(new MapRenderer());
        });
    }

    public void setMapImage(BufferedImage mapImage) {
        MapRenderer mapRenderer = (MapRenderer) swingNode.getContent();
        mapRenderer.setRenderState(new MapRenderState(mapImage, null, null));
    }

    public void setZoomFactor(double zoomFactor) {
        MapRenderer mapRenderer = (MapRenderer) swingNode.getContent();
        mapRenderer.setZoomFactor(zoomFactor);
    }
}
