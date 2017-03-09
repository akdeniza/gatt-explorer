package com.akdeniza.gatt_explorer.lib.parser;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by Akdeniz on 08/03/2017.
 */

public class UUIDParser {

    public UUID to(byte[] bytes) {

        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long mostSig = bb.getLong();
        long leastSig = bb.getLong();

        return new UUID(mostSig, leastSig);
    }


}
