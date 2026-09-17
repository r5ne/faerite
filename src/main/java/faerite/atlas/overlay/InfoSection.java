package faerite.atlas.overlay;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class InfoSection extends VBox {
    public InfoSection(InfoSectionValue... values) {
        getStyleClass().add("info-section");

        for (InfoSectionValue value : values) {
            if (value.title() != null) {
                Label titleLabel = new Label(value.title());
                titleLabel.getStyleClass().add("info-section-title");
                getChildren().add(titleLabel);
            }

            if (value.value() != null) {
                Label valueLabel = new Label(value.value());
                valueLabel.getStyleClass().add("info-section-value");
                getChildren().add(valueLabel);
            }

            if (value.description() != null) {
                Label descriptionLabel = new Label(value.description());
                descriptionLabel.getStyleClass().add("info-section-description");
                getChildren().add(descriptionLabel);
            }
        }
    }
}
