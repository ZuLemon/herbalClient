package net.andy.boiling.domain;

/**
 * Created by Administrator on 2014-11-14.
 * 标签管理
 */
public class TagDomain {
    private Integer id;
    //标签ID
    private String tagId = "";
    //标签号码
    private String code = "";
    //标签颜色
    private String color = "";
    //标签类别(处方,煎药机,加液机,包装机)
    private String type = "";
    //状态(空闲,使用)
    private String status = "";
    //绑定的煎药记录
    private String bindId = "";
    private String colorValue="";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBindId() {
        return bindId;
    }

    public void setBindId(String bindId) {
        this.bindId = bindId;
    }

    public String getColorValue() {
        return colorValue;
    }

    public void setColorValue(String colorValue) {
        this.colorValue = colorValue;
    }
}
