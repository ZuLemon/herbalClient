package net.andy.dispensing.domain;

/**
 * Created by mac on 16-二月-24.
 * 货位详细信息
 */
public class ShelfDetailDomain {
    private Integer id;
    /*货位ID*/
    private Integer shelfId = 0;
    /*药品编码*/
    private String herbId = "";
    /*药品名称*/
    private String herbName = "";
    /*货位编码*/
    private String code = "";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShelfId() {
        return shelfId;
    }

    public void setShelfId(Integer shelfId) {
        this.shelfId = shelfId;
    }

    public String getHerbId() {
        return herbId;
    }

    public void setHerbId(String herbId) {
        this.herbId = herbId;
    }

    public String getHerbName() {
        return herbName;
    }

    public void setHerbName(String herbName) {
        this.herbName = herbName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
