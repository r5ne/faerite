package faerite.atlas.overlay;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InfoSection extends VBox {

    public InfoSection(String title, Node... contentNodes) {
        getStyleClass().add("info-section");

        if (title != null) {
            Label titleLabel = new Label(title);
            titleLabel.getStyleClass().add("card-title");
            getChildren().add(titleLabel);
        }

        getChildren().addAll(contentNodes);
    }
}
