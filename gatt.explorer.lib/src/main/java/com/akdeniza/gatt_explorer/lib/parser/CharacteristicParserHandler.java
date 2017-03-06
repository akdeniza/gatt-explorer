package com.akdeniza.gatt_explorer.lib.parser;

import com.akdeniza.gatt_explorer.lib.model.Characteristic;

import java.util.List;

/**
 * Created by Akdeniz on 05/03/2017.
 */

public class CharacteristicParserHandler {

    public static final String FORMAT_STRING = "0x19";
    public static final String FORMAT_16BIT_INTEGER = "0x06";


    private StringParser stringParser;
    private IntParser intParser;

    public CharacteristicParserHandler() {
        this.stringParser = new StringParser();
        this.intParser = new IntParser();
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
                    }
                } else {
                    characteristic.setValue("NOT READABLE");
                }

            }
        }
        return newList;
    }
}
