package com.kulun.kulunenergy.utils;

import android.util.Base64;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by dqn on 2019/4/17.<br>
 */
public class RSAUtils {
    public static final String CHARSET = "UTF-8";
    public static final String RSA_ALGORITHM = "RSA";
//    public static final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqa4doWOmRvGZdm/qxfBtaNDUFTbK0wCHkOo7sQvdmEt8TWLqRI91Z1heyeOHWIZk6FdfeRaOQVmJL1ZAkfkzGda+99B68Fk4fXjH2xvWO7K+V5D7aWfTJorXdLaRMmsoDa8fDCDc2oz4+NQ23oixH56w3y75yy44PyATFLXonSlfR0wYIOrc3jzLN54hvUxGfWFlgbrCoxlmFB5HAX1bFGChdOE8zSWef8ZrT3wG7m4CRSkYpxyZm/AafiH5HdQVarZQFcVOGVXP3StBfonHBUsajkIvm8j66IuqLXYhSwfRY/D+ydceJms72sjiUabuV+WCgs0LkuIuA0HucgJ1cwIDAQAB";
    public static final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgPncdjW4YHQJEgqG1ftE3DTqSI9cTCzfs7aouc8haz9_1IbwQP5vjQWVIFoqqSzkKU1HdbPh931rLB4OTGCPduReS_-urk8Xjr4nHdIKB9cjz5WehtRDf3IoPP0vQtS3wITAOCKLJ3Rduijc7g-aVzhA04HdBMkoVxebk3664qf22GsXHuWcX5AtUu7Qmy8N2i0oi4zd2mNaCbqzirnuafgpF0X6U_6tCJKcZyjLIK5powk_YHP0Sqru1RcSUveXY1Xmz9ZZg0XYIFAzphCl6fwo4vV-aZtIb11_yO4ALua0sJHkAJcGkC4K3FyVqWBEo3VcrsW9423EV2nnSyHdMwIDAQAB";
    /**
     * 得到公钥
     *
     * @param publicKey 密钥字符串（经过base64编码）
     */
    public static RSAPublicKey getPublicKey(String publicKey){
        //通过X509编码的Key指令获得公钥对象
        RSAPublicKey key = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decode(publicKey, Base64.NO_WRAP));
            key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        }catch (Exception e){
        }
        return key;
    }
    public static String getRSAString(String phone){
//        return RSAUtils.publicEncrypt(MD5.encode(phone), RSAUtils.getPublicKey(RSAUtils.publicKey));
        return RSAUtils.newEncrypt(MD5.encode(phone));
    }
    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
//        try {
            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            String string = Base64.encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()), Base64.DEFAULT);
            Log.d(Utils.TAG, "签名："+string);
            return string;
//        } catch (Exception e) {
//            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
//        }
    }
    public static String newEncrypt(String data){
        byte[] mi = null;
        try {
            byte[] keyBytes = Base64.decode(publicKey, Base64.NO_WRAP);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            mi = cipher.doFinal(data.getBytes());
        }catch (Exception e){
        }
        return Base64.encodeToString(mi, Base64.DEFAULT);
    }
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }
}
