package com.btkj.millingmachine.serialutil;

import com.btkj.millingmachine.util.Utils;

public class UserArrayUtils {
    public static  int array_copy(byte[] des, int des_offset, byte[] src) {
        int i= 0;
        if (des != null && src != null) {
            for ( i=0; i<src.length; i++) {
               int curr_des_index = des_offset + i;
               if (curr_des_index >= des.length) {
                   break;
               }
               des[curr_des_index] = src[i];
            }
        }
        return i;
    }

    public static  int array_copy(byte[] des, int des_offset, byte[] src, int src_offset, int len) {
        int i= 0;
        if (des != null && src != null) {
            for ( i=0; i<src.length; i++) {
               int curr_des_index = des_offset + i;
               int curr_src_index = src_offset + i;
               if (curr_des_index >= des.length || curr_src_index >= src.length || i >= len) {
                   break;
               }
               des[curr_des_index] = src[curr_src_index];
            }
        }
        return i;
    }

    public static  int array_copy(byte[] des, int des_offset, byte src) {
        int i= 0;
        if (des != null) {
            for ( i=0; i<1; i++) {
                int curr_des_index = des_offset + i;
                if (curr_des_index >= des.length) {
                    break;
                }
                des[curr_des_index] = src;
            }
        }
        return i;
    }

    public static  int array_copy(byte[] des, int des_offset, int src, int len) {
        int i= 0;
        if (des != null && len <= Integer.BYTES) {
            for ( i=0; i<len; i++) {
                int curr_des_index = des_offset + i;
                if (curr_des_index >= des.length) {
                    break;
                }
                des[curr_des_index] = (byte) ((src>>8*(len-i-1))&0xff);
            }
        }
        return i;
    }

    public static String hexDumpArray(byte[] data) {
        StringBuffer sb = new StringBuffer();
        if (data != null) {
            for (int i=0; i<data.length; i++) {
//                System.out.print(String.format("%02x ", data[i]));
                sb.append(String.format("%02x", data[i]));
            }
//            Utils.log(String.format("%02x ", data[i]));
//            System.out.println();
            Utils.log(sb.toString());
        }
        return sb.toString();
    }

}
