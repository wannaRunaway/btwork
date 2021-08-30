package com.btkj.millingmachine.serialutil;

public interface ICrcFactory<T> {
    T crc(byte[] data, int startOffset, int len);
}
