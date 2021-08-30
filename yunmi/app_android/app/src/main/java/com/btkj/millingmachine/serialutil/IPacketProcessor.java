package com.btkj.millingmachine.serialutil;

import android.content.Context;

public interface IPacketProcessor {
    boolean processPacket(PacketType packetType, int contentOffset, Context context);
}
