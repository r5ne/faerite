package faerite;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Faerite extends Application {
    @Override
    public void start(Stage stage) {
        MapViewModel viewModel = new MapViewModel();
        RootView root = new RootView(viewModel);

        Rectangle2D screenRect = Screen.getPrimary().getVisualBounds();

        // Ignored on non-strict compositors where the setMaximised call ensures the window is as big as the monitor.
        // On strict compositors ensures the window gets treated as non-maximised, and is forced to the screen size.
        Scene scene = new Scene(root, screenRect.getWidth()/2, screenRect.getHeight()/2);
        stage.setScene(scene);
        stage.setTitle("faerite.Faerite");
        // Ignored on strict compositors (e.g. tiled, scrolling)
        stage.setMaximized(true);
        stage.show();
    }
}