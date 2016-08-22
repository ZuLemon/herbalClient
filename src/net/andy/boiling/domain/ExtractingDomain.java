package net.andy.boiling.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtractingDomain {

    private Integer id;
    /*煎制计划ID*/
    private String planId = "";
    /*煎制计划RFID*/
    private String tagId = "";
    /*处方ID*/
    private String presId = "";
    /*煎制方案ID*/
    private String solutionId = "";
    /*操作员ID*/
    private Integer userId = 0;
    /*操作部门*/
    private String deptId = "";
    /*操作时间(即煎制计划产生时间)*/
    private Date operationTime = new Date();
    /*一煎加液量(即浸泡加液量，单位毫升)*/
    private Integer quantity = 0;
    /*出液量(单位毫升)*/
    private Integer out = 0;
    /*浸泡时长(单位分钟)*/
    private Integer soakTime = 0;
    /*浸泡类型(电锅,汽锅)*/
    private String soakType = "";
    /*一煎时长(单位分钟)*/
    private Integer extractTime1 = 0;
    /*二煎时长*/
    private Integer extractTime2 = 0;
    /*三煎时长*/
    private Integer extractTime3 = 0;
    /*温度(单位摄氏度)*/
    private Integer temperature = 0;
    /*煎制压力*/
    private String pressure = "";
    /*每次用量*/
    private String dosage = "";
    /*用药方式(口服、外用)*/
    private String way = "";
    /*用药次数(日一次，日二次，日三次)*/
    private String frequency = "";
    /*处方剂型(饮片、膏方)*/
    private String classification = "";
    /*煎制方式(机煎、传统煎制)*/
    private String method = "";
    /*状态(开始，浸泡，领取，一煎，二煎，三煎，包装)*/
    private String planStatus = "";

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

    public String getPresId() {
        return presId;
    }

    public void setPresId(String presId) {
        this.presId = presId;
    }

    public String getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
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

    public Date getOperationTime() {

        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public Integer getSoakTime() {
        return soakTime;
    }

    public void setSoakTime(Integer soakTime) {
        this.soakTime = soakTime;
    }

    public String getSoakType() {
        return soakType;
    }

    public void setSoakType(String soakType) {
        this.soakType = soakType;
    }

    public Integer getExtractTime1() {
        return extractTime1;
    }

    public void setExtractTime1(Integer extractTime1) {
        this.extractTime1 = extractTime1;
    }

    public Integer getExtractTime2() {
        return extractTime2;
    }

    public void setExtractTime2(Integer extractTime2) {
        this.extractTime2 = extractTime2;
    }

    public Integer getExtractTime3() {
        return extractTime3;
    }

    public void setExtractTime3(Integer extractTime3) {
        this.extractTime3 = extractTime3;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getOut() {
        return out;
    }

    public void setOut(Integer out) {
        this.out = out;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPlanStatus() {
        return planStatus;
    }

    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }

}
