package net.andy.boiling.domain;

import java.util.Date;

/**
 * 煎制过程
 */
public class ExtractDomain {
    private Integer id;
    /*煎制计划ID*/
    private String planId = "";
    /*煎药机ID*/
    private String equipId = "";
    /*操作员*/
    private Integer userId;
    /*开始时间*/
    private Date beginTime = new Date();
    /*结束时间*/
    private Date endTime;
    /*煎制状态(一煎，二煎，三煎)*/
    private String extractStatus;

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

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getExtractStatus() {
        return extractStatus;
    }

    public void setExtractStatus(String extractStatus) {
        this.extractStatus = extractStatus;
    }
}
