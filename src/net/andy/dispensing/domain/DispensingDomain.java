package net.andy.dispensing.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by mac on 16-二月-23.
 * 药品调剂记录
 */
public class DispensingDomain implements Serializable{
    private Integer id;
    /*调剂ID*/
    private String planId = "";
    /*绑定标签*/
    private String tagId = "";
    /*调剂人*/
    private Integer userId;
    /*调剂部门*/
    private String deptId = "";
    /*调剂开始时间*/
    private Date beginTime = new Date();
    /*调剂结束时间*/
    private Date endTime;
    /*类型(主处方,附处方)*/
    private String type = "主处方";
    /*调剂台工位ID*/
    private Integer stationId = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
    }
}
