package com.nickrocky.util;

/**
 * Composite black and black probably couldnt be discerned by a reader as uniquely different
 * */
public enum EncodingSchema {

    RESERVED, //0000
    BYTE, //1000
    /**
     * All other values will remain unassigned until we actually need them
     * */
    RESERVED_2, //1110 COMPOSITE BLACK
    RESERVED_3; //0001 BLACK

    EncodingSchema(){}

    public static EncodingSchema getEncoding(Packages type){
        EncodingSchema schema = RESERVED;
        switch (type){
            case CYAN -> schema = BYTE;
        }
        return schema;
    }
}
