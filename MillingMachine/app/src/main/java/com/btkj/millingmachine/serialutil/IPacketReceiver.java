package com.btkj.millingmachine.serialutil;

import android.content.Context;

public interface IPacketReceiver {
    int storeRcvByte(byte rcvByte, Context context) ;
}
