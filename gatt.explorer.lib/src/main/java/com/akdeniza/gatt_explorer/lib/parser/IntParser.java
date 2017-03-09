package com.akdeniza.gatt_explorer.lib.parser;

/**
 * Integer parser to parse from an byte array to an int
 * @author Akdeniz on 05/03/2017.
 */

public class IntParser {

    /**
     * Parses the given byte array to an integer and returns the value as a string
     * @param bytes byte array to be parsed
     * @return string of the parsed value
     */
    public String to(byte[] bytes) {

        long value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value = (value << 8) + (bytes[i] & 0xff);
        }
        return "" + value;

    }

}
