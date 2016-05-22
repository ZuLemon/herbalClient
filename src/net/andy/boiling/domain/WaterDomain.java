package net.andy.boiling.domain;

import java.util.Date;

/**
 * Created by mac on 15-十月-22.
 * 加液机数据交换表
 */
public class WaterDomain {
    Integer id;
    /**/
    String planId="";
    String tagId = "";
    String equipId = "";
    String quantity = "";
    Date start = new Date();
    Date finish;
    String status = "s";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getFinish() {
        return finish;
    }

    public void setFinish(Date finish) {
        this.finish = finish;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
