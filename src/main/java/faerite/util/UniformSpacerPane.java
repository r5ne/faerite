package faerite.util;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.util.List;

public class UniformSpacerPane extends Region {

    @Override
    protected void layoutChildren() {
        double width = getWidth();
        double height = getHeight();
        List<Node> children = getManagedChildren();

        if (children.isEmpty()) return;

        double maxButtonWidth = 0;
        for (Node child : children) {
            maxButtonWidth = Math.max(maxButtonWidth, child.prefWidth(-1));
        }

        double totalButtonWidth = maxButtonWidth * children.size();
        double availableSpace = width - totalButtonWidth;
        double spacing = availableSpace / (children.size() + 1);

        spacing = Math.max(0, spacing);

        double x = spacing;
        for (Node child : children) {
            double btnHeight = child.prefHeight(-1);

            double y = (height - btnHeight) / 2;

            child.resize(maxButtonWidth, btnHeight);
            child.relocate(x, y);

            x += maxButtonWidth + spacing;
        }
    }

    @Override
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }
}