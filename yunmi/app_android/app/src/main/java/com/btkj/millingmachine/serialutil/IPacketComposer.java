package com.btkj.millingmachine.serialutil;

public interface IPacketComposer {
    int packetComposite(PacketType type, int contentOffset, int contentLen);
    byte[] getOut_packet();
}
