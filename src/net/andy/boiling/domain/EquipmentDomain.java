package net.andy.boiling.domain;

public class EquipmentDomain {
    private Integer id;
    /*设备ID*/
    private String equipId = "";
    /*设备标签号*/
    private String tagId = "";
    /*设备名称*/
    private String equipName = "";
    /*设备类型(加液机，煎药机，包装机)*/
    private String equipType = "";
    /*煎药机类型(电锅，气锅)*/
    private String equipType1 = "";
    /*设备用途(内服，外用)*/
    private String equipPurpose = "";
    /*设备状态(空闲，占用，使用，维修，停用，报废)*/
    private String equipStatus = "";
    /*操作人*/
    private Integer equipUserId = 0;
    /*被绑定的煎制计划*/
    private String planId = "";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public String getEquipType() {
        return equipType;
    }

    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }

    public String getEquipType1() {
        return equipType1;
    }

    public void setEquipType1(String equipType1) {
        this.equipType1 = equipType1;
    }

    public String getEquipPurpose() {
        return equipPurpose;
    }

    public void setEquipPurpose(String equipPurpose) {
        this.equipPurpose = equipPurpose;
    }

    public String getEquipStatus() {
        return equipStatus;
    }

    public void setEquipStatus(String equipStatus) {
        this.equipStatus = equipStatus;
    }

    public Integer getEquipUserId() {
        return equipUserId;
    }

    public void setEquipUserId(Integer equipUserId) {
        this.equipUserId = equipUserId;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }
}
