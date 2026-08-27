package faerite.atlas.overlay;

import faerite.util.MarkdownRenderer;
import javafx.scene.layout.VBox;

public class MarkdownView extends VBox {
    private final MarkdownRenderer renderer = new MarkdownRenderer();

    public void setMarkdown(String markdown) {
        getChildren().setAll(renderer.render(markdown));
    }
}
