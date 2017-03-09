package com.akdeniza.gatt_explorer.lib.parser;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 *  UUIDParser to parse an 16-Byte array to an UUID
 *  @author Akdeniz on 08/03/2017.
 */

public class UUIDParser {

    /**
     * Parses the given byte array to an UUID
     * @param bytes to be parsed. Must be 16 byte
     * @return the parsed UUID
     */
    public UUID to(byte[] bytes) {

        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long mostSig = bb.getLong();
        long leastSig = bb.getLong();

        return new UUID(mostSig, leastSig);
    }


}
