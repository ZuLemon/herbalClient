package net.andy.dispensing.domain;

/**
 * Created by mac on 16-二月-24.
 * 药品调剂规则
 */
public class RulesDomain {
    private Integer id;
    /*规则名称*/
    private String name = "";
    /*规则描述*/
    private String description = "";
    /*规则代码*/
    private String rules = "";
    /*规则类型(主处方,附处方)*/
    private String type = "";


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
