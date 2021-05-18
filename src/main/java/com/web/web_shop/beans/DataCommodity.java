package com.web.web_shop.beans;

/**
 * DateCommodity
 */
public class DataCommodity {

    private Long cid;
    private String name;
    private Integer cur_number;
    private Integer total_number;
    private Integer price;
    private String seller_name;
    //private String Shopname;
    private Integer type;
    private Integer com_status;
    private String description;

    public Long getCid() {
        return cid;
    }
    public String getName() {
        return name;
    }
    public Integer getType() {
        return type;
    }
    public Integer getPrice() {
        return price;
    }
    //public String getShopname() {
    //    return Shopname;
    //}
    public Integer getCom_status() {
        return com_status;
    }
    public Integer getCur_number() {
        return cur_number;
    }
    public String getDescription() {
        return description;
    }
    public Integer getTotal_number() {
        return total_number;
    }
    public String getSeller_name() {
        return seller_name;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    //public void setShopname(String shopname) {
    //    Shopname = shopname;
    //}
    public void setCom_status(Integer com_status) {
        this.com_status = com_status;
    }
    public void setCur_number(Integer cur_number) {
        this.cur_number = cur_number;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setTotal_number(Integer total_number) {
        this.total_number = total_number;
    }
    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }
}
