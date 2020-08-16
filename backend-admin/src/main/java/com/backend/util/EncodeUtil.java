package com.backend.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class EncodeUtil {

    /**
     * Hex编码.
     */
    public static String encodeHex(byte[] input) {
        return new String(Hex.encodeHex(input));
    }

    /**
     * Hex解码.
     */
    public static byte[] decodeHex(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            return null;
        }
    }

}
