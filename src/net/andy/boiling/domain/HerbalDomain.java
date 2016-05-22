package net.andy.boiling.domain;

/**
 * Created by mac on 16-一月-5.
 * 草药目录
 */
public class HerbalDomain {
    private Integer id;
    private String itemName = "";
    private String itemAbbr = "";
    /*吸水率(默认值0)*/
    private String ratio = "0";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemAbbr() {
        return itemAbbr;
    }

    public void setItemAbbr(String itemAbbr) {
        this.itemAbbr = itemAbbr;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
}
