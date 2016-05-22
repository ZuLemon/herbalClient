package net.andy.boiling.domain;

import java.math.BigDecimal;

public class PrescriptionDetailDomain {
    private Integer id;
    /*调剂ID*/
    private String planId="";
    /*处方ID*/
    private String presId="";
    /*药品编码*/
    private String herbId="";
    /*药品名称*/
    private String herbName="";
    /*单位*/
    private String herbUnit="";
    /*数量*/
    private BigDecimal quantity;
    /*特殊要求*/
    private String special="";
    /*贵重药品*/
    private String valuables = "";

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

    public String getPresId() {
        return presId;
    }

    public void setPresId(String presId) {
        this.presId = presId;
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

    public String getHerbUnit() {
        return herbUnit;
    }

    public void setHerbUnit(String herbUnit) {
        this.herbUnit = herbUnit;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getValuables() {
        return valuables;
    }

    public void setValuables(String valuables) {
        this.valuables = valuables;
    }
}
