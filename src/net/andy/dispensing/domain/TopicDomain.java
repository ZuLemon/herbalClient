package net.andy.dispensing.domain;


/**
 * Created by mac on 16-三月-27.
 * 订阅消息
 */
public class TopicDomain {
    private Integer id;
    /*订阅用户*/
    private Integer userId = 0;
    /*用户部门*/
    private String deptId = "";
    /*主题*/
    private String topic = "";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
