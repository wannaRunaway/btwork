package com.btkj.millingmachine.serialutil;

import com.btkj.millingmachine.util.Utils;

public class PacketComposerImpl extends Packet implements IPacketComposer {
    private final byte[] out_packet = new byte[1024];
    private int out_packet_len = 0;
    private final static int PACKET_CONTENT_OFFSET = 7;

    private int setPacketContent(PacketType packetType, int contentOffset, int contentLen) {
       int i = 0;

        i = UserArrayUtils.array_copy(out_packet, PACKET_CONTENT_OFFSET, packetType.getCmd(), 2) ;
       switch (packetType) {
           case U_CON_ACK:
               i += UserArrayUtils.array_copy(out_packet, PACKET_CONTENT_OFFSET+i, 0x0004, 2);
               break;
           case U_UPDATE_REQ:
               i += UserArrayUtils.array_copy(out_packet, PACKET_CONTENT_OFFSET+i, 0x0004, 2);
               break;
           case U_UPDATE_ACK_PROFILE:
               i += UserArrayUtils.array_copy(out_packet, PACKET_CONTENT_OFFSET+i, 14+0x0004, 2);
               i += UserArrayUtils.array_copy(out_packet, PACKET_CONTENT_OFFSET+i, FileInfo.getInstance().getFileLen(), 4);
               i += UserArrayUtils.array_copy(out_packet, PACKET_CONTENT_OFFSET+i, FileInfo.getInstance().getFileBlockLen(), 4);
               i += UserArrayUtils.array_copy(out_packet, PACKET_CONTENT_OFFSET+i, FileInfo.getInstance().getFileHeadLen(), 2);
               i += UserArrayUtils.array_copy(out_packet, PACKET_CONTENT_OFFSET+i, FileInfo.getInstance().getHwVersion());
               i += UserArrayUtils.array_copy(out_packet, PACKET_CONTENT_OFFSET+i, FileInfo.getInstance().getSwVersion());
               break;
           case U_LOAD_DATA_ACK:
               i += UserArrayUtils.array_copy(out_packet, PACKET_CONTENT_OFFSET+i, FileInfo.getInstance().getFileBlockLen()+0x0004, 2);
               i += UserArrayUtils.array_copy(out_packet, PACKET_CONTENT_OFFSET+i, FileInfo.getInstance().getFileContent(), contentOffset, contentLen);
               break;
       }

       return i;
    }

    /**
     *  封装上位下发报文；
     * @param packetType 待封装下发报文类型；
     *                  contentOffset  针对固件内容响应报文，其它报文无效，指定下发内容段在固件文件的偏移位置；
     *                   cotentLen  针对固件内容响应报文，其它报文无效， 指定下发内容段长度；
     * @return 封装报文总长
     */
    @Override
    public int packetComposite(PacketType packetType, int contentOffset, int contentLen) {
        int out_packet_len = 0;
        int packet_data_len = 0;
        packet_data_len = this.setPacketContent(packetType, contentOffset, contentLen);
        out_packet_len += UserArrayUtils.array_copy(out_packet, out_packet_len,  Packet.P_HEAD);
        out_packet_len += UserArrayUtils.array_copy(out_packet, out_packet_len,  Packet.P_VERSION);
        out_packet_len += UserArrayUtils.array_copy(out_packet, out_packet_len,  10+packet_data_len, 2);
        out_packet_len += UserArrayUtils.array_copy(out_packet, out_packet_len,  Packet.P_U_SEQ.getAndIncrement(), 2);
        out_packet_len += packet_data_len; //跳过填充报文数据段长度
        out_packet_len += UserArrayUtils.array_copy(out_packet, out_packet_len, this.calcPacketCrc(out_packet, 2,  out_packet_len-2));
        out_packet_len += UserArrayUtils.array_copy(out_packet, out_packet_len,  Packet.P_TAIL);

        Utils.log("Data sent len: " + out_packet_len);
        UserArrayUtils.hexDumpArray(out_packet);

        return out_packet_len;
    }

    public byte[] getOut_packet() {
        return out_packet;
    }
}
