package com.btkj.millingmachine.serialutil;

import java.util.concurrent.atomic.AtomicInteger;

public abstract  class Packet implements IPacket {
    public final static byte[] P_HEAD  = {(byte) 0xea, 0x61};
    public final static byte[] P_TAIL = { 0x62, (byte) 0xeb};
    public final static byte P_VERSION=  0x00 ;
    public final static int P_HEAD_LEN = 7+4;
    public final static AtomicInteger P_U_SEQ = new AtomicInteger(0);
    public final static AtomicInteger P_D_SEQ = new AtomicInteger(0);
    private final ICrcFactory<Byte> crc = new CrcFactoryImpl();

    @Override
    public byte calcPacketCrc(byte[] packet, int startOffset, int len) {
        return crc.crc(packet, startOffset, len);
    }
}
