package faerite.util;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;

public class PersistentToggleGroup extends ToggleGroup {

    public PersistentToggleGroup() {
        getToggles().addListener(
            (ListChangeListener<Toggle>) change -> {
                while (change.next()) {
                    for (Toggle addedToggle : change.getAddedSubList()) {
                        if (addedToggle instanceof ToggleButton button) {
                            button.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                                if (button.isSelected()) {
                                    event.consume();
                                }
                            });
                        }
                    }
                }
            }
        );
    }
}
