package com.refinitiv.collab.platform;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Base64;

@Log4j2
public class CipherTrail {
    public static void main(String[] args) {

        // To run this example, first initialize the client, and create a table
        // named 'Game' with a primary key of type hash / string called 'GameId'.

        try {
            // First set up the example by inserting a new item

            // To see different results, change either player's
            // starting positions to 20, or set player 1's location to 19.

        } catch (Exception e) {
            System.out.println("Failed to move player 1 because the game is over");
        }
    }


    public static final String APP_SECURITY_IV = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyGffCqoC1vCDLeBvjfuHdw4jo" +
            "hGvubOpQjEhhPzW1PbLSRKsNBLgj+eDGOiZE9BwmEwqy16sMOq0kMlhewTQlRrLJ" +
            "Nlw3L0iogs9WTIGm3el1SuZLyMnMksnV0NCsuq538cPMNppZRwARb7NXmpmh0KM7" +
            "9fJ/1xqnpo1tgRcv4wIDAQAB";

    public static <T> EncryptedEvent encryptEvent(T eventData, PublicKey userPublicKey) throws Exception {
        //event to Json string
        String secretMessage = JSON.toJSONString(eventData);
        log.info("JSON: {}" + secretMessage);
        //Generate symmetric key
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        log.info("Secret Key : {}", new String(secKey.getEncoded(), StandardCharsets.UTF_8));
        log.info("Secret Key (hex) : {}", Hex.encodeHexString(secKey.getEncoded()));

        //Encrypt json with symmetric key
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        log.info("Cipher block size : {}", cipher.getBlockSize());
        // ivParameterSpec.
        cipher.init(Cipher.ENCRYPT_MODE, secKey, new IvParameterSpec(APP_SECURITY_IV.getBytes(StandardCharsets.UTF_8)));
        byte[] byteCipherText = cipher.doFinal(secretMessage.getBytes());

        //Encrypt AES key by using RSA
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, userPublicKey);
        byte[] encryptedSecKey = encryptCipher.doFinal(Hex.encodeHexString(secKey.getEncoded()).getBytes(StandardCharsets.UTF_8));

        return EncryptedEvent.builder()
                .payload(Base64.getEncoder().encodeToString(byteCipherText))
                .key(Base64.getEncoder().encodeToString(encryptedSecKey))
                .build();
    }

//    public static <T extends EncryptedEvent, V> V decryptEvent(T eventData, PrivateKey privateKey, Class<V> classz) throws Exception {
//        Cipher decoderCipher = Cipher.getInstance("RSA");
//        decoderCipher.init(Cipher.DECRYPT_MODE, privateKey);
//        byte[] decryptedSecKey = decoderCipher.doFinal(Base64.getDecoder().decode(eventData.getKey()));
//        String secret =new String(decryptedSecKey, StandardCharsets.UTF_8);
//        log.info("Decrypted symmetric key  : {}", secret);
//        log.info("Decrypted symmetric key  length : {}", secret.length());
//        byte[] cipherData = Base64.getDecoder().decode(eventData.getPayload());
//        byte[] saltData = Arrays.copyOfRange(cipherData, 8, 16);
//        log.info("Salt : {}",Hex.encodeHexString(saltData));
//        MessageDigest md5 = MessageDigest.getInstance("MD5");
//        final byte[][] keyAndIV = generateKeyAndIV(32, 16, 1, saltData, secret.getBytes(StandardCharsets.UTF_8), md5);
//        SecretKeySpec key = new SecretKeySpec(keyAndIV[0], "AES");
//        IvParameterSpec iv = new IvParameterSpec(keyAndIV[1]);
//        log.info("IV : {}",Hex.encodeHexString(iv.getIV()));
//        byte[] encrypted = Arrays.copyOfRange(cipherData, 16, cipherData.length);
//        Cipher aesCBC = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
//        byte[] decryptedData = aesCBC.doFinal(encrypted);
//        String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);
//        return GSON.fromJson(decryptedText, classz);
//    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EncryptedEvent {
        @ApiModelProperty(required = false, example = "success", position = 49)
        private String payload;
        private String key;

        public static EncryptedEvent builder() {
            return new EncryptedEvent();
        }

        public EncryptedEvent payload(String payload) {
            this.setPayload(payload);
            return this;
        }

        public EncryptedEvent key(String key) {
            this.setKey(key);
            return this;
        }

        public EncryptedEvent build() {
            return new EncryptedEvent(this.payload, this.key);
        }
    }
}
