package faerite.util;

public final class Colors {
    private Colors() {}

    public static int deriveColorARGB(int rgb, double satFactor, double brightFactor) {
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = rgb & 0xFF;

        float[] hsb = java.awt.Color.RGBtoHSB(r, g, b, null);
        hsb[1] = (float) Math.clamp(hsb[1] * satFactor, 0.0, 1.0);
        hsb[2] = (float) Math.clamp(hsb[2] * brightFactor, 0.0, 1.0);

        return java.awt.Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
    }
}
