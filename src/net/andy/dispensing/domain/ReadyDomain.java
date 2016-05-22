package net.andy.dispensing.domain;

/**
 * Created by Guang on 2016/5/11.
 * 预调剂处方表
 */
public class ReadyDomain {
    /*预调剂ID*/
    private Integer id;
    /*预调剂标题*/
    private String title;
    /*预调剂处方状态*/
    private String status;
    /*预调剂部门ID*/
    private String deptId;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
