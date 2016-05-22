package net.andy.dispensing.domain;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by mac on 16-二月-23.
 * 调剂明细
 */
public class DispensingDetailDomain implements Serializable{
    private Integer id;
    /*调剂主表ID*/
    private Integer disId;
    /*调剂ID*/
    private String planId = "";
    /*药品编码*/
    private String herbId = "";
    /*药品名称*/
    private String herbName = "";
    /*单位*/
    private String herbUnit = "";
    /*规格*/
    private String herbSpec = "";
    /*数量*/
    private BigDecimal quantity ;
    /*特殊要求*/
    private String special = "";
    /*贵重药*/
    private String valuables = "";
    /*超量警告*/
    private String warning = "";
    /*调剂人*/
    private Integer userId = 0;
    /*调剂部门*/
    private String deptId = "";
    /*开始时间*/
    private Date beginTime = new Date();
    /*完成时间*/
    private Date endTime;
    /*调剂状态*/
    private String status = "未调剂";
    /*货位*/
    private String shelf = "";

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Integer getDisId() {
        return disId;
    }

    public void setDisId(Integer disId) {
        this.disId = disId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getHerbId() {
        return herbId;
    }

    public void setHerbId(String herbId) {
        this.herbId = herbId;
    }

    public String getHerbName() {
        return herbName;
    }

    public void setHerbName(String herbName) {
        this.herbName = herbName;
    }

    public String getHerbSpec() {
        return herbSpec;
    }

    public void setHerbSpec(String herbSpec) {
        this.herbSpec = herbSpec;
    }

    public String getHerbUnit() {
        return herbUnit;
    }

    public void setHerbUnit(String herbUnit) {
        this.herbUnit = herbUnit;
    }

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

    public String getShelf() {
        return shelf;
    }

    public void setShelf(String shelf) {
        this.shelf = shelf;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
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

    public String getValuables() {
        return valuables;
    }

    public void setValuables(String valuables) {
        this.valuables = valuables;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
