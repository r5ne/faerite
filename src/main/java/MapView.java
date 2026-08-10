import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class MapView extends Pane {
    private final MapViewModel viewModel;

    private final Group mapTiles;
    private final Map<TileCoordinate, ImageView> mapTileIndex = new HashMap<>();
    private final Map<TileCoordinate, Image> hitboxTileIndex;
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

        this.setOnMouseMoved(event -> {
            double screenX = event.getX();
            double screenY = event.getY();

            double mapX = (screenX - mapTiles.getTranslateX()) / mapScale.getX();
            double mapY = (screenY - mapTiles.getTranslateY()) / mapScale.getY();

            int tileNumWidth = (int) (mapX / viewModel.tileSize);
            int tileNumHeight = (int) (mapY / viewModel.tileSize);

            int tileX = (int) (mapX % viewModel.tileSize);
            int tileY = (int) (mapY % viewModel.tileSize);

            Color hitboxColor = getHitboxColorAt(tileNumWidth, tileNumHeight, tileX, tileY);

            viewModel.updateHoveredColor(hitboxColor);
        });
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
                ImageView currentTile = mapTileIndex.get(new TileCoordinate(x, y));

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

    private void forEachTile(String tileName, BiConsumer<TileCoordinate, Image> tileProcessor) {
        for (int x = 0; x < viewModel.tileNumWidth; x++) {
            for (int y = 0; y < viewModel.tileNumHeight; y++) {
                String path = String.format("/%s_%d_%d.png", tileName, x, y);
                Image tile = new Image(getClass().getResourceAsStream(path), 0, 0, false, false, true);
                TileCoordinate key = new TileCoordinate(x, y);

                tileProcessor.accept(key, tile);
            }
        }
    }

    private Group assembleMapTiles() {
        Group tileGroup = new Group();

        forEachTile(viewModel.mapTileName, (key, tile) -> {
            ImageView tileView = new ImageView(tile);
            tileView.setX(key.x() * viewModel.tileSize);
            tileView.setY(key.y() * viewModel.tileSize);
            tileView.setSmooth(false);

            tileGroup.getChildren().add(tileView);
            mapTileIndex.put(key, tileView);
        });

        return tileGroup;
    }

    private Map<TileCoordinate, Image> assembleHitboxTiles() {
        Map<TileCoordinate, Image> tileIndex = new HashMap<>();

        forEachTile(viewModel.hitboxTileName, tileIndex::put);

        return tileIndex;
    }

    private Color getHitboxColorAt(int tileNumWidth, int tileNumHeight, int x, int y) {
        Image image = hitboxTileIndex.get(new TileCoordinate(tileNumWidth, tileNumHeight));
        return image.getPixelReader().getColor(x, y);
    }
}
