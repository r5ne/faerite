import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;

public class MapView extends Pane {
    private final MapViewModel viewModel = new MapViewModel();

    private final Group mapTiles;
    private final Scale mapScale = new Scale();

    public MapView(MapViewModel viewModel) {
        this.viewModel = viewModel;

        backgroundProperty().bind(Bindings.createObjectBinding(() -> {
                    return new Background(new BackgroundFill(viewModel.getOceanColorProperty().get(), CornerRadii.EMPTY,
                            Insets.EMPTY));
                },
                viewModel.getOceanColorProperty()
        ));

        mapTiles = assembleTiles("tile", 6, 4096);
        mapTiles.getTransforms().add(mapScale);
        getChildren().add(mapTiles);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        double paneWidth = getWidth();
        double paneHeight = getHeight();
        double mapTilesWidth = mapTiles.getBoundsInLocal().getWidth();
        double mapTilesHeight = mapTiles.getBoundsInLocal().getHeight();

        if (paneWidth <= 0 || paneHeight <= 0 || mapTilesWidth <= 0 || mapTilesHeight <= 0) {
            return;
        }

        double newScale = Math.min(paneWidth / mapTilesWidth, paneHeight / mapTilesHeight);
        mapScale.setX(newScale);
        mapScale.setY(newScale);

        double scaledWidth = mapTilesWidth * newScale;
        double scaledHeight = mapTilesHeight * newScale;
        mapTiles.setTranslateX((paneWidth - scaledWidth) / 2);
        mapTiles.setTranslateY((paneHeight - scaledHeight) / 2);
    }

    private Group assembleTiles(String tileName, int sideLength, int tileSize) {
        Group tileGroup = new Group();
        for (int x = 0; x < sideLength; x++) {
            for (int y = 0; y < sideLength; y++) {
                String path = "/" + tileName + "_" + x + "_" + y + ".png";
                Image image = new Image(getClass().getResourceAsStream(path));
                ImageView tile = new ImageView(image);

                tile.setX(x * tileSize);
                tile.setY(y * tileSize);
                tile.setSmooth(false);

                tileGroup.getChildren().add(tile);
            }
        }
        return tileGroup;
    }
}
