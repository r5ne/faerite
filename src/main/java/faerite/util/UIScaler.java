package faerite.util;

public class UIScaler {
    private UIScaler() {}

    private static final int LOGICAL_FONT_SIZE = 16;
    private static final double LOGICAL_WIDTH = 2560.0;
    private static final double LOGICAL_HEIGHT = 1440.0;

    private static int fontSize;
    private static double uiScale;

    public static void calculateScale(double width, double height) {
        double widthRatio = width / LOGICAL_WIDTH;
        double heightRatio = height / LOGICAL_HEIGHT;
        uiScale = Math.min(widthRatio, heightRatio);
        fontSize = (int) Math.round(uiScale * LOGICAL_FONT_SIZE);
    }

    public static int getFontSize() {
        return fontSize;
    }

    public static double getUiScale() {
        return uiScale;
    }
}
