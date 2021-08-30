package com.btkj.millingmachine.serialutil;

import java.util.concurrent.atomic.AtomicInteger;

public class FileInfo {
    private static FileInfo ourInstance = new FileInfo();

    public static FileInfo getInstance() {
        return ourInstance;
    }

    private FileInfo() {
    }

    public  final static int MAX_FILE_LEN = 1<< 20;

    private  final static int BLOCK_LEN = (1<< 9) + 2;

    private final  static int FILE_HEAD_LEN = 10;

    private byte[] fileContent = new byte[MAX_FILE_LEN];

    private AtomicInteger fileLen = new AtomicInteger();

    private byte[] hwVersion = new byte[2];

    private byte[] swVersion = new byte[2];

    private final int fileBlockLen = BLOCK_LEN;

    private final int fileHeadLen = FILE_HEAD_LEN;

    public byte[] getFileContent() {
        return fileContent;
    }

    public int getFileLen() {
        return fileLen.get();
    }

    public byte[] getHwVersion() {
        return hwVersion;
    }

    public byte[] getSwVersion() {
        return swVersion;
    }

    public String getHwVersionHex() {
        return UserArrayUtils.hexDumpArray(this.hwVersion);
    }

    public String getSwVersionHex() {
        return UserArrayUtils.hexDumpArray(this.swVersion);
    }

    public int getFileHeadLen() {
        return fileHeadLen;
    }

    public int getFileBlockLen() {
        return fileBlockLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen.compareAndSet(this.fileLen.get(), fileLen);
    }

    public void resetFileLen() {
        this.fileLen.compareAndSet(this.fileLen.get(), 0);
    }

    /***
     *  如果文件长度不足一个块（514B）大小，认为文件读取无效。
     */
    public  boolean alreadRead() {
        if (this.fileLen.get() < this.fileBlockLen) {
            return false;
        }
        else {
            return true;
        }
    }

    public void decodeFileHead() {
        if (this.alreadRead()) {
            System.arraycopy(this.fileContent, 0, this.hwVersion, 0, 2);
            System.arraycopy(this.fileContent, 2, this.swVersion, 0, 2);
        }
    }
}
