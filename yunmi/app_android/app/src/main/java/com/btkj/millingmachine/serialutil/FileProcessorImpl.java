package com.btkj.millingmachine.serialutil;

import com.btkj.millingmachine.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystemException;

public class FileProcessorImpl implements IFileProcessor {
    @Override
    public boolean downloadFile(String fileName) {
        return false;
    }

    @Override
    public boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file != null && file.exists()) {
            file.delete();
        }
        return true;
    }

    /***
     *  检查要更新文件是否存在，不存在则不做操作；
     *  如果文件存在，未读取，则启动读；已读取，则直接返回true;
     *  如果无效文件，则直接删除。
     */
    @Override
    public boolean openAndVerifyFile(String fileName) {
        File file = new File(fileName);
        if (file != null && file.exists()) {
//           if (!FileInfo.getInstance().alreadRead()) {
               readFile(fileName);
//           }
               if (FileInfo.getInstance().alreadRead()) {
                   return true;
               }
//           else {
//               file.delete();
//           }
        }
        FileInfo.getInstance().resetFileLen();
        return false;
    }

    private void readFile(String fileName) {
        //File file = new File(fileName);
        InputStream in = null;

        try {
            // 一次读多个字节
            FileInfo.getInstance().resetFileLen();
            byte[] tempbytes = FileInfo.getInstance().getFileContent();
            int byteread = 0;
            in = new FileInputStream(fileName);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                Utils.log(byteread + " bytes read" );
                break;
            }
            if (byteread != -1 && byteread >= FileInfo.MAX_FILE_LEN) {
                throw new Exception("file size exceeds 2M");
            }
//            Utils.log("读到的文件"+UserArrayUtils.hexDumpArray(tempbytes));
            FileInfo.getInstance().setFileLen(byteread);
            FileInfo.getInstance().decodeFileHead();
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    Utils.log(e1.getMessage());
                }
            }
        }
    }

}
