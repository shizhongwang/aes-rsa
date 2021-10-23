package com.refinitiv.collab.platform.aesrsa;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.refinitiv.collab.platform.aesrsa.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.TreeMap;

/**
 * Description: AES+RSA签名，加密 验签，解密
 *
 * @author: wubaoguo
 * @email: wustrive2008@gmail.com
 * @date: 2015/8/13 15:12
 */

@Log4j2
public class Main {
    public static final String clientPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKbNojYr8KlqKD/y" +
            "COd7QXu3e4TsrHd4sz3XgDYWEZZgYqIjVDcpcnlztwomgjMj9xSxdpyCc85GOGa0" +
            "lva1fNZpG6KXYS1xuFa9G7FRbaACoCL31TRv8t4TNkfQhQ7e2S7ZktqyUePWYLlz" +
            "u8hx5jXdriErRIx1jWK1q1NeEd3NAgMBAAECgYAws7Ob+4JeBLfRy9pbs/ovpCf1" +
            "bKEClQRIlyZBJHpoHKZPzt7k6D4bRfT4irvTMLoQmawXEGO9o3UOT8YQLHdRLitW" +
            "1CYKLy8k8ycyNpB/1L2vP+kHDzmM6Pr0IvkFgnbIFQmXeS5NBV+xOdlAYzuPFkCy" +
            "fUSOKdmt3F/Pbf9EhQJBANrF5Uaxmk7qGXfRV7tCT+f27eAWtYi2h/gJenLrmtke" +
            "Hg7SkgDiYHErJDns85va4cnhaAzAI1eSIHVaXh3JGXcCQQDDL9ns78LNDr/QuHN9" +
            "pmeDdlQfikeDKzW8dMcUIqGVX4WQJMptviZuf3cMvgm9+hDTVLvSePdTlA9YSCF4" +
            "VNPbAkEAvbe54XlpCKBIX7iiLRkPdGiV1qu614j7FqUZlAkvKrPMeywuQygNXHZ+" +
            "HuGWTIUfItQfSFdjDrEBBuPMFGZtdwJAV5N3xyyIjfMJM4AfKYhpN333HrOvhHX1" +
            "xVnsHOew8lGKnvMy9Gx11+xPISN/QYMa24dQQo5OAm0TOXwbsF73MwJAHzqaKZPs" +
            "EN08JunWDOKs3ZS+92maJIm1YGdYf5ipB8/Bm3wElnJsCiAeRqYKmPpAMlCZ5x+Z" +
            "AsuC1sjcp2r7xw==";

    public static final String clientPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCmzaI2K/Cpaig/8gjne0F7t3uE" +
            "7Kx3eLM914A2FhGWYGKiI1Q3KXJ5c7cKJoIzI/cUsXacgnPORjhmtJb2tXzWaRui" +
            "l2EtcbhWvRuxUW2gAqAi99U0b/LeEzZH0IUO3tku2ZLaslHj1mC5c7vIceY13a4h" +
            "K0SMdY1itatTXhHdzQIDAQAB";

    public static final String serverPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALIZ98KqgLW8IMt4" +
            "G+N+4d3DiOiEa+5s6lCMSGE/NbU9stJEqw0EuCP54MY6JkT0HCYTCrLXqww6rSQy" +
            "WF7BNCVGssk2XDcvSKiCz1ZMgabd6XVK5kvIycySydXQ0Ky6rnfxw8w2mllHABFv" +
            "s1eamaHQozv18n/XGqemjW2BFy/jAgMBAAECgYAxT3FCi3SBXKnzy7hk/z9H6Bhi" +
            "0C8V3z/stzpe+mJDYOa+wtZdD15wT4HFQFpSIwgcHo+Kvp2UEDbZ27qN2Y43AZbF" +
            "9LOalWTRUzYtr8wL8MIbgtew/QQ9YFNWdkTZ6MxCItjD/mSz3Lrkcphvbsx4VoCV" +
            "YIJ04r+Loi0t9g0guQJBANvkpfrq0bLVRYWfaigjkx47mr0trJkB7mjADe69Iqts" +
            "M/2x5dHPpClDK78yzAWxU2BrYzOd31QIOm32iMIvRxUCQQDPWJPMOzcq8Jqs1PAM" +
            "7D0hxnvF3tSJB0CJCQWdGFkJiuIYSbrWnCVF78jJyU2AK1H3RDi9BzGPL2Z3i2Si" +
            "+9kXAkAPnKtAJl3fEY9PDmNuGCCA3AB/f/eqIV345/HVSm5kt1j1oSTNAa4JE/DO" +
            "MWAU42MlDFrNtl69y5vCZOeOyeaFAkBOJieGmWcAozDZJWTYqg2cdk/eU08t2nLj" +
            "c2gPPscIRrVSzC9EhhOyWV8HVv0D6s/471inPlfajNYFBp/Goj+/AkEAiejHX/58" +
            "Vv8+ccW22RMZmyxiHcZpTw9hz7vHUCWv03+fyVGtGMhJ4xuPt8UaZm91yHSPWWar" +
            "M8Xa7errKaXN9A==";
    public static final String serverPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCyGffCqoC1vCDLeBvjfuHdw4jo" +
            "hGvubOpQjEhhPzW1PbLSRKsNBLgj+eDGOiZE9BwmEwqy16sMOq0kMlhewTQlRrLJ" +
            "Nlw3L0iogs9WTIGm3el1SuZLyMnMksnV0NCsuq538cPMNppZRwARb7NXmpmh0KM7" +
            "9fJ/1xqnpo1tgRcv4wIDAQAB";

    public static void main(String[] args) throws Exception {
        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("userid", "152255855");
        params.put("phone", "随着Internet网的广泛应用，信息安全问题日益突出，以数据加密技术为核心的信息安全技术也得到了极大的发展。 " +
                "目前的数据加密技术根据加密密钥类型可分私钥加密（对称加密）系统和公钥加密（非对称加密）系统。对称加密算法是较传统的加密体制， " +
                "通信双方在加/解密过程中使用他们共享的单一密钥，鉴于其算法简单和加密速度快的优点，目前仍然是主流的密码体制之一。 最常用的对称" +
                "密码算法是数据加密标准（DES）算法，但是由于DES密钥长度较短，已经不适合当今分布式开放网络对数据加密安全性的要求。 最后，一种新" +
                "的基于Rijndael算法对称高级数据加密标准AES取代了数据加密标准DES。 非对称加密由于加/解密钥不同（公钥加密，私钥解密），密钥管" +
                "理简单，也得到广泛应用。RSA是非对称加密系统最著名的公钥密码算法。");

        client(params);

        server();
    }

    public static void client(TreeMap<String, Object> params) throws Exception {
        // 生成RSA签名
        String sign = EncryUtil.handleRSA(params, clientPrivateKey);
        params.put("sign", sign);

        String info = JSON.toJSONString(params);
        //随机生成AES密钥
        String aesKey = SecureRandomUtil.getRandom(16);
        //AES加密数据
        String data = AES.encryptToBase64(ConvertUtils.stringToHexString(info), aesKey);

        // 使用RSA算法将商户自己随机生成的AESkey加密
        String encryptkey = RSA.encrypt(aesKey, serverPublicKey);

        EncryptedData encryptedData = EncryptedData.builder()
                .encryptKey(encryptkey)
                .payload(data)
                .build();
        log.info("加密后的请求数据:\n" + JSON.toJSONString(encryptedData));

//        Req.data = data;
//        Req.encryptkey = encryptkey;
//
//
//        System.out.println("加密后的请求数据:\n" + new Req().toString());
    }

    public static void server() throws Exception {

        // 验签
        boolean passSign = EncryUtil.checkDecryptAndSign(Req.data,
                Req.encryptkey, clientPublicKey, serverPrivateKey);

        if (passSign) {
            // 验签通过
            String aeskey = RSA.decrypt(Req.encryptkey,
                    serverPrivateKey);
            String data = ConvertUtils.hexStringToString(AES.decryptFromBase64(Req.data,
                    aeskey));

            JSONObject jsonObj = JSONObject.parseObject(data);
            System.out.println("解密后的明文:" + jsonObj.toJSONString());
//
//            String userid = jsonObj.getString("userid");
//            String phone = jsonObj.getString("phone");
//
//            System.out.println("解密后的明文:userid:" + userid + " phone:" + phone);

        } else {
            System.out.println("验签失败");
        }
    }

    static class Req {
        public static String data;
        public static String encryptkey;

        @Override
        public String toString() {
            return "data:" + data + "\nencryptkey:" + encryptkey;
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
