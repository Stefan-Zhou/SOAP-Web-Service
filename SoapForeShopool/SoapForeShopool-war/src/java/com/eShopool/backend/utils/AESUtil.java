package com.eShopool.backend.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @Author: Roylion
 * @Description: AES algorithm encapsulation
 * @Date: Created in 9:46 2018/8/9
 */
public class AESUtil {
    
    /**
     * Encryption algorithm
     */
    private static final String ENCRY_ALGORITHM = "AES";

    /**
     * Encryption algorithm / encryption mode / fill type
     * This example uses AES encryption, ECB encryption mode, pkcs5padding filling
     */
    private static final String CIPHER_MODE = "AES/ECB/PKCS5Padding";

    /**
     * Set IV offset
     * In this example, the ECB encryption mode is adopted, and the IV offset does not need to be set
     */
    private static final String IV_ = null;

    /**
     * Set encrypted character set
     * This example uses UTF-8 character set
     */
    private static final String CHARACTER = "UTF-8";

    /**
     * Set encryption password processing length
     * Fill 0 if the length is less than this
     */
    private static final int PWD_SIZE = 16;

    /**
     * Password processing method
     * If there is a problem with encryption and decryption, Please check this method first.
     * Exclude that the password length is insufficient and fill in "0", resulting in inconsistent passwords.
     * @param password Pending password
     * @return
     * @throws UnsupportedEncodingException
     */
    private static byte[] pwdHandler(String password) throws UnsupportedEncodingException {
        byte[] data = null;
        if (password == null) {
            password = "";
        }
        StringBuffer sb = new StringBuffer(PWD_SIZE);
        sb.append(password);
        while (sb.length() < PWD_SIZE) {
            sb.append("0");
        }
        if (sb.length() > PWD_SIZE) {
            sb.setLength(PWD_SIZE);
        }

        data = sb.toString().getBytes("UTF-8");

        return data;
    }

    /**
     * Original encryption
     * @param clearTextBytes Clear text byte array, byte array to be encrypted
     * @param pwdBytes Encryption password byte array
     * @return Returns the encrypted ciphertext byte array, and the encryption error returns null
     */
    public static byte[] encrypt(byte[] clearTextBytes, byte[] pwdBytes) {
        try {
            // 1 Get encryption key
            SecretKeySpec keySpec = new SecretKeySpec(pwdBytes, ENCRY_ALGORITHM);

            // 2 Get cipher instance
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);

            // 3 Initialize cipher instance. Set execution mode and encryption key
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);

            // 4 execute
            byte[] cipherTextBytes = cipher.doFinal(clearTextBytes);

            // 5 Return ciphertext character set
            return cipherTextBytes;

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Original decryption
     * @param cipherTextBytes Ciphertext byte array, byte array to be decrypted
     * @param pwdBytes Decryption password byte array
     * @return Return the plaintext byte array after decryption, and return null for decryption error
     */
    public static byte[] decrypt(byte[] cipherTextBytes, byte[] pwdBytes) {

        try {
            // 1 Get decryption key
            SecretKeySpec keySpec = new SecretKeySpec(pwdBytes, ENCRY_ALGORITHM);

            // 2 Get cipher instance
            Cipher cipher = Cipher.getInstance(CIPHER_MODE);
            
            // 3 Initialize cipher instance. Set execution mode and encryption key
            cipher.init(Cipher.DECRYPT_MODE, keySpec);

            // 4 execute
            byte[] clearTextBytes = cipher.doFinal(cipherTextBytes);

            // 5 Return clear text character set
            return clearTextBytes;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Decryption error, return null
        return null;
    }
    
    /**
     * BASE64 encryption
     * @param clearText Plaintext, content to be encrypted
     * @param password Password, encrypted password
     * @return Return the ciphertext, encrypted content. Encryption error return null
     */
    public static String encryptBase64(String clearText, String password) {
        try {
            // 1 Get encrypted ciphertext byte array
            byte[] cipherTextBytes = encrypt(clearText.getBytes(CHARACTER), pwdHandler(password));
            
            String cipherText = Base64.getEncoder().encodeToString(cipherTextBytes);

            // 2 Returns the ciphertext output by Base64
            return cipherText;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Decryption error, return null
        return null;
    }

    /**
     * BASE64 Decryption
     * @param cipherText Encrypted content with decryption
     * @param password Password, decrypted password
     * @return Return the plaintext, the content after decryption. Decryption error return null
     */
    public static String decryptBase64(String cipherText, String password) {
        try {
            // 1 Using base64 decodebuffer to get ciphertext byte array of Base64 output ciphertext
            byte[] cipherTextBytes = Base64.getDecoder().decode(cipherText);

            // 2 Decrypt the ciphertext byte array to get the plaintext byte array
            byte[] clearTextBytes = decrypt(cipherTextBytes, pwdHandler(password));

            // 3 Return clear text string according to character transcoding
            return new String(clearTextBytes, CHARACTER);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Decryption error, return null
        return null;
    }

    /**
     * HEX Encryption
     * @param clearText Plaintext, content to be encrypted
     * @param password Password, encrypted password
     * @return Return the ciphertext, encrypted content. Encryption error return null
     */
    public static String encryptHex(String clearText, String password) {
        try {
            // 1 Get encrypted ciphertext byte array
            byte[] cipherTextBytes = encrypt(clearText.getBytes(CHARACTER), pwdHandler(password));

            // 2 Convert ciphertext byte array to hex output ciphertext
            String cipherText = byte2hex(cipherTextBytes);

            // 3 Return hex output ciphertext
            return cipherText;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Decryption error, return null
        return null;
    }

    /**
     * HEX Decryption
     * @param cipherText Plaintext, content to be encrypted
     * @param password Password, encrypted password
     * @return Return the ciphertext, encrypted content. Encryption error return null
     */
    public static String decryptHex(String cipherText, String password) {
        try {
            // 1 Convert hex output ciphertext to ciphertext byte array
            byte[] cipherTextBytes = hex2byte(cipherText);

            // 2 Decrypt the ciphertext byte array to get the plaintext byte array
            byte[] clearTextBytes = decrypt(cipherTextBytes, pwdHandler(password));

            // 3 Return clear text string according to character transcoding
            return new String(clearTextBytes, CHARACTER);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Decryption error, return null
        return null;
    }

    /**
     * Byte array converted to hexadecimal string
     */
    public static String byte2hex(byte[] bytes) {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        String tmp = "";
        for (int n = 0; n < bytes.length; n++) {
            // Integer to hexadecimal
            tmp = (java.lang.Integer.toHexString(bytes[n] & 0XFF));
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString().toUpperCase();
    }

    /**
     * Convert hex string to byte array
     */
    private static byte[] hex2byte(String str) {
        if (str == null || str.length() < 2) {
            return new byte[0];
        }
        str = str.toLowerCase();
        int l = str.length() / 2;
        byte[] result = new byte[l];
        for (int i = 0; i < l; ++i) {
            String tmp = str.substring(2 * i, 2 * i + 2);
            result[i] = (byte) (Integer.parseInt(tmp, 16) & 0xFF);
        }
        return result;
    }

}
