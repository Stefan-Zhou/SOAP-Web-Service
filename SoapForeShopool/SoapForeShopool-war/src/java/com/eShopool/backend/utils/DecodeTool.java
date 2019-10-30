package com.eShopool.backend.utils;

import java.util.Base64;

/**
 * This class is a tool which is used to decode the image encoded by Base64.
 * 
 * @author Group 10
 * @version 1.0.1
 * @since 30/10/2019
 */
public class DecodeTool {

    public DecodeTool(){}

    public static String Base64Decoder(String str) {
        byte[] data = Base64.getDecoder().decode(str);
        String decode = null;
        decode = new String(data);
        return decode;
    }

}