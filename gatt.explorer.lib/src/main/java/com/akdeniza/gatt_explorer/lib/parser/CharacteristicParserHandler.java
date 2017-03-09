package com.akdeniza.gatt_explorer.lib.parser;

import com.akdeniza.gatt_explorer.lib.model.Characteristic;

import java.util.*;

/**
 * Created by Akdeniz on 05/03/2017.
 */

public class CharacteristicParserHandler {

    public static final String FORMAT_STRING = "0x19";
    public static final String FORMAT_16BIT_INTEGER = "0x06";
    public static final String FORMAT_128BIT_INTEGER = "0x0B";


    private StringParser stringParser;
    private IntParser intParser;
    private UUIDParser uuidParser;

    public CharacteristicParserHandler() {
        this.stringParser = new StringParser();
        this.intParser = new IntParser();
        this.uuidParser = new UUIDParser();
    }


    public List<Object> parseAllCharacteristics(List<Object> list) {
        List<Object> newList = list;

        for (int i = 0; i < newList.size(); i++) {
            if (newList.get(i) instanceof Characteristic) {
                Characteristic characteristic = (Characteristic) newList.get(i);
                if (characteristic.getAccess().contains("r")) {

                    switch (characteristic.getFormat()) {
                        case FORMAT_STRING:
                            characteristic.setValue(stringParser.to(characteristic.getValueInByte()));
                            break;
                        case FORMAT_16BIT_INTEGER:
                            characteristic.setValue((intParser.to(characteristic.getValueInByte())));
                            break;
                        case FORMAT_128BIT_INTEGER:
                           characteristic.setValue(uuidParser.to(characteristic.getValueInByte()).toString());
                            break;
                    }
                } else {
                    characteristic.setValue("NOT READABLE");
                }

            }
        }
        return newList;
    }
}
