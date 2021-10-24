package com.refinitiv.collab.platform.aesrsa;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.refinitiv.collab.platform.aesrsa.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.Instant;
import java.util.TreeMap;

@Log4j2
public class E2EEncrypt {
    public static EncryptedData client(TreeMap<String, Object> params, String clientPrivateKey, String serverPublicKey) throws Exception {
        Instant instantStart = Instant.now();

        // 生成RSA签名, insert into input data
        String sign = EncryUtil.handleRSA(params, clientPrivateKey);
        params.put("sign", sign);

        String jsonData = JSON.toJSONString(params);
        //随机生成AES密钥
        String aesKey = SecureRandomUtil.getRandom(16);
        //AES加密数据
        String data = AES.encryptToBase64(ConvertUtils.stringToHexString(jsonData), aesKey);

        // 使用RSA算法将商户自己随机生成的AESkey加密
        String encryptkey = RSA.encrypt(aesKey, serverPublicKey);

        EncryptedData encryptedData = EncryptedData.builder()
                .encryptKey(encryptkey)
                .payload(data)
                .build();

        String jsonString = JSON.toJSONString(encryptedData);
        String dataEn = String.format("%s, %s", jsonString.length(), jsonString);
        log.info("DataEn:\n" + dataEn);
        log.info("TimeEn: " + Duration.between(instantStart, Instant.now()).toMillis());
        return encryptedData;
    }

    public static void server(EncryptedData encryptedData, String clientPublicKey, String serverPrivateKey) throws Exception {
        Instant instantStart = Instant.now();

        // 验签
        boolean passSign = EncryUtil.checkDecryptAndSign(encryptedData.payload,
                encryptedData.encryptKey, clientPublicKey, serverPrivateKey);

        if (passSign) {
            // 验签通过
            String aeskey = RSA.decrypt(encryptedData.encryptKey,
                    serverPrivateKey);
            String data = ConvertUtils.hexStringToString(AES.decryptFromBase64(encryptedData.payload,
                    aeskey));

            JSONObject jsonObj = JSONObject.parseObject(data);
            jsonObj.remove("sign");

            String jsonString = JSON.toJSONString(jsonObj);
            String dataEn = String.format("%s, %s", jsonString.length(), jsonString);
            log.info("DataDe:\n" + dataEn);
            log.info("TimeDe: " + Duration.between(instantStart, Instant.now()).toMillis());
            log.info("\n");
        } else {
            log.info("验签失败");
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EncryptedData {
        private String payload;
        private String encryptKey;

        public static EncryptedData builder() {
            return new EncryptedData();
        }

        public EncryptedData payload(String payload) {
            this.setPayload(payload);
            return this;
        }

        public EncryptedData encryptKey(String key) {
            this.setEncryptKey(key);
            return this;
        }

        public EncryptedData build() {
            return new EncryptedData(this.payload, this.encryptKey);
        }

        @Override
        public String toString() {
            return "payload:" + payload + "\nencryptkey:" + encryptKey;
        }
    }
}
