package com.btkj.millingmachine.model.appupdate;

import java.io.Serializable;

/**
 * created by xuedi on 2019/7/25
 */
public class UpdateData implements Serializable {
    /**
     * {        
     *  * 		"id": 1,
     *  * 		        "packageName": "1",
     *  * 		        "versionCode": "1",
     *  * 		        "versionName": "1",
     *  * 		        "apkName": "碾米",
     *  * 		        "apkSize": "20MB",
     *  * 		        "apkPath": "https://rice-app.oss-cn-hangzhou.aliyuncs.com/app-rice-1.0-1.apk?Expires=1563960165&OSSAccessKeyId=TMP.hWmRe1CY3ZNMEYwYJJJRTyy3UwmsZyERQVHKSXx3axaLDTif3Ey9E4P2LxpvUJ6RcyTjrkoRUjYbaAZ29eQykcg87ZRoNY9NAbvouHYYV1qEuh2jbQqFNTq7pFTvMb.tmp&Signature=RhhwTmmmj2uHy97RMGDPHqUQQX0%3D",
     *  * 		        "createtime": [            2019,             7,             24,             17,             40,             45        ]    
     *  *        }
     */
    private int id;
    private String packageName, versionCode, versionName, apkName, apkSize, apkPath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getApkSize() {
        return apkSize;
    }

    public void setApkSize(String apkSize) {
        this.apkSize = apkSize;
    }

    public String getApkPath() {
        return apkPath;
    }

    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }
}
