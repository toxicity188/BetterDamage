package kr.toxicity.damage.api.util;

public final class BitUtil {
    private BitUtil() {
        throw new RuntimeException();
    }

    public static byte opacityToByte(float opacity) {
        return (byte) (Math.round(opacity) & 0xFF);
    }
}
