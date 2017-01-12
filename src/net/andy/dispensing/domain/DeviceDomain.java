package net.andy.dispensing.domain;

import java.util.Date;

/**
 * Created by Guang on 2016/12/22.
 */
public class DeviceDomain {
    private Integer id;
    private String imei;
    private String meid;
    private String mac;
    private String model;
    private String loginIP;
    private Date regeditTime;
    private Date lastTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getRegeditTime() {
        return regeditTime;
    }

    public void setRegeditTime(Date regeditTime) {
        this.regeditTime = regeditTime;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIp) {
        this.loginIP = loginIp;
    }
}
