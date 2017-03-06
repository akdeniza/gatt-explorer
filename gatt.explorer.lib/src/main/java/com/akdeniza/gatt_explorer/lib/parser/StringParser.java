package com.akdeniza.gatt_explorer.lib.parser;

import java.io.UnsupportedEncodingException;

/**
 * Created by Akdeniz on 05/03/2017.
 */

public class StringParser {


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
