import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;

import java.util.HashMap;
import java.util.Map;

public class MapView extends Pane {
    private final MapViewModel viewModel;

    private final Group mapTiles;
    private final Map<String, ImageView> mapTileIndex = new HashMap<>();
    private final Map<String, Image> hitboxTileIndex;
    private final Scale mapScale = new Scale();

    public MapView(MapViewModel viewModel) {
        this.viewModel = viewModel;

        backgroundProperty().bind(Bindings.createObjectBinding(() -> {
                    return new Background(new BackgroundFill(viewModel.getOceanColorProperty().get(), CornerRadii.EMPTY,
                            Insets.EMPTY));
                },
                viewModel.getOceanColorProperty()
        ));

        hitboxTileIndex = assembleHitboxTiles();
        mapTiles = assembleMapTiles();
        mapTiles.getTransforms().add(mapScale);
        getChildren().add(mapTiles);

        updateVisibleMap(viewModel.getCurrentMapBoundsProperty().get());
        viewModel.getCurrentMapBoundsProperty().addListener((_, _, newBounds) -> {updateVisibleMap(newBounds);});
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        Rectangle2D mapBounds = viewModel.getCurrentMapBoundsProperty().get();
        double paneWidth = getWidth();
        double paneHeight = getHeight();

        if (paneWidth <= 0 || paneHeight <= 0 || mapBounds.getWidth() <= 0 || mapBounds.getHeight() <= 0) {
            return;
        }

        double newScale = Math.min(paneWidth / mapBounds.getWidth(), paneHeight / mapBounds.getHeight());
        mapScale.setX(newScale);
        mapScale.setY(newScale);

        double scaledWidth = mapBounds.getWidth() * newScale;
        double scaledHeight = mapBounds.getHeight() * newScale;
        mapTiles.setTranslateX((paneWidth - scaledWidth) / 2 - (mapBounds.getMinX() * newScale));
        mapTiles.setTranslateY((paneHeight - scaledHeight) / 2 - (mapBounds.getMinY() * newScale));
    }

    private void updateVisibleMap(Rectangle2D bounds) {
        for (int x = 0; x < viewModel.tileNumWidth; x++) {
            for (int y = 0; y < viewModel.tileNumHeight; y++) {
                Rectangle2D tileBounds = new Rectangle2D(x * viewModel.tileSize, y * viewModel.tileSize, viewModel.tileSize, viewModel.tileSize);
                ImageView currentTile = mapTileIndex.get(x + "_" + y);

                if (tileBounds.intersects(bounds)) {
                    if (currentTile.getImage() == null) {
                        String path = String.format("/%s_%d_%d.png", viewModel.mapTileName, x, y);
                        currentTile.setImage(new Image(path));
                    }
                }
                else {
                    currentTile.setImage(null);
                }
            }
        }
    }

    private Group assembleMapTiles() {
        Group tileGroup = new Group();
        for (int x = 0; x < viewModel.tileNumWidth; x++) {
            for (int y = 0; y < viewModel.tileNumHeight; y++) {
                String path = String.format("/%s_%d_%d.png", viewModel.mapTileName, x, y);
                Image image = new Image(getClass().getResourceAsStream(path));
                ImageView tile = new ImageView(image);

                tile.setX(x * viewModel.tileSize);
                tile.setY(y * viewModel.tileSize);
                tile.setSmooth(false);

                tileGroup.getChildren().add(tile);
                mapTileIndex.put(x + "_" + y, tile);
            }
        }
        return tileGroup;
    }

    private HashMap<String, Image> assembleHitboxTiles() {
        HashMap<String, Image> tileIndex = new HashMap<>();
        for (int x = 0; x < viewModel.tileNumWidth; x++) {
            for (int y = 0; y < viewModel.tileNumHeight; y++) {
                String path = String.format("/%s_%d_%d.png", viewModel.hitboxTileName, x, y);
                Image image = new Image(getClass().getResourceAsStream(path));
                tileIndex.put(x + "_" + y, image);
            }
        }
        return tileIndex;
    }

    private Color getHitboxColorAt(int tileNumWidth, int tileNumHeight, int x, int y) {
        Image image = hitboxTileIndex.get(tileNumWidth + "_" + tileNumHeight);
        return image.getPixelReader().getColor(x, y);
    }
}
