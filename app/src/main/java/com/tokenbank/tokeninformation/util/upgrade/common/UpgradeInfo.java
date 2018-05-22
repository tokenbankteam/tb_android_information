package com.tokenbank.tokeninformation.util.upgrade.common;

import com.google.gson.annotations.SerializedName;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:
 */
public class UpgradeInfo {

    @SerializedName("platform")
    private String mPlatform;
    @SerializedName("software_version")
    private String mVersion;
    @SerializedName("download_url")
    private String mDownloadUrl;
    @SerializedName("hash")
    private String mHashCode;
    @SerializedName("upgrade_way")
    private int mUpgradeMode;
    @SerializedName("size")
    private long mSize;

    //用于辅助的字段，非json返回
    private String apkName;

    public String getPlatform() {
        return mPlatform;
    }

    public UpgradeInfo setPlatform(String platform) {
        this.mPlatform = platform;
        return this;
    }

    public String getVersion() {
        return mVersion;
    }

    public UpgradeInfo setVersion(String version) {
        this.mVersion = version;
        return this;
    }

    public String getUrl() {
        return mDownloadUrl;
    }

    public UpgradeInfo setUrl(String url) {
        this.mDownloadUrl = url;
        return this;
    }

    public long getSize() {
        return mSize;
    }

    public UpgradeInfo setSize(long size) {
        this.mSize = size;
        return this;
    }

    public String getHashCode() {
        return mHashCode;
    }

    public UpgradeInfo setHashCode(String hashCode) {
        this.mHashCode = hashCode;
        return this;
    }

    public int getUpgradeMode() {
        return mUpgradeMode;
    }

    public UpgradeInfo setUpgradeMode(int upgradeMode) {
        this.mUpgradeMode = upgradeMode;
        return this;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String toString() {
        return "[platform = " + mPlatform + ";version = " + mVersion + ";downloadUrl=" + mDownloadUrl + ";hashCode=" + mHashCode + ";upgradeMode=" + mUpgradeMode + "]";
    }
}
