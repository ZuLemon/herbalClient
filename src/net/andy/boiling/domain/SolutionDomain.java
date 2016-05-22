package net.andy.boiling.domain;

public class SolutionDomain {
    private Integer id;
    /*方案ID*/
    private String solutionId;
    /*名称*/
    private String name;
    /*浸泡时间*/
    private Integer soakTime;
    /*一煎时间*/
    private Integer extractTime1;
    /*二煎时间*/
    private Integer extractTime2;
    /*三煎时间*/
    private Integer extractTime3;
    /*温度*/
    private Integer temperature;
    /*压力*/
    private String pressure;
    /*剂型(饮片，膏方)*/
    private String classification;
    /*煎药方式(机煎、传统煎制)*/
    private String mode;
    /*状态(正常，删除)*/
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSolutionId() {
        return solutionId;
    }

    public void setSolutionId(String solutionId) {
        this.solutionId = solutionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSoakTime() {
        return soakTime;
    }

    public void setSoakTime(Integer soakTime) {
        this.soakTime = soakTime;
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

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
