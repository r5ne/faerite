package faerite;

import faerite.model.MapDataLoader;
import faerite.model.MapModel;
import faerite.view.RootView;
import faerite.viewmodel.AtlasViewModel;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class Faerite extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = initScene("british-isles.json");
        String cssPath = getClass().getResource("/stylesheet.css").toExternalForm();
        scene.getStylesheets().add(cssPath);
        stage.setScene(scene);
        stage.setTitle("Faerite");
        Image faeriteIcon16 = new Image(Faerite.class.getResourceAsStream("/faerite-icon-16.png"));
        Image faeriteIcon32 = new Image(Faerite.class.getResourceAsStream("/faerite-icon-32.png"));
        stage.getIcons().addAll(faeriteIcon16, faeriteIcon32);
        // Ignored on strict compositors (e.g. tiled, scrolling)
        stage.setMaximized(true);
        stage.show();
    }

    private static @NotNull Scene initScene(String mapModelFileName) {
        /// @param mapModelFileName The name of the mapModel json file to load.
        /// @return The Scene with the views initialised and the mapModel loaded as the first Map in the AtlasViewModel.
        MapModel mapModel = MapDataLoader.loadMapModel(mapModelFileName);
        AtlasViewModel viewModel = new AtlasViewModel(mapModel);
        RootView root = new RootView(viewModel);

        Rectangle2D screenRect = Screen.getPrimary().getVisualBounds();

        // Screen dimensions are ignored on non-strict compositors where the setMaximised call ensures the window is
        // as big as the monitor.
        // On strict compositors ensures the window gets treated as non-maximised, and is forced to the screen size.
        return new Scene(root, screenRect.getWidth() / 2, screenRect.getHeight() / 2);
    }
}
