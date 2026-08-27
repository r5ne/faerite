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
    exports faerite.atlas.map;
    exports faerite.io;
    exports faerite.atlas.overlay;
    exports faerite.util;
    exports faerite.atlas;
}
