package com.btkj.millingmachine.serialutil;

import java.lang.Byte;

public class CrcFactoryImpl implements ICrcFactory<Byte> {
    private final static int CRC8_TABLE_LEN = 256;
    private final static byte P_8 = (byte) 0xd5;
    private final static byte[] crc8_table = new byte[CRC8_TABLE_LEN];

    static {
        init_crc8_tab();
    }

    @Override
    public Byte crc(byte[] data, int startOffset, int len) {
        byte crc = 0 ;
        int data_len = 0;
        if (data != null && len <= data.length) { data_len = len; }

       for (int i=0; i< data_len; i++) {
           crc = update_crc8_ex( crc, data[(startOffset+i)%data.length]);
       }

        return crc;
    }

    private byte update_crc8_ex(byte crc, byte c) {
        return (byte) ((crc^c)&0xff);
    }

    private byte update_crc8(byte crc, byte c) {
        return (crc8_table[(crc^c)&0xff]);
    }

    private static void init_crc8_tab(){
        init_crc8_nomal_tab();
    }

    private static void init_crc8_nomal_tab() {
        int i,j;
        byte crc;

        for (i=0; i<CRC8_TABLE_LEN; i++) {
           crc = (byte) i;
            for (j=0; j<8; j++) {
                if ((crc & 0x80) != 0) {
                    crc = (byte) ((crc<<1) ^ P_8);
                }
                else {
                    crc <<= 1;
                }
            }
            crc8_table[i] = crc;
        }
    }
}
