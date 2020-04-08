package com.zhanke.webview.bean.android;

/**
 * Created by zhanke on 2020/3/9.
 * Describe
 */
public class AppInfoBean {
    /**
     * appVersion : app版本
     * osVersion : 系统版本
     * deviceWidth : 屏幕宽度
     * deviceHeight : 屏幕高度
     * imei : 设备imei号
     * uuid : 设备的uuid
     * platform : 1:安卓 2:ios
     * model : 手机型号
     */

    private String appVersion;
    private String osVersion;
    private String deviceWidth;
    private String deviceHeight;
    private String imei;
    private String uuid;
    private String platform = "1";
    private String model;

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getDeviceWidth() {
        return deviceWidth;
    }

    public void setDeviceWidth(String deviceWidth) {
        this.deviceWidth = deviceWidth;
    }

    public String getDeviceHeight() {
        return deviceHeight;
    }

    public void setDeviceHeight(String deviceHeight) {
        this.deviceHeight = deviceHeight;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
