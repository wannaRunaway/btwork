package com.btkj.millingmachine.serialutil;

import android.content.Context;

import com.btkj.millingmachine.util.Utils;

import java.util.concurrent.atomic.AtomicInteger;

public abstract  class AbsPacketDecomposer extends Packet implements IPacketDecomposer, IPacketReceiver {
    public final static int MAX_PACKET_LEN = 512;
    private final byte[] in_packet = new byte[MAX_PACKET_LEN];
    private AtomicInteger g_packet_rcv_data_offset = new AtomicInteger();
    private AtomicInteger g_packet_rcv_data_len = new AtomicInteger();

    private int getIndex(int next) {
        return g_packet_rcv_data_offset.addAndGet(next) % MAX_PACKET_LEN;
    }

    private int findPacketValidOffset() {
        int validPacketOffset = -1;
        int i = g_packet_rcv_data_offset.get();
        for (int j=0; j < g_packet_rcv_data_len.get(); j++){
            int next_i = (i+1)%MAX_PACKET_LEN ;
            if (in_packet[i] == P_HEAD[0] && in_packet[next_i] == P_HEAD[1]) {
                validPacketOffset = i;
            }
            i = next_i;
        }

        return validPacketOffset;
    }

    private synchronized void reviseOffsetAdd(int reviseLen){
        int updateValue = this.g_packet_rcv_data_len.addAndGet(reviseLen)%MAX_PACKET_LEN;
        this.g_packet_rcv_data_len.compareAndSet(this.g_packet_rcv_data_len.get(), updateValue);
    }

    private synchronized void reviseOffsetMinus(int reviseLen){
        int updateValue = this.g_packet_rcv_data_offset.addAndGet(reviseLen)%MAX_PACKET_LEN;
        this.g_packet_rcv_data_offset.compareAndSet(this.g_packet_rcv_data_offset.get(), updateValue);
        updateValue = this.g_packet_rcv_data_len.addAndGet(0-reviseLen)%MAX_PACKET_LEN;
        this.g_packet_rcv_data_len.compareAndSet(this.g_packet_rcv_data_len.get(), updateValue);
    }

    public PacketType decodePacketType(int offset) {
        if (offset>=0) {
            int cmd = ((in_packet[(offset)%MAX_PACKET_LEN]<<8)&0xff00) | (in_packet[(offset+1)%MAX_PACKET_LEN]&0xff);
            if (cmd == PacketType.D_CON_REQ.getCmd()) {
                return PacketType.D_CON_REQ;
            }
            else if (cmd == PacketType.D_UPDATE__ACK_REQ.getCmd()) {
                return PacketType.D_UPDATE__ACK_REQ;
            }
            else if (cmd == PacketType.D_LOAD_DATA_REQ.getCmd()) {
                return PacketType.D_LOAD_DATA_REQ;
            }
            else if (cmd == PacketType.D_UPDATE_RESULT.getCmd()) {
                return PacketType.D_UPDATE_RESULT;
            }
        }
        return null;
    }

    protected int decodePacketByte( int offset) {
        if (offset>=0) {
            return  ((in_packet[offset%MAX_PACKET_LEN] << 0) & 0xff) ;
        }
        return 0;
    }

    protected int decodePacketShort( int offset) {
        if (offset>=0) {
            return  ((in_packet[offset%MAX_PACKET_LEN] << 8) & 0xff00) | (in_packet[(offset + 1)%MAX_PACKET_LEN]<<0 & 0xff);
        }
        return 0;
    }

    protected int decodePacketInt( int offset) {
        if (offset>=0) {
            return  ((in_packet[(offset+0)%MAX_PACKET_LEN] << 24) & 0xff000000) | ((in_packet[(offset+1)%MAX_PACKET_LEN] << 16) & 0xff0000)
                    | ((in_packet[(offset+2)%MAX_PACKET_LEN] << 8) & 0xff00) | (in_packet[(offset + 3)%MAX_PACKET_LEN] & 0xff);
        }
        return 0;
    }


    @Override
    public PacketType packetDecomposite(Context context) {
        int validStartOffset = this.findPacketValidOffset();
        if (-1 == validStartOffset) {
            return  null;
        }
        else {
            Utils.log("Packet start found:" + validStartOffset);
        }

        if (!this.isPacketLenRcved(validStartOffset,  Packet.P_HEAD_LEN)) {
            Utils.log("Packet head rcving... , validStartOffset:" + validStartOffset + ", rcvLen:" + this.g_packet_rcv_data_len.get());
            return null;
        }

        PacketType packetType = this.decodePacketType(validStartOffset + 7);
        int packetLen = this.decodePacketShort(validStartOffset + 3);
        int seq = this.decodePacketShort(validStartOffset + 5);
        if (packetType == PacketType.D_CON_REQ) {
            Packet.P_D_SEQ.compareAndSet(Packet.P_D_SEQ.get(), seq);
        }

        if (checkPacket(validStartOffset)) {
            int packetDataOffset = validStartOffset + 7 + 4;
            this.processPacket(packetType, packetDataOffset,context);
            Packet.P_D_SEQ.incrementAndGet();
            Utils.log("in_packet offset b:" + this.g_packet_rcv_data_offset.get() + ", len:" + this.g_packet_rcv_data_len);
            this.reviseOffsetMinus(this.packetOffsetMinus(validStartOffset, this.g_packet_rcv_data_offset.get())+packetLen);
            Utils.log("in_packet offset e:" + this.g_packet_rcv_data_offset.get() + ", len:" + this.g_packet_rcv_data_len);
            UserArrayUtils.hexDumpArray(in_packet);
        }

        return packetType;
    }

    abstract boolean processPacket(PacketType packetType, int contentOffset, Context context);

    private int packetOffsetMinus(int validStartOffset, int rawStartOffset){
        if (validStartOffset >= MAX_PACKET_LEN || rawStartOffset>= MAX_PACKET_LEN) {
            throw new ArrayIndexOutOfBoundsException("in_packet array offset out of bound: validOffset"+validStartOffset + ",rawOffset:" + rawStartOffset);
        }
        if (validStartOffset >= rawStartOffset) {
            return validStartOffset-rawStartOffset;
        }
        return MAX_PACKET_LEN+validStartOffset-rawStartOffset;
    }

    private boolean isPacketLenRcved(int startOffset, int len) {
        if (startOffset >= this.g_packet_rcv_data_offset.get()) {
            if (startOffset+len <= this.g_packet_rcv_data_offset.get()+this.g_packet_rcv_data_len.get()) {
                return true;
            }
        }
        else {
            if (startOffset+len+MAX_PACKET_LEN <= this.g_packet_rcv_data_offset.get()+this.g_packet_rcv_data_len.get()) {
                return true;
           }
        }
        return false;
    }

    private boolean checkPacket( int validStartOffset) {
        int packetSeq = this.decodePacketShort(validStartOffset + 5);
        int packetLen = this.decodePacketShort(validStartOffset + 3);
        if (!this.isPacketLenRcved(validStartOffset, packetLen)) {
            Utils.log("Rcving packet, packet expectted len: " + packetLen + ", System rcved len:" + this.g_packet_rcv_data_len.get());
            return false;
        }

        if (packetSeq != Packet.P_D_SEQ.get()) {
            Utils.log("Rcv packet seq error, packet: " + packetSeq + ", System:" + Packet.P_D_SEQ.get());
            return false;
        }

        byte crc = in_packet[(packetLen+validStartOffset-3)%MAX_PACKET_LEN];
        byte calcCrc = this.calcPacketCrc(in_packet, validStartOffset+2, packetLen-5);
        if (crc != calcCrc) {
            Utils.log("Rcv packet crc error, packet: " + crc + ", System:" + calcCrc);
            return false;
        }

        return true;
    }

    @Override
    public int storeRcvByte(byte rcvByte, Context context) {
       this.in_packet[(this.g_packet_rcv_data_offset.get() + this.g_packet_rcv_data_len.get())%MAX_PACKET_LEN] = rcvByte;
       this.g_packet_rcv_data_len.incrementAndGet();
       this.packetDecomposite(context);
       return 1;
    }
}
