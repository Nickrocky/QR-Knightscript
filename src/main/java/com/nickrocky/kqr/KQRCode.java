package com.nickrocky.kqr;

import com.nickrocky.util.Packages;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.nickrocky.util.Packages.*;

public class KQRCode {

    private final int height, width;

    private final int[] MAGENTA_REFERENCE, CYAN_REFERENCE, YELLOW_REFERENCE,
    DARK_MAGENTA_REFERENCE, DARK_CYAN_REFERENCE, DARK_YELLOW_REFERENCE, GRAY_REFERENCE;

    private Packages[][] colorInput;
    private List<Packages> payload;


    public KQRCode(BufferedImage croppedImage, int height, int width){
        Color magenta = new Color(croppedImage.getRGB(0, 0));
        Color cyan = new Color(croppedImage.getRGB(0, croppedImage.getHeight()-1));
        Color yellow = new Color(croppedImage.getRGB(croppedImage.getWidth()-1, 0));
        MAGENTA_REFERENCE = rgbToCmyk(magenta);
        CYAN_REFERENCE = rgbToCmyk(cyan);
        YELLOW_REFERENCE = rgbToCmyk(yellow);
        DARK_MAGENTA_REFERENCE = rgbToCmyk(magenta);
        DARK_CYAN_REFERENCE = rgbToCmyk(cyan);
        DARK_YELLOW_REFERENCE = rgbToCmyk(yellow);
        GRAY_REFERENCE = new int[]{0, 0, 0, 50};
        DARK_MAGENTA_REFERENCE[3] = 50;
        DARK_CYAN_REFERENCE[3] = 50;
        DARK_YELLOW_REFERENCE[3] = 50;

        colorInput = new Packages[width][height];

        for(int y = 0; y < croppedImage.getHeight(); y++){
            for(int x = 0; x < croppedImage.getWidth(); x++){
                Color color = new Color(croppedImage.getRGB(x,y));
                colorInput[x][y] = convertToPackage(rgbToCmyk(color));
            }
        }
        this.height = height;
        this.width = width;

        payload = readInput(croppedImage);

    }


    private List<Packages> readInput(BufferedImage image){
        List<Packages> data = new ArrayList<>();
        for(int y = height-1; y > 9; y--){
            for(int x = width-1; x > 10; x--){
                //System.out.println("X: " + x + " Y: " + y);
                Color color = new Color(image.getRGB(x,y));
                Packages p = convertToPackage(rgbToCmyk(color));
                data.add(p);
                //System.out.println("X: " + x + " Y: " + y + " " + p.name());
            }
        }
        for(int y = height-1; y > 8; y--){
            for(int x = 8; x > 9; x--){
                Color color = new Color(image.getRGB(x,y));
                Packages p = convertToPackage(rgbToCmyk(color));
                data.add(p);
                //System.out.println("X: " + x + " Y: " + y + " " + p.name());
            }


        }
        for(int y = width-10; y > 9; y--){
            for(int x = 8; x > 0; x--){
                Color color = new Color(image.getRGB(x,y));
                Packages p = convertToPackage(rgbToCmyk(color));
                data.add(p);
                //System.out.println("X: " + x + " Y: " + y + " " + p.name());
            }
        }

        return data;
    }

    //From HackABull 2022 Project, its just a util though
    private int[] rgbToCmyk(Color color) {
        double percentageR = color.getRed() / 255.0 * 100;
        double percentageG = color.getGreen() / 255.0 * 100;
        double percentageB = color.getBlue() / 255.0 * 100;

        double k = 100 - Math.max(Math.max(percentageR, percentageG), percentageB);

        if (k == 100) {
            return new int[]{ 0, 0, 0, 100 };
        }

        int c = (int)((100 - percentageR - k) / (100 - k) * 100);
        int m = (int)((100 - percentageG - k) / (100 - k) * 100);
        int y = (int)((100 - percentageB - k) / (100 - k) * 100);

        return new int[]{ c, m, y, (int)k };
    }

    Packages convertToPackage(int[] cmyk){
        int cyan, magenta, yellow, key;
        cyan = cmyk[0];
        magenta = cmyk[1];
        yellow = cmyk[2];
        key = cmyk[3];

        if(cmyk[0] <= 20) cyan = 0;
        if(cmyk[1] <= 20) magenta = 0;
        if(cmyk[2] <= 20) yellow = 0;
        if(cmyk[3] > 50 && cmyk[3] <=80){
            key = 75;
        }else{
            if(key >= 30 && key < 50){
                key = 50;
            }else{
                key = 0;
            }
        }

        if(cyan == MAGENTA_REFERENCE[0] && magenta == MAGENTA_REFERENCE[1] && yellow == MAGENTA_REFERENCE[2]){
            if(key == 50) return DARK_MAGENTA;
            return Packages.MAGENTA;
        }
        if(cyan == CYAN_REFERENCE[0] && magenta == CYAN_REFERENCE[1] && yellow == CYAN_REFERENCE[2]){
            if(key == 50) return DARK_CYAN;
            return Packages.CYAN;
        }
        if(cyan == YELLOW_REFERENCE[0] && magenta == YELLOW_REFERENCE[1] && yellow == YELLOW_REFERENCE[2]){
            if(key == 50) return DARK_YELLOW;
            return Packages.YELLOW;
        }
        //green
        if(cyan == CYAN_REFERENCE[0] && magenta == YELLOW_REFERENCE[1] && yellow == YELLOW_REFERENCE[2]){
            if(key == 50) return DARK_GREEN;
            return Packages.GREEN;
        }
        //blue
        if(cyan == CYAN_REFERENCE[0] && magenta == MAGENTA_REFERENCE[1] && yellow == MAGENTA_REFERENCE[2]){
            if(key == 50) return DARK_BLUE;
            return Packages.BLUE;
        }
        //red
        if(cyan == MAGENTA_REFERENCE[0] && magenta == MAGENTA_REFERENCE[1] && yellow == YELLOW_REFERENCE[2]){
            if(key == 50) return DARK_RED;
            return Packages.RED;
        }
        //white
        if(cyan == 0 && magenta == 0 && yellow == 0){
            if(key == 50) return GRAY;
            if(key == 75) return DARK_GRAY;
            return Packages.WHITE;
        }
        if(cyan != 100 && magenta != 100 && yellow != 100) System.out.println("Misread color: " + cmyk[0] + " " + cmyk[1] + " " + cmyk[2] + " " + cmyk[3]);
        return Packages.BLACK;
    }

}
