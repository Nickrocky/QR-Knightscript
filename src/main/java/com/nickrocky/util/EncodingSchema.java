package com.nickrocky.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Composite black and black probably couldn't be discerned by a reader as uniquely different
 * */
@RequiredArgsConstructor
@Getter
public enum EncodingSchema {

    //C | M | Y | K
    //These were assigned out of order intentionally
    //The main idea is that a true color is a overarching TYPE of encoding, and a sub-color (Ex. Dark Yellow) is a specialized version of that type of encoding
    RESERVED(Packages.WHITE, Packages.WHITE), //0000 0000

    BYTE(Packages.WHITE, Packages.CYAN), //0000 1000 CYAN
    /**
     * All other values will remain unassigned until we actually need them
     * */
    ;

    private final Packages msb, lsb;

    public static EncodingSchema getEncoding(Packages type){
        EncodingSchema schema = RESERVED;
        switch (type){
            case CYAN -> schema = BYTE;
        }
        return schema;
    }
}
