package faerite.atlas;

import javafx.geometry.Pos;

public record AtlasStyle(
    double mapPadding,
    int mapBorderSize,

    int infoBoxHorisontalPadding,
    int infoBoxVerticalPadding,
    Pos infoBoxTitleAlignment,
    int infoBoxSpacing
) {
    public static final AtlasStyle DEFAULTS = new AtlasStyle(40.0, 2, 3, 30, Pos.CENTER, 15);
}
