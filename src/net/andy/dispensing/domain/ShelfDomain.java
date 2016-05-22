package net.andy.dispensing.domain;

/**
 * Created by mac on 16-二月-24.
 * 货位管理
 */
public class ShelfDomain {
    private Integer id;
    /*货位名称*/
    private String name = "";

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
}
