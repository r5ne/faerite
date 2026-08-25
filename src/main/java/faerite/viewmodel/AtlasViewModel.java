package faerite.viewmodel;

import faerite.model.MapModel;
import faerite.model.RegionSelectionModel;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AtlasViewModel {

    private final ObservableList<MapViewModel> layerHistory = FXCollections.observableArrayList();
    private final IntegerProperty currentLayerIndex = new SimpleIntegerProperty(-1);
    private final ObjectProperty<MapViewModel> activeLayer = new SimpleObjectProperty<>();

    private final IntegerProperty oceanColor = new SimpleIntegerProperty(0x213840); // rgb
    private final IntegerProperty hoveredBorderColor = new SimpleIntegerProperty(); // argb
    private final IntegerProperty selectedBorderColor = new SimpleIntegerProperty(0xffff3d3d); // argb

    public AtlasViewModel(MapModel rootMapModel) {
        hoveredBorderColor.set(deriveColorARGB(oceanColor.get(), 0.7, 3));

        currentLayerIndex.addListener((_, _, newIndex) -> {
            if (newIndex.intValue() >= 0 && newIndex.intValue() < layerHistory.size()) {
                activeLayer.set(layerHistory.get(newIndex.intValue()));
            }
        });

        zoomIn(rootMapModel);
    }

    public void zoomIn(MapModel mapModel) {
        int nextIndex = currentLayerIndex.get() + 1;

        if (nextIndex < layerHistory.size()) {
            if (layerHistory.get(nextIndex).mapModel.name().equals(mapModel.name())) {
                currentLayerIndex.set(nextIndex);
                return;
            } else {
                layerHistory.subList(nextIndex, layerHistory.size()).clear();
            }
        }

        layerHistory.add(new MapViewModel(mapModel));
        currentLayerIndex.set(nextIndex);
    }

    public void zoomOut() {
        if (currentLayerIndex.get() > 0) {
            MapViewModel oldMap = activeLayer.get();
            currentLayerIndex.set(currentLayerIndex.get() - 1);
            MapViewModel newMap = activeLayer.get();

            RegionSelectionModel oldSelectedRegion = oldMap.getSelectedRegion();
            if (oldSelectedRegion != null && oldSelectedRegion.parentMapMaskColor() != null) {
                newMap.setSelectedRegionByColor(oldSelectedRegion.parentMapMaskColor());
            }
        }
    }

    public MapViewModel getRootLayer() { return layerHistory.getFirst(); }

    public MapViewModel getActiveLayer() {
        return activeLayer.get();
    }

    public ReadOnlyObjectProperty<MapViewModel> activeLayerProperty() {
        return activeLayer;
    }

    public int getOceanColor() {
        return oceanColor.get();
    }

    public IntegerProperty oceanColorProperty() {
        return oceanColor;
    }

    public int getHoveredBorderColor() {
        return hoveredBorderColor.get();
    }

    public IntegerProperty hoveredBorderColorProperty() {
        return hoveredBorderColor;
    }

    public int getSelectedBorderColor() {
        return selectedBorderColor.get();
    }

    public IntegerProperty selectedBorderColorProperty() {
        return selectedBorderColor;
    }

    private int deriveColorARGB(int rgb, double satFactor, double brightFactor) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        float[] hsb = java.awt.Color.RGBtoHSB(r, g, b, null);
        hsb[1] = (float) Math.clamp(hsb[1] * satFactor, 0.0, 1.0);
        hsb[2] = (float) Math.clamp(hsb[2] * brightFactor, 0.0, 1.0);

        return java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }
}
