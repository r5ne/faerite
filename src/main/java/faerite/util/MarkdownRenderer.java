package faerite.util;

import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;

import java.util.ArrayDeque;
import java.util.Deque;

public class MarkdownRenderer extends AbstractVisitor {

    private final VBox container = new VBox(8.0);
    private TextFlow currentTextFlow;
    private final Deque<String> styleStack = new ArrayDeque<>();

    private int listIndex = 1;

    public VBox render(String markdownText) {
        container.getChildren().clear();
        container.getStyleClass().add("markdown-view");

        if (markdownText == null || markdownText.isBlank()) return container;

        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownText);
        document.accept(this);

        return container;
    }

    @Override
    public void visit(Heading heading) {
        Label label = new Label();
        label.getStyleClass().add("markdown-h" + heading.getLevel());

        StringBuilder sb = new StringBuilder();
        Node child = heading.getFirstChild();
        while (child != null) {
            if (child instanceof org.commonmark.node.Text t) sb.append(t.getLiteral());
            child = child.getNext();
        }
        label.setText(sb.toString());
        container.getChildren().add(label);
    }

    @Override
    public void visit(Paragraph paragraph) {
        currentTextFlow = new TextFlow();
        visitChildren(paragraph);
        container.getChildren().add(currentTextFlow);
        currentTextFlow = null;
    }

    @Override
    public void visit(HardLineBreak hardLineBreak) {
        if (currentTextFlow != null) {
            currentTextFlow.getChildren().add(new javafx.scene.text.Text("\n"));
        }
    }

    @Override
    public void visit(OrderedList orderedList) {
        listIndex = orderedList.getMarkerStartNumber();
        visitChildren(orderedList);
    }

    @Override
    public void visit(ListItem listItem) {
        HBox listRow = new HBox(5.0);
        listRow.getStyleClass().add("markdown-list-item");

        Label bullet = new Label(listItem.getParent() instanceof OrderedList ? (listIndex++) + "." : "•");
        bullet.getStyleClass().add("markdown-text");

        VBox itemContent = new VBox();

        VBox previousContainer = new VBox();
        previousContainer.getChildren().addAll(container.getChildren());
        container.getChildren().clear();

        visitChildren(listItem);

        itemContent.getChildren().addAll(container.getChildren());

        container.getChildren().clear();
        container.getChildren().addAll(previousContainer.getChildren());

        listRow.getChildren().addAll(bullet, itemContent);
        container.getChildren().add(listRow);
    }

    @Override
    public void visit(StrongEmphasis strongEmphasis) {
        styleStack.push("-fx-font-weight: bold; ");
        visitChildren(strongEmphasis);
        styleStack.pop();
    }

    @Override
    public void visit(Emphasis emphasis) {
        styleStack.push("-fx-font-style: italic; ");
        visitChildren(emphasis);
        styleStack.pop();
    }

    @Override
    public void visit(org.commonmark.node.Text text) {
        if (currentTextFlow != null) {
            var fxText = new javafx.scene.text.Text(text.getLiteral());
            fxText.getStyleClass().add("markdown-text"); // Attach CSS class
            fxText.setStyle(String.join("", styleStack)); // Attach bold/italic
            currentTextFlow.getChildren().add(fxText);
        }
    }
}
