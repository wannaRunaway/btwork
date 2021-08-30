package com.btkj.millingmachine.serialutil;

public interface IFileProcessor {

    boolean downloadFile(String fileName);

    boolean deleteFile(String fileName);
    /**
     *  return  false if file invalid, true otherwise.
     */
    boolean openAndVerifyFile(String fileName);

}
