package com.btkj.millingmachine.serialportutil;
/**
 * Created by Administrator on 2018/1/13.
 */

public class RiceUtil {
    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    public static String bytesToHexString(byte[] src, int size) {
        String ret = "";
        if (src == null || size <= 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            String hex = Integer.toHexString(src[i] & 0xFF);
            if (hex.length() < 2) {
                hex = "0" + hex;
            }
//            hex += " ";
            ret += hex;
        }
        return ret.toUpperCase();
    }

    public static int byteArrayToInt(byte[] tmpByte) {
        return tmpByte[3] << 24 + tmpByte[2] << 16 + tmpByte[1] << 8 + tmpByte[0];
    }

    public static int toInt32(byte[] bytes, int index) {
        int a = (int) ((int) (0xff & bytes[index]) << 32 | (int) (0xff & bytes[index + 1]) << 40 | (int) (0xff & bytes[index + 2]) << 48 | (int) (0xff & bytes[index + 3]) << 56);
        return a;
    }

    public static long toUInt32(byte[] res) {
        int firstByte = 0;
        int secondByte = 0;
        int thirdByte = 0;
        int fourthByte = 0;
        int index = 0;
        firstByte = (0x000000FF & ((int) res[index]));
        secondByte = (0x000000FF & ((int) res[index + 1]));
        thirdByte = (0x000000FF & ((int) res[index + 2]));
        fourthByte = (0x000000FF & ((int) res[index + 3]));
        index = index + 4;
        return ((long) (firstByte | secondByte << 8 | thirdByte << 16 | fourthByte << 24)) & 0xFFFFFFFFL;
    }

    /***
     * 主控制板命令加校验字节
     * @param data
     * @return
     */
    public static byte[] convertCmd(byte[] data) {
        try {
            byte[] buffer = new byte[data.length + 1];
            for (int i = 0; i < data.length; i++) {
                buffer[i] = data[i];
                buffer[data.length] = (byte) (buffer[data.length] ^ data[i]);
            }
            return buffer;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * int转byte数组
     * @param bytes
     * @return
     */
    public static byte[] IntToByte(int num){
        byte[]bytes=new byte[1];
//        bytes[0]=(byte) ((num>>24)&0xff);
//        bytes[1]=(byte) ((num>>16)&0xff);
//        bytes[2]=(byte) ((num>>8)&0xff);
        bytes[0]=(byte) (num&0xff);
        return bytes;
    }

    public static byte[] getbytes(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
}
