import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;

public class MapView extends Application {
    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        stage.setTitle("Faerite");
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }
}
