package com.akdeniza.gatt_explorer.lib.parser;

import java.math.BigInteger;

/**
 * Created by Akdeniz on 05/03/2017.
 */

public class IntParser {

    public String to(byte[] bytes) {
        return "" + new BigInteger(bytes).intValue();
    }
}
