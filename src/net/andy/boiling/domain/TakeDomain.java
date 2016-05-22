package net.andy.boiling.domain;

import java.util.Date;

/**
 * 取桶操作
 */
public class TakeDomain {
    private Integer id;
    /*煎制计划ID*/
    private String planId;
    /*操作员ID*/
    private Integer userId;
    /*取桶时间*/
    private Date takeTime;
    /*绑定的煎药机ID*/
    private String equipId;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Date takeTime) {
        this.takeTime = takeTime;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }
}
