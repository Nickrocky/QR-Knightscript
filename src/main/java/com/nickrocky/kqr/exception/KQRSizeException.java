package com.nickrocky.kqr.exception;

public class KQRSizeException extends Exception{
    public KQRSizeException(int payloadSize){
        super("The payload you attempted to encode was larger than allowed for KQR codes. You attempted to encode a payload of " + payloadSize + " bytes, while we only support 9600 bytes at this time.");
    }
}
