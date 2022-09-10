package com.nickrocky.util;

public class KQRUtil {

    /**
     * Calculates the amount of data (in bytes) a particular QR can hold
     * @param glyphSize Takes the size of the glyph
     * @return The maximum amount of data a particular glyph can hold
     * */
    public static int getByteSizeByGlyphSize(int glyphSize){
        int sizeRatio = (int) Math.floor(glyphSize-2)/2;
        int doubledRate = 5+sizeRatio;
        return ((sizeRatio*9) + (sizeRatio*9) + ((doubledRate)*(doubledRate*2)));
        //System.out.println("Size: " + glyphSize + " Byte Size: " + sizeOfQr);
    }

    /**
     * Suggests a size to store the data presented
     * @param input The information you wish to store in a QR code
     * @return the suggested glyph size
     * @apiNote If this program returns -1 the data will NOT fit
     * @apiNote All glyph sizes are EVEN numbers
     * */
    public static int suggestSize(byte[] input){
        int glyphSize = 6;
        boolean fits = false;
        while(fits){
            if(getByteSizeByGlyphSize(glyphSize) < input.length){
                glyphSize += 2;
            }else{
                fits = true;
            }
            if(glyphSize >= 128){
                glyphSize = -1;
                break;
            }
        }
        return glyphSize;
    }

    /**
     * Before writing a QR code this function is called to ensure that everything will indeed fit
     * */
    public static boolean validate(byte[] input){
        return suggestSize(input)==-1 ? true : false;
    }

}
