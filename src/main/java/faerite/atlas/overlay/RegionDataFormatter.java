package faerite.atlas.overlay;

import java.text.DecimalFormat;

public final class RegionDataFormatter {
    private static final DecimalFormat decimalFormatter = new DecimalFormat("#,###.##");

    private RegionDataFormatter() {}

    public static String formatLong(long value) {
        String formattedLong = formatNumber(value);

        if (formatNumber(value) != null) {
            return formattedLong;
        } else {
            return String.format("%,d", value);
        }
    }

    public static String formatDouble(double value) {
        String formattedDouble = formatNumber(value);

        if (formatNumber(value) != null) {
            return formattedDouble;
        } else {
            return decimalFormatter.format(value);
        }
    }

    private static String formatNumber(Number number) {
        Double doubleValue = number.doubleValue();

        if (doubleValue.compareTo(1000000.0) < 0) {
            return null;
        }

        String formattedNumber;

        if (doubleValue.compareTo(1000000000.0) >= 0) {
            formattedNumber = doubleValue / 1000000000 + " billion";
        } else {
            formattedNumber = doubleValue / 1000000 + " million";
        }

        return formattedNumber;
    }
}
