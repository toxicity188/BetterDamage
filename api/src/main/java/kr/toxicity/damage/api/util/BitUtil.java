package kr.toxicity.damage.api.util;

/**
 * Bit util
 */
public final class BitUtil {

    /**
     * No initializer
     */
    private BitUtil() {
        throw new RuntimeException();
    }

    /**
     * Sets float opacity to display byte
     * @param opacity float opacity
     * @return byte
     */
    public static byte opacityToByte(float opacity) {
        return (byte) (Math.round(opacity) & 0xFF);
    }
}
