package net.andy.dispensing.domain;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by mac on 16-三月-16.
 * 补充药品通知
 */
public class ReplenishDomain implements Serializable {
    private Integer id;
    /*货位ID*/
    private Integer shelfId = 0;
    /*药品编码*/
    private String herbId = "";
    /*工位ID*/
    private Integer stationId = 0;
    /*请求时间*/
    private Date sendTime = new Date();
    /*完成时间*/
    private Date finishTime = new Date();
    /*部门*/
    private String deptId = "";
    /*请求人*/
    private Integer requestUser = 0;
    /*操作员*/
    private Integer userId = 0;
    /*状态(申请,完成)*/
    private String status = "申请";

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getHerbId() {
        return herbId;
    }

    public void setHerbId(String herbId) {
        this.herbId = herbId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRequestUser() {
        return requestUser;
    }

    public void setRequestUser(Integer requestUser) {
        this.requestUser = requestUser;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getShelfId() {
        return shelfId;
    }

    public void setShelfId(Integer shelfId) {
        this.shelfId = shelfId;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
