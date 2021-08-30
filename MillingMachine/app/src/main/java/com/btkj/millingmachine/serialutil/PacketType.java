package com.btkj.millingmachine.serialutil;

public enum PacketType {
    /**  硬件重启后，500ms内上送，连接请求命令 */
    D_CON_REQ(0X1101),
    /**  请求连接响应命令 ，硬件收到后，进入bootloader 模式 */
    U_CON_ACK(0X1102),
    /**  上位机下发请求软件升级命令 */
    U_UPDATE_REQ(0X2201),
    /**  硬件收到请求软件升级命令后，向上位机请求固件基本信息 */
    D_UPDATE__ACK_REQ(0X2203),
    /**  上位机响应升级软件固件基本信息 */
    U_UPDATE_ACK_PROFILE(0X2204),
    /**  硬件分段请求固件内容 */
    D_LOAD_DATA_REQ(0X2205),
    /**  上位机回复固件内容 */
    U_LOAD_DATA_ACK(0X2206),
    /**  硬件完成升级后，上报升级结果 */
    D_UPDATE_RESULT(0X2207);

    private int cmd;

    private  PacketType(int cmd) {
        this.cmd = cmd;
    }

    public int getCmd() {
        return cmd;
    }

    public byte[] getCmdArray() {
        byte[] cmd = new byte[2];
        cmd[0] = getMsbByte();
        cmd[1] = getLsbByte();
        return cmd;
    }

    /**  获取命令类型高字节 */
    public byte getMsbByte() {
        return (byte) (this.cmd>>8 & 0xff);
    }

    /**  获取命令类型低字节 */
    public byte getLsbByte() {
        return (byte) (this.cmd>>0 & 0xff);
    }


}
