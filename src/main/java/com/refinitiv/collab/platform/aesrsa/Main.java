package com.refinitiv.collab.platform.aesrsa;

import com.alibaba.fastjson.JSON;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.refinitiv.collab.platform.aesrsa.util.RSA;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.sl.usermodel.ObjectMetaData;
import org.apache.tomcat.jni.Time;

import java.util.Map;
import java.util.TreeMap;

@Log4j2
public class Main {
    public static void main(String[] args) throws Exception {
        TreeMap<String, Object> inputs = buildInput();
        RSAKeys rsaKeys;

        //generate new ras keypair for each round
        //encrypt and decrypt the same input
        //check the data
        rsaKeys = RSAKeys.generate();
        E2EEncrypt.server(E2EEncrypt.client(inputs, rsaKeys.cliSK, rsaKeys.svrPK), rsaKeys.cliPK, rsaKeys.svrSK);

        rsaKeys = RSAKeys.generate();
        inputs.remove("sign");
        E2EEncrypt.server(E2EEncrypt.client(inputs, rsaKeys.cliSK, rsaKeys.svrPK), rsaKeys.cliPK, rsaKeys.svrSK);

        rsaKeys = RSAKeys.generate();
        inputs.remove("sign");
        E2EEncrypt.server(E2EEncrypt.client(inputs, rsaKeys.cliSK, rsaKeys.svrPK), rsaKeys.cliPK, rsaKeys.svrSK);


        //generate new input for each round
        //using the same rsa keypair to en&de
        E2EEncrypt.server(E2EEncrypt.client(buildInput(), clientPrivateKey, serverPublicKey), clientPublicKey, serverPrivateKey);

        Thread.sleep(1000);
        E2EEncrypt.server(E2EEncrypt.client(buildInput(), clientPrivateKey, serverPublicKey), clientPublicKey, serverPrivateKey);

        Thread.sleep(1000);
        E2EEncrypt.server(E2EEncrypt.client(buildInput(), clientPrivateKey, serverPublicKey), clientPublicKey, serverPrivateKey);

        Thread.sleep(1000);
        E2EEncrypt.server(E2EEncrypt.client(buildInput(), clientPrivateKey, serverPublicKey), clientPublicKey, serverPrivateKey);

    }

    private static TreeMap<String, Object> buildInput() {
        Faker faker = new Faker();
        UserInfo userInfo = new UserInfo();
        userInfo.setRealName(faker.name().fullName());
        userInfo.setCellPhone(faker.phoneNumber().cellPhone());
        userInfo.setCity(faker.address().city());
        userInfo.setStreet(faker.address().streetAddress());
        userInfo.setUniversityName(faker.university().name());
        String jsonData = JSON.toJSONString(userInfo);

        TreeMap<String, Object> params = new TreeMap<String, Object>();
        params.put("userid", "152255855");
        params.put("data", jsonData);

//        params.put("data", "随着Internet网的广泛应用，信息安全问题日益突出，以数据加密技术为核心的信息安全技术也得到了极大的发展。 " +
//                "目前的数据加密技术根据加密密钥类型可分私钥加密（对称加密）系统和公钥加密（非对称加密）系统。对称加密算法是较传统的加密体制， " +
//                "通信双方在加/解密过程中使用他们共享的单一密钥，鉴于其算法简单和加密速度快的优点，目前仍然是主流的密码体制之一。 最常用的对称" +
//                "密码算法是数据加密标准（DES）算法，但是由于DES密钥长度较短，已经不适合当今分布式开放网络对数据加密安全性的要求。 最后，一种新" +
//                "的基于Rijndael算法对称高级数据加密标准AES取代了数据加密标准DES。 非对称加密由于加/解密钥不同（公钥加密，私钥解密），密钥管" +
//                "理简单，也得到广泛应用。RSA是非对称加密系统最著名的公钥密码算法。");

        return params;
    }

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


    @Data
    public static class UserInfo {
        private String realName;
        private String cellPhone;
        private String universityName;
        private String city;
        private String street;
    }

    @Data
    public static class RSAKeys {
        private String cliPK;
        private String cliSK;
        private String svrPK;
        private String svrSK;

        private static String PUBKEY = "publicKey";
        private static String PRIKEY = "privateKey";

        public static RSAKeys generate() throws Exception {
            RSAKeys rsaKeys = new RSAKeys();

            Map<String, String> rsaMap = RSA.generateKeyPair();
            rsaKeys.cliPK = rsaMap.get(PUBKEY);
            rsaKeys.cliSK = rsaMap.get(PRIKEY);

            Map<String, String> rsaMap2 = RSA.generateKeyPair();
            rsaKeys.svrPK = rsaMap2.get(PUBKEY);
            rsaKeys.svrSK = rsaMap2.get(PRIKEY);

            return rsaKeys;
        }
    }

}
