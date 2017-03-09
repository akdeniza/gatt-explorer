package com.akdeniza.gatt_explorer.lib.parser;

import java.io.UnsupportedEncodingException;

/**
 *  String parser to parse from a byte array to an string
 *  @author Akdeniz on 05/03/2017.
 */

public class StringParser {


    /**
     * Parses the given byte array into an String
     * @param bytes to be parsed
     * @return parsed string
     */
    public String to(byte[] bytes){
        String parsed = null;
        try {
            parsed = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return parsed;
    }
}
