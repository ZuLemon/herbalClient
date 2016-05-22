package net.andy.boiling.domain;

/**
 * Created by mac on 15-十月-16.
 * HIS药品目录
 */
public class HisItemDomain {
    Integer id;
    String itemId = "";
    String itemName = "";
    String ItemAbbr = "";
    Integer ratio = 0;
    String spec = "";
    String unit = "";
    String dosageForm = "";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemAbbr() {
        return ItemAbbr;
    }

    public void setItemAbbr(String itemAbbr) {
        ItemAbbr = itemAbbr;
    }

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }
}
