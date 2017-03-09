package com.akdeniza.gatt_explorer.lib.parser;

/**
 * Created by Akdeniz on 05/03/2017.
 */

public class IntParser {

    public String to(byte[] bytes) {

        long value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value = (value << 8) + (bytes[i] & 0xff);
        }
        return "" + value;

    }

}
