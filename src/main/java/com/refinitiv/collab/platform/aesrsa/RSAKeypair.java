package com.refinitiv.collab.platform.aesrsa;

import com.refinitiv.collab.platform.aesrsa.util.RSA;
import lombok.Data;

import java.util.Map;

@Data
public class RSAKeypair {
    private String privateKey;
    private String publicKey;

    private static String PUBKEY = "publicKey";
    private static String PRIKEY = "privateKey";

    public static RSAKeypair build() throws Exception {
        RSAKeypair rsaKeypair = new RSAKeypair();
        Map<String, String> rsaMap = RSA.generateKeyPair();
        rsaKeypair.setPrivateKey(rsaMap.get(PRIKEY));
        rsaKeypair.setPublicKey(rsaMap.get(PUBKEY));
        return rsaKeypair;
    }
}

