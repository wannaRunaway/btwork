package com.btkj.millingmachine.serialutil;

public interface IPacket {
    byte calcPacketCrc(byte[] packet, int startOffset, int len);
}
