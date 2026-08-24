module faerite {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires org.jetbrains.annotations;
    requires java.desktop;
    requires javafx.swing;

    exports faerite;
    exports faerite.model;
    exports faerite.view;
    exports faerite.viewmodel;
}
