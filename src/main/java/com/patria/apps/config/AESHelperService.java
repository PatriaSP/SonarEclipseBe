package com.patria.apps.config;

import com.patria.apps.exception.GeneralException;
import jakarta.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AESHelperService {

    @Value("${AES_KEY}")
    private String aesKey;

    private static String secretKey;

    @PostConstruct
    public void init() {
        secretKey = aesKey;
    }

    private static final String ALGORITHM = "AES";

    public static SecretKey generateKey(String keyStr) throws Exception {
        byte[] keyBytes = keyStr.getBytes();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public static String encrypt(String data) throws Exception {
        SecretKey key = generateKey(secretKey);
        String keyStr = keyToString(key);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes).replace("/", "_").replace("+", "-");
    }

    public static String decrypt(String encryptedData, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData.replace("_", "/").replace("-", "+")));
        return new String(decryptedBytes);
    }

    public static String keyToString(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded()).replace("/", "_").replace("+", "-");
    }

    public static SecretKey stringToKey(String keyStr) {
        byte[] decodedKey = Base64.getDecoder().decode(keyStr.replace("_", "/").replace("-", "+"));
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, ALGORITHM);
    }

    public static Long getDecryptedString(String encryptedData) {
        String ret = "";
        try {
            SecretKey key = generateKey(secretKey);
            String keyStr = keyToString(key);

            SecretKey decodedKey = stringToKey(keyStr);
            String decryptedId = "";

            ret = decrypt(encryptedData, decodedKey);
        } catch (Exception ex) {
            ret = ex.getMessage();
        }
        return Long.parseLong(ret);
    }

    public static String aes256CbcEncrypt(byte[] ivByte, String key, String value){
        try {
            String cipherMode = "AES/CBC/PKCS5Padding"; 
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec iv = new IvParameterSpec(ivByte);
            Cipher cipher = Cipher.getInstance(cipherMode);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));

            return Base64.getEncoder().encodeToString(encrypted);
        } catch (UnsupportedEncodingException ex) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error!", ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error!", ex);
        } catch (NoSuchPaddingException ex) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error!", ex);
        } catch (InvalidKeyException ex) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error!", ex);
        } catch (InvalidAlgorithmParameterException ex) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error!", ex);
        } catch (IllegalBlockSizeException ex) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error!", ex);
        } catch (BadPaddingException ex) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error!", ex);
        }
    }

    public static String hmacSha512Base64(String data, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            mac.init(secretKeySpec);

            byte[] hmacBytes = mac.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (Exception e) {
            throw new GeneralException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error!", e);
        }
    }
}
