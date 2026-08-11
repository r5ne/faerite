import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

import java.io.InputStream;

public class MapView extends Pane {
    private final MapViewModel viewModel;

    private final ImageView mapImageView = new ImageView();
    private final Scale mapScale = new Scale();
    private final Translate mapTranslate = new Translate();

    private Image hitboxMask;
    private Image borderMask;

    public MapView(MapViewModel viewModel) {
        this.viewModel = viewModel;

        // nearest-neighbour interp for pixel art
        mapImageView.setSmooth(false);
        getChildren().add(mapImageView);
        mapImageView.getTransforms().addAll(mapScale, mapTranslate);

        // Keep the background synced with the oceanColor.
        backgroundProperty().bind(Bindings.createObjectBinding(() ->
            new Background(new BackgroundFill(
                    viewModel.getOceanColorProperty().get(),
                    CornerRadii.EMPTY,
                    Insets.EMPTY
            )),
                viewModel.getOceanColorProperty()
        ));

        updateMapData(viewModel.getCurrentMap());
        // Update the map, border and hitbox masks when the currentMap changes.
        viewModel.getCurrentMapProperty().addListener((_, _, newMap) -> {
            if (newMap != null) {
                updateMapData(newMap);
            }
        });

        this.setOnMouseMoved(event -> {
            // Gets absolute position regardless of Scale & Transform objects applied.
            Point2D point = mapImageView.sceneToLocal(event.getSceneX(), event.getSceneY());
            int pixelX = (int) Math.floor(point.getX());
            int pixelY = (int) Math.floor(point.getY());

            // Out of bounds check.
            if (pixelX >= 0 && pixelX < hitboxMask.getWidth() && pixelY >= 0 && pixelY < hitboxMask.getHeight()) {
                int argb = hitboxMask.getPixelReader().getArgb(pixelX, pixelY);
                viewModel.updateHoveredColor(argb);
            } else {
                viewModel.updateHoveredColor(0);
            }
        });
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();

        if (viewModel.getCurrentMap() == null) return;

        int mapWidth = viewModel.getCurrentMap().width();
        int mapHeight = viewModel.getCurrentMap().height();
        double paneWidth = getWidth();
        double paneHeight = getHeight();

        if (paneWidth <= 0 || paneHeight <= 0) return;

        double newScale = Math.min(paneWidth / mapWidth, paneHeight / mapHeight);
        mapScale.setX(newScale);
        mapScale.setY(newScale);

        double scaledWidth = mapWidth * newScale;
        double scaledHeight = mapHeight * newScale;
        mapTranslate.setX((paneWidth - scaledWidth) / 2);
        mapTranslate.setY((paneHeight - scaledHeight) / 2);
    }

    private void updateMapData(MapModel map) {
        this.mapImageView.setImage(loadImage(map.fileName()));
        this.borderMask = loadImage(map.borderMaskFileName());
        this.hitboxMask = loadImage(map.hitboxMaskFileName());
        // Force recalculation of scale and translate properties.
        requestLayout();
    }

    private static Image loadImage(String fileName) {
        String path = String.format("/%s.png", fileName);
        InputStream stream = MapView.class.getResourceAsStream(path);

        if (stream == null) {
            throw new IllegalArgumentException("No file exists at: " + path);
        }
        return new Image(stream, 0, 0, false, false, true);
    }
}
