package com.nickrocky;

import lombok.SneakyThrows;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Deflater;

import static com.nickrocky.Packages.*;

/**
 * Currently its 1 pixel per package, in the future it would be nice to make it allow for more than one pixel shouldnt be awful either
 * */
public class KQRWriter {

    private final int MARKER_AND_QUIET_ZONE = 9; //in pixels

    public void create(String fileName, EncodingSchema schema, byte[] payload){
        switch (schema){
            //Essentially dumping bytes into a qr code
            case BYTE -> createByteQRCode(fileName, payload);
            //case BYTE_COMPRESSED -> createByteQRCode(fileName, payload, true);
        }
    }

    private void createByteQRCode(String fileName, byte[] payload){
        createByteQRCode(fileName, payload, false, 1);
    }

    @SneakyThrows
    private void createByteQRCode(String fileName, byte[] payload, boolean useCompression, long templateNumber){
        if(KQRUtil.validate(payload)) throw new KQRSizeException(payload.length);
        int qrCodeSize = KQRUtil.suggestSize(payload) + (2 * MARKER_AND_QUIET_ZONE); //This should be the distance across the top, but since they are squares we use it for both measurements
        BufferedImage qrImage = new BufferedImage(qrCodeSize, qrCodeSize, BufferedImage.TYPE_INT_RGB);
        applyBaseline(qrImage, qrCodeSize);
        List<Packages> packs = new ArrayList<>();
        byte[] correctedPayload = null;
        if(useCompression){
            byte[] buffer = new byte[9600];
            Deflater compressor = new Deflater();
            compressor.setInput(payload);
            compressor.finish();
            int size = compressor.deflate(buffer);
            compressor.end();

            Deflater somethingElse = new Deflater();
            somethingElse.setInput(payload);
            somethingElse.finish();
            byte[] realBuffer = new byte[size];
            somethingElse.deflate(realBuffer);
            somethingElse.end();
            correctedPayload = realBuffer;

        }else{
            correctedPayload = payload;
        }

        for(byte b : correctedPayload){
            var packages = new Packages[2];
            if(useCompression){
                packages = CharacterSet.getPackageFromByte((byte) (((int) b) + 128));
            }else{
                packages = CharacterSet.getPackageFromByte(b);
            }

            packs.add(packages[0]);
            packs.add(packages[1]);
        }
        if(useCompression){
            //applyData(qrImage, qrCodeSize, EncodingSchema.BYTE_COMPRESSED, packs);
        }else{
            System.out.println("Done!");
            applyData(qrImage, qrCodeSize, EncodingSchema.BYTE, packs, templateNumber);
        }
        File glyphExport = new File(fileName+".png");
        ImageIO.write(qrImage, "png", glyphExport);
    }

    private void createAppQRCode(String fileName, byte[] payload){

    }

    private void applyBaseline(BufferedImage image, int glyphSize){
        int magentaPixel = (MAGENTA.getRGBColor().getRed()<<16 | MAGENTA.getRGBColor().getGreen()<<8 | MAGENTA.getRGBColor().getBlue());
        int yellowPixel = (YELLOW.getRGBColor().getRed()<<16 | YELLOW.getRGBColor().getGreen()<<8 | YELLOW.getRGBColor().getBlue());
        int cyanPixel = (CYAN.getRGBColor().getRed()<<16 | CYAN.getRGBColor().getGreen()<<8 | CYAN.getRGBColor().getBlue());

        //-- Start Magenta Marker

        //Set the top row of the Magenta Marker
        for(int x = 0; x < 8; x++){
            image.setRGB(x, 0, magentaPixel);
        }
        image.setRGB(0, 1, magentaPixel);
        image.setRGB(5, 1, magentaPixel);
        image.setRGB(6, 1, magentaPixel);
        image.setRGB(7, 1, magentaPixel);
        image.setRGB(6, 2, magentaPixel);

        for(int x = 0; x < 4; x++){
            image.setRGB(0, x+2, magentaPixel);
            image.setRGB(2, x+2, magentaPixel);
            image.setRGB(3, x+2, magentaPixel);
            image.setRGB(4, x+2, magentaPixel);
            image.setRGB(5, x+2, magentaPixel);
            image.setRGB(7, x+2, magentaPixel);
        }

        image.setRGB(0, 6, magentaPixel);
        image.setRGB(7, 6, magentaPixel);
        for(int x = 0; x < 8; x++){
            image.setRGB(x, 7, magentaPixel);
        }

        //-- End Magenta Marker

        //-- Start Yellow Marker
        for(int x = glyphSize; x > (glyphSize-8); x--){
            image.setRGB(x-1, 0, yellowPixel);
        }

        image.setRGB(glyphSize-8, 1, yellowPixel);
        image.setRGB(glyphSize-3, 1, yellowPixel);
        image.setRGB(glyphSize-2, 1, yellowPixel);
        image.setRGB(glyphSize-1, 1, yellowPixel);
        image.setRGB(glyphSize-2, 2, yellowPixel);

        for(int x = 0; x < 4; x++){
            image.setRGB(glyphSize-8, x+2, yellowPixel);
            image.setRGB(glyphSize-6, x+2, yellowPixel);
            image.setRGB(glyphSize-5, x+2, yellowPixel);
            image.setRGB(glyphSize-4, x+2, yellowPixel);
            image.setRGB(glyphSize-3, x+2, yellowPixel);
            image.setRGB(glyphSize-1, x+2, yellowPixel);
        }

        image.setRGB(glyphSize-8, 6, yellowPixel);
        image.setRGB(glyphSize-1, 6, yellowPixel);
        for(int x = glyphSize; x > (glyphSize-8); x--){
            image.setRGB(x-1, 7, yellowPixel);
        }

        //-- End Yellow Marker

        //-- Start Cyan Marker
        for(int x = 0; x < 8; x++){
            image.setRGB(x, glyphSize-8, cyanPixel);
        }
        image.setRGB(0, glyphSize-7, cyanPixel);
        image.setRGB(7, glyphSize-7, cyanPixel);
        image.setRGB(6, glyphSize-7, cyanPixel);
        image.setRGB(5, glyphSize-7, cyanPixel);
        image.setRGB(6, glyphSize-6, cyanPixel);

        for(int y = glyphSize-6; y < glyphSize-2; y++){
            image.setRGB(0, y, cyanPixel);
            image.setRGB(2, y, cyanPixel);
            image.setRGB(3, y, cyanPixel);
            image.setRGB(4, y, cyanPixel);
            image.setRGB(5, y, cyanPixel);
            image.setRGB(7, y, cyanPixel);

        }

        image.setRGB(0, glyphSize-2, cyanPixel);
        image.setRGB(7, glyphSize-2, cyanPixel);
        for(int x = 0; x < 8; x++){
            image.setRGB(x, glyphSize-1, cyanPixel);
        }
        //-- End Cyan Marker

        //-- Start Timing Traces

        int timingLength = (glyphSize-(MARKER_AND_QUIET_ZONE*2));
        int blackPixel = 0; //0 is usually straight zeros across for an int
        int whitePixel = (WHITE.getRGBColor().getRed()<<16 | WHITE.getRGBColor().getGreen()<<8 | WHITE.getRGBColor().getBlue());

        //-- Start vertical timer bar
        for(int y = 9; y < 9+timingLength; y++){
            if ((y % 2) == 1) {
                image.setRGB(9, y, whitePixel);
            } else {
                image.setRGB(9, y, blackPixel);
            }
        }
        //-- End vertical timer bar

        //--Start horizontal timer bar
        for(int x = 9; x < 9+timingLength; x++){
            if((x%2)==1){
                image.setRGB(x, 9, whitePixel);
            }else{
                image.setRGB(x, 9, blackPixel);
            }
        }
        //-- End Horizontal timer bar

        //That should be the end of the basic 'always here' glyph components
    }

    private void applyData(BufferedImage image, int glyphSize, EncodingSchema schema, List<Packages> payload, long template){

        //Encode the encoding data and the template indicator
        /**
         * A brief aside on the template indicator...
         * The entire intent is that you essentially run the indicator number through a hashing algorithm
         * and that hashing algorithm output can then be used to request and download from a database.
         * The URL would be regular enough to essentially do something akin to
         * template.<domain>.<tld>/<hash>
         *     where that domain would effectively act as a repo server of all of these templates
         * */

        //Alright now back to the main attraction

        int encodingLSBpixel = (schema.getLsb().getRGBColor().getRed()<<16 | schema.getLsb().getRGBColor().getGreen()<<8 | schema.getLsb().getRGBColor().getBlue());
        int encodingMSBpixel = (schema.getMsb().getRGBColor().getRed()<<16 | schema.getMsb().getRGBColor().getGreen()<<8 | schema.getMsb().getRGBColor().getBlue());

        //-- Start Encoding Type LSB/MSB on all 3 markers
        image.setRGB(0, 9, encodingMSBpixel); //Under Magenta Marker
        image.setRGB(1,9, encodingLSBpixel); //Under Magenta Marker

        image.setRGB(9, glyphSize-1, encodingMSBpixel); //Right side of Cyan Marker
        image.setRGB(9, glyphSize-2, encodingLSBpixel); //Right side of Cyan Marker

        image.setRGB(glyphSize-10, 0, encodingMSBpixel); //Left side of Yellow
        image.setRGB(glyphSize-10, 1, encodingLSBpixel); //Left side of Yellow


        //-- End Encoding Type stuff




        byte[] templateNumberInBytes = new byte[8]; //bc a long is 8 bytes
        for(int i = 7; i >= 0; i--){
            templateNumberInBytes[i] = (byte)(template & 0xFF);
            template >>= 8;
        }
        List<Packages> packs = new ArrayList<>();
        for(byte b : templateNumberInBytes){
            var temp = CharacterSet.getPackageFromByte(b);
            packs.add(temp[0]);
            packs.add(temp[1]);
        }

        int TEMPLATE_DATA_INDEX = 0;
        //UGH yes ugly hardcoding but also these are always at fixed points relative to the markers
        //--Start Template Encoding Number - MAGENTA
        for(int x = 2; x < 9; x++){
            image.setRGB(x,9, (payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getRed()<<16 | payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getGreen()<<8 | payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getBlue()));
            TEMPLATE_DATA_INDEX++;
        }
        for(int y = 8; y > 0; y--){
            image.setRGB(9,y, (payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getRed()<<16 | payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getGreen()<<8 | payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getBlue()));
            TEMPLATE_DATA_INDEX++;
        }
        //--End Template Encoding Number - MAGENTA

        /**
         * These are effectively redundancy, they dont actually all encode different info, its just that
         * template information is INCREDIBLY important, and without it you physically cannot finish reading the code
         * as such its been encoded 3 times the idea is, even if someone tore it in half, maybe one of the encoding regions would
         * survive, granted youll have bigger issues on your hands if you didnt use error correction to account for 50% data loss
         * */
        TEMPLATE_DATA_INDEX = 0;
        //--Start Template Encoding Number - YELLOW
        int templatex = glyphSize-MARKER_AND_QUIET_ZONE-1;
        for(int y = 2; y < 9; y++){
            image.setRGB(templatex,y, (payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getRed()<<16 | payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getGreen()<<8 | payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getBlue()));
            TEMPLATE_DATA_INDEX++;
        }
        for(int x = templatex+1; x < glyphSize-1; x++){
            image.setRGB(x,MARKER_AND_QUIET_ZONE, (payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getRed()<<16 | payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getGreen()<<8 | payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getBlue()));
            TEMPLATE_DATA_INDEX++;
        }
        //--End Template Encoding Number - YELLOW

        TEMPLATE_DATA_INDEX = 0;
        //--Start Template Encoding Number - CYAN
        for(int y = glyphSize-3; y > glyphSize-MARKER_AND_QUIET_ZONE-1; y--){
            image.setRGB(9,y, (payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getRed()<<16 | payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getGreen()<<8 | payload.get(TEMPLATE_DATA_INDEX).getRGBColor().getBlue()));
            TEMPLATE_DATA_INDEX++;
        }


        int DATA_INDEX = 0;
        //-- Start Stamping Zone 1 (Big Bottom Area)

        int LOWER_BOTTOM_HARD_X = 10; //Cannot go past
        int LOWER_BOTTOM_HARD_Y = 10;

        int x = glyphSize-1,y = glyphSize-1;
        for(Packages packages : payload){
            System.out.println("Bruh " + x + " " +y + " Color " + packages.name());
            image.setRGB(x, y, (packages.getRGBColor().getRed()<<16 | packages.getRGBColor().getGreen()<<8 | packages.getRGBColor().getBlue()));
            DATA_INDEX++;
            if(x == LOWER_BOTTOM_HARD_X && y == LOWER_BOTTOM_HARD_Y) break;
            if(x == LOWER_BOTTOM_HARD_X){
                y--;
                x = (glyphSize-1);
                continue;
            }
            x--;
        }

        //-- End Stamping Zone 1

        //-- Start Stamping Zone 2 (Top)

        int startingPointZone2X = glyphSize - (MARKER_AND_QUIET_ZONE+2);
        int startingPointZone2Y = 8;

        for(int i = DATA_INDEX; i < payload.size(); i++){
            System.out.println("X : " + startingPointZone2X + " Y " + startingPointZone2Y + " " + payload.get(i).name());
            image.setRGB(startingPointZone2X, startingPointZone2Y, (payload.get(i).getRGBColor().getRed()<<16 | payload.get(i).getRGBColor().getGreen()<<8 | payload.get(i).getRGBColor().getBlue()));
            DATA_INDEX++;
            if(startingPointZone2X == 10 && startingPointZone2Y == 0) break;
            if(startingPointZone2X == 10){
                startingPointZone2Y--;
                startingPointZone2X = glyphSize - (MARKER_AND_QUIET_ZONE + 2);
                continue;
            }
            startingPointZone2X--;
        }

        //-- Start Zone 3 Side
        int startingPointZone3X = 8;
        int startingPointZone3Y = 13;
        for(int i = DATA_INDEX; i < payload.size(); i++){
            image.setRGB(startingPointZone3X, startingPointZone3Y, (payload.get(i).getRGBColor().getRed()<<16 | payload.get(i).getRGBColor().getGreen()<<8 | payload.get(i).getRGBColor().getBlue()));
            if(startingPointZone3X == 0 && startingPointZone3Y == 10) break;
            if(startingPointZone3X == 0){
                startingPointZone3Y--;
                startingPointZone3X = 8;
                continue;
            }
            startingPointZone3X--;

        }
        System.out.println("Packages " + payload.size());


    }

}
