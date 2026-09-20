package faerite.atlas.overlay;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InfoSection extends VBox {

    public InfoSection(Node... contentNodes) {
        getStyleClass().add("info-section");
        setMinWidth(0);

        getChildren().addAll(contentNodes);
    }

    public InfoSection(String title, Node... contentNodes) {
        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("info-section-title");
        titleLabel.setWrapText(true);

        Node[] extraContentNodes = new Node[contentNodes.length + 1];
        System.arraycopy(contentNodes, 0, extraContentNodes, 1, contentNodes.length);
        extraContentNodes[0] = titleLabel;

        super(extraContentNodes);
    }

    public void setNewContent(Node... contentNodes) {
        clearContent();
        getChildren().addAll(contentNodes);
    }

    public void clearContent() {
        getChildren().subList(1, getChildren().size()).clear();
    }

    public void addContent(Node... contentNodes) {
        getChildren().addAll(contentNodes);
    }
}
