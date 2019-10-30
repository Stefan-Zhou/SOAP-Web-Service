package com.eShopool.backend.utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;

public class RSAUtil {
    
    /** Specify RSA as encryption algorithm */
    private static String ALGORITHM = "RSA";
    /** Specify encryption mode and fill mode */
    private static String ALGORITHM_MODEL = "RSA/ECB/PKCS1Padding";
    /** Specifies the size of the key, generally 1024; The larger the key, the higher the security */
    private static int KEYSIZE = 1024;
    /** Specify public key to store files */
    private static String PUBLIC_KEY_FILE = "PublicKey";
    /** Specify private key to store file */
    private static String PRIVATE_KEY_FILE = "PrivateKey";

    public static Map<String ,String> generateKeyPair() throws Exception{
        SecureRandom sr = new SecureRandom();
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(KEYSIZE, sr);
        KeyPair kp = kpg.generateKeyPair();
        Key publicKey = kp.getPublic();
        Key privateKey = kp.getPrivate();
        byte[] pb = publicKey.getEncoded();
        String pbStr =  new String(Base64.getEncoder().encode(pb));
        byte[] pr = privateKey.getEncoded();
        String prStr =  new String(Base64.getEncoder().encode(pr));
        Map<String, String> map = new HashMap<String, String>();
        map.put("publicKey",pbStr);
        map.put("privateKey", prStr);
        return map;
    }

    /** Recovering public key from Base64 encoded public key */
    public static PublicKey getPulbickey(String key_base64) throws Exception{
        byte[] pb = Base64.getDecoder().decode(key_base64);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pb);
        KeyFactory keyfactory = KeyFactory.getInstance("RSA");
        return keyfactory.generatePublic(keySpec);
    }

    /** Recover private key from Base64 encoded private key */
    public static PrivateKey getPrivatekey(String key_base64) throws Exception{
        byte[] pb = Base64.getDecoder().decode(key_base64);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pb);
        KeyFactory  keyfactory = KeyFactory.getInstance("RSA");
        return keyfactory.generatePrivate(keySpec);
    }

    /** Perform encryption operation */
    public static byte[] encrypt(Key key,byte[] source) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] ciphertext = cipher.doFinal(source);
        return ciphertext;
    }
    
    /** Perform decryption operation */
    public static byte[] decrypt(Key key,byte[] ciphertext) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] source = cipher.doFinal(ciphertext);
        return source;
    }
}