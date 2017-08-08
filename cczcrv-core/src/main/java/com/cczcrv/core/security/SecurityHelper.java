package com.cczcrv.core.security;

import com.cczcrv.core.security.coder.*;
import com.cczcrv.core.utils.StringHelper;

import java.security.MessageDigest;
import java.util.Map;

/**
 * Created by liuxx on 2017/5/16.
 */
public final class SecurityHelper {
    private SecurityHelper() {
    }

    /**
     * 默认算法密钥
     */
    private static final byte[] ENCRYPT_KEY = { -81, 0, 105, 7, -32, 26, -49, 88 };

    public static final String CHARSET = "UTF-8";

    /**
     * BASE64解码(Url安全)
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static final byte[] decryptBASE64(String key) {
        try {
            String result=pad(key.replace('-','+').replace('_','/'));
            return new BASE64Encoder().decode(result);
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
    }

    /**
     * BASE64编码(Url安全)
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static final String encryptBASE64(byte[] key) {
        try {
            String result= new BASE64Encoder().encode(key);
            return StringHelper.trimEnd(result.replace('+','-').replace('/','_'),'=');
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
    }

    private static String pad(String text){
        int num = 3 - (text.length() + 3) % 4;
        if (num == 0)
        {
            return text;
        }
        for (int i=0;i<num;i++) {
            text += "=";
        }
        return text;
    }

    /**
     * 数据解密，算法（DES）
     *
     * @param cryptData
     *            加密数据
     * @return 解密后的数据
     */
    public static final String decryptDes(String cryptData) {
        return decryptDes(cryptData, ENCRYPT_KEY);
    }

    /**
     * 数据加密，算法（DES）
     *
     * @param data
     *            要进行加密的数据
     * @return 加密后的数据
     */
    public static final String encryptDes(String data) {
        return encryptDes(data, ENCRYPT_KEY);
    }

    /**
     * 基于MD5算法的单向加密
     *
     * @param strSrc
     *            明文
     * @return 返回密文
     */
    public static final String encryptMd5(String strSrc) {
        String outString = null;
        try {
            outString = encryptBASE64(MDCoder.encodeMD5(strSrc.getBytes(CHARSET)));
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return outString;
    }
    public final static String MD5(String pwd) {
        //用于加密的字符
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = pwd.getBytes();

            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);

            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }

            //返回经过加密后的字符串
            return new String(str);

        } catch (Exception e) {
            return null;
        }
    }
    /**
     * SHA加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static final String encryptSHA(String data) {
        try {
            return encryptBASE64(SHACoder.encodeSHA256(data.getBytes(CHARSET)));
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
    }

    /**
     * HMAC加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static final String encryptHMAC(String data) {
        return encryptHMAC(data, ENCRYPT_KEY);
    }

    /**
     * 数据解密，算法（DES）
     *
     * @param cryptData
     *            加密数据
     * @return 解密后的数据
     */
    public static final String decryptDes(String cryptData, byte[] key) {
        String decryptedData = null;
        try {
            // 把字符串解码为字节数组，并解密
            decryptedData = new String(DESCoder.decrypt(decryptBASE64(cryptData), key));
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
        return decryptedData;
    }

    /**
     * 数据加密，算法（DES）
     *
     * @param data
     *            要进行加密的数据
     * @return 加密后的数据
     */
    public static final String encryptDes(String data, byte[] key) {
        String encryptedData = null;
        try {
            // 加密，并把字节数组编码成字符串
            encryptedData = encryptBASE64(DESCoder.encrypt(data.getBytes(CHARSET), key));
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
        return encryptedData;
    }

    /**
     * HMAC加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static final String encryptHMAC(String data, byte[] key) {
        try {
            return encryptBASE64(HmacCoder.encodeHmacSHA512(data.getBytes(CHARSET), key));
        } catch (Exception e) {
            throw new RuntimeException("加密错误，错误信息：", e);
        }
    }

    /**
     * RSA签名
     *
     * @param data
     *            原数据
     * @return
     */
    public static final String signRSA(String data, String privateKey) {
        try {
            return encryptBASE64(RSACoder.sign(data.getBytes(CHARSET), decryptBASE64(privateKey)));
        } catch (Exception e) {
            throw new RuntimeException("签名错误，错误信息：", e);
        }
    }

    /**
     * RSA验签
     *
     * @param data
     *            原数据
     * @return
     */
    public static final boolean verifyRSA(String data, String publicKey, String sign) {
        try {
            return RSACoder.verify(data.getBytes(CHARSET), decryptBASE64(publicKey), decryptBASE64(sign));
        } catch (Exception e) {
            throw new RuntimeException("验签错误，错误信息：", e);
        }
    }

    /**
     * 数据加密，算法（RSA）
     *
     * @param data
     *            数据
     * @return 加密后的数据
     */
    public static final String encryptRSAPrivate(String data, String privateKey) {
        try {
            return encryptBASE64(RSACoder.encryptByPrivateKey(data.getBytes(CHARSET), decryptBASE64(privateKey)));
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
    }

    /**
     * 数据解密，算法（RSA）
     *
     * @param cryptData
     *            加密数据
     * @return 解密后的数据
     */
    public static final String decryptRSAPublic(String cryptData, String publicKey) {
        try {
            // 把字符串解码为字节数组，并解密
            return new String(RSACoder.decryptByPublicKey(decryptBASE64(cryptData), decryptBASE64(publicKey)));
        } catch (Exception e) {
            throw new RuntimeException("解密错误，错误信息：", e);
        }
    }

    public static String encryptPassword(String password) {
        return encryptMd5(SecurityHelper.encryptSHA(password));
    }

    public static void main(String[] args) throws Exception {
        System.out.println(encryptDes("SHJR"));
        System.out.println(decryptDes("INzvw/3Qc4q="));
        System.out.println(encryptMd5("SHJR"));
        System.out.println(encryptSHA("1"));
        Map<String, Object> key = RSACoder.initKey();
        String privateKey = encryptBASE64(RSACoder.getPrivateKey(key));
        String publicKey = encryptBASE64(RSACoder.getPublicKey(key));
        System.out.println(privateKey);
        System.out.println(publicKey);
        String sign = signRSA("132", privateKey);
        System.out.println(sign);
        String encrypt = encryptRSAPrivate("132", privateKey);
        System.out.println(encrypt);
        String org = decryptRSAPublic(encrypt, publicKey);
        System.out.println(org);
        System.out.println(verifyRSA(org, publicKey, sign));

        // System.out.println("-------列出加密服务提供者-----");
        // Provider[] pro = Security.getProviders();
        // for (Provider p : pro) {
        // System.out.println("Provider:" + p.getName() + " - version:" +
        // p.getVersion());
        // System.out.println(p.getInfo());
        // }
        // System.out.println("");
        // System.out.println("-------列出系统支持的消息摘要算法：");
        // for (String s : Security.getAlgorithms("MessageDigest")) {
        // System.out.println(s);
        // }
        // System.out.println("-------列出系统支持的生成公钥和私钥对的算法：");
        // for (String s : Security.getAlgorithms("KeyPairGenerator")) {
        // System.out.println(s);
        // }
    }
}
