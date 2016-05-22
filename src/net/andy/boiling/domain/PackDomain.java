package net.andy.boiling.domain;

import java.util.Date;

/**
 * 包装操作
 */
public class PackDomain {
    private Integer id;
    /*煎制计划ID*/
    private String planId = "";
    /*操作员*/
    private Integer userId = 0;
    /*包装时间*/
    private Date packTime = new Date();

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

    public Date getPackTime() {
        return packTime;
    }

    public void setPackTime(Date packTime) {
        this.packTime = packTime;
    }
}
