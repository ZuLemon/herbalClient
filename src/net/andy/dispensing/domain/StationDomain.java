package net.andy.dispensing.domain;

/**
 * Created by mac on 16-二月-24.
 * 工位管理
 */
public class StationDomain {
    private Integer id;
    /*工位名称*/
    private String name = "";
    /*绑定设备*/
    private String device = "";
    /*绑定规则ID*/
    private Integer rulesId = 0;
    /*绑定货位ID*/
    private Integer shelfId = 0;
    private String deptId="";

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getRulesId() {
        return rulesId;
    }

    public void setRulesId(Integer rulesId) {
        this.rulesId = rulesId;
    }

    public Integer getShelfId() {
        return shelfId;
    }

    public void setShelfId(Integer shelfId) {
        this.shelfId = shelfId;
    }
}
