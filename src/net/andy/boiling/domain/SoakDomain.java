package net.andy.boiling.domain;

import java.util.Date;

/**
 * 浸泡
 */
public class SoakDomain {
    private Integer id;
    /*煎制计划ID*/
    private String planId="";
    /*加液机ID*/
    private String equipId="";
    /*操作员ID*/
    private Integer userId;
    /*开始时间*/
    private Date beginTime=new Date();
    /*结束时间*/
    private Date endTime;

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
}
