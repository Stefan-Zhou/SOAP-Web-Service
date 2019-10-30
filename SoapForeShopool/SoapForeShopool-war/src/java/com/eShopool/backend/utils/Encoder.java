package com.eShopool.backend.utils;

import java.io.UnsupportedEncodingException;

/**
 * This class could is used to encoder string which contains special characters.
 * 
 * @author Group 10
 * @version 1.0.1
 * @since 30/10/2019
 */
public class Encoder {

    // Construction.
    public Encoder(){}

    /**
     * This function is used to encode a string by base 64.
     * @param info the given string.
     * @return encoded string.
     */
    public static String Base64Encode(String info){
        try{
            byte[] encryptedData = info.getBytes("utf-8");
            String encodeBase64 = Base64.encodeToString(encryptedData, Base64.NO_WRAP);
            String safeBase64Str = encodeBase64.replace('+', '-');
            safeBase64Str = safeBase64Str.replace('/', '_');
            safeBase64Str = safeBase64Str.replaceAll("=", "");
            return safeBase64Str;
        }catch(UnsupportedEncodingException e){
            return info;
        }

    }
}
