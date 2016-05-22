package net.andy.boiling.domain;

import java.io.Serializable;
import java.util.Date;

public class PrescriptionDomain implements Serializable{
    private Integer id;
    /*调剂ID*/
    private String planId = "";
    /*处方ID*/
    private String presId = "";
    /*病人编号(门诊号，住院号)*/
    private String patientNo = "";
    /*病人姓名*/
    private String patientName = "";
    /*条码*/
    private String barcode = "";
    /*预调剂处方ID*/
     private Integer ready=0;
    /*处方诊断*/
    private String diagnosis = "";
    /*处方付数*/
    private Integer presNumber;
    /*中药味数*/
    private Integer herbCnt=0;
    /*贵重中药味数*/
    private Integer valuablesCnt=0;
    /*处方类别(门诊，住院)*/
    private String category = "";
    /*处方剂型(免煎,饮片，膏方)*/
    private String classification = "";
    /*用药方式(口服、外用、外洗)*/
    private String way = "";
    /*配置方式*/
    private String manufacture = "";
    /*是否代煎(代煎,自煎)*/
    private String process="";
    /*用药时间(格式yyyy-mm-dd)*/
    private String date = "";
    /*用药次数(日一次，日二次，日三次)*/
    private String frequency = "";
    /*每次用量*/
    private String dosage = "";
    /*功效(一般类，解表类，滋补类)*/
    private String efficacy = "";
    /*主处方调剂状态(新处方,调剂中,完成)*/
    private String main = "新处方";
    /*附处方调剂状态(新处方,调剂中,完成)*/
    private String attach = "新处方";
    /*提交时间*/
    private Date subTime = new Date();
    /*开单科室ID*/
    private String deptId = "";
    /*开单科室名称*/
    private String deptName = "";
    /*调剂药房ID*/
    private String drugstoreId = "";
    /*调剂药房名称*/
    private String drugstoreName = "";
    /*处方医生ID*/
    private String doctorId = "";
    /*处方医生姓名*/
    private String doctorName = "";

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

    public String getPatientNo() {
        return patientNo;
    }

    public void setPatientNo(String patientNo) {
        this.patientNo = patientNo;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Integer getPresNumber() {
        return presNumber;
    }

    public void setPresNumber(Integer presNumber) {
        this.presNumber = presNumber;
    }

    public Integer getHerbCnt() {
        return herbCnt;
    }

    public void setHerbCnt(Integer herbCnt) {
        this.herbCnt = herbCnt;
    }

    public Integer getValuablesCnt() {
        return valuablesCnt;
    }

    public void setValuablesCnt(Integer valuablesCnt) {
        this.valuablesCnt = valuablesCnt;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClassification() {
        return classification;
    }

    public void setClassification(String formula) {
        this.classification = formula;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getEfficacy() {
        return efficacy;
    }

    public void setEfficacy(String efficacy) {
        this.efficacy = efficacy;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Date getSubTime() {
        return subTime;
    }

    public void setSubTime(Date subTime) {
        this.subTime = subTime;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDrugstoreId() {
        return drugstoreId;
    }

    public void setDrugstoreId(String drugstoreId) {
        this.drugstoreId = drugstoreId;
    }

    public String getDrugstoreName() {
        return drugstoreName;
    }

    public void setDrugstoreName(String drugstoreName) {
        this.drugstoreName = drugstoreName;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Integer getReady() {
        return ready;
    }

    public void setReady(Integer ready) {
        this.ready = ready;
    }
}
