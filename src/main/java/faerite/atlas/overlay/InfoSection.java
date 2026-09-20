package faerite.atlas.overlay;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InfoSection extends VBox {

    public InfoSection(String title, Node... contentNodes) {
        getStyleClass().add("info-section");

        if (title != null) {
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("info-section-title");
            getChildren().add(titleLabel);
        }
        titleLabel.setWrapText(true);

        getChildren().addAll(contentNodes);
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
