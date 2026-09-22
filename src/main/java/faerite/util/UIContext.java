package faerite.util;

public class UIContext {

    private static final double LOGICAL_WIDTH = 2560.0;
    private static final double LOGICAL_HEIGHT = 1440.0;

    private static final int LOGICAL_FONT_SIZE = 16;

    private final double screenWidth;
    private final double screenHeight;

    private int fontSize;
    private double uiScale;

    public UIContext(double width, double height) {
        screenWidth = width;
        screenHeight = height;
        calculateScale();
    }

    public void calculateScale() {
        double widthRatio = screenWidth / LOGICAL_WIDTH;
        double heightRatio = screenHeight / LOGICAL_HEIGHT;
        uiScale = Math.min(widthRatio, heightRatio);
        fontSize = (int) Math.round(uiScale * LOGICAL_FONT_SIZE);
    }

    public static int getFontSize() {
    public int getFontSize() {
        return fontSize;
    }

    public double getUiScale() {
        return uiScale;
    }
}
