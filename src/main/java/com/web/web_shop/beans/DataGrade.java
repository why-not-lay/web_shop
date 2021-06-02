package com.web.web_shop.beans;

/**
 * DataGrade
 */
public class DataGrade {

    private String name;
    private String seller_name;
    private Integer sold_number;
    private Integer cur_price;
    private Integer cur_number;
    private String status;
    private Integer income;

    public String getSeller_name() {
        return seller_name;
    }
    public String getName() {
        return name;
    }
    public String getStatus() {
        return status;
    }
    public Integer getIncome() {
        return income;
    }
    public Integer getCur_price() {
        return cur_price;
    }
    public Integer getCur_number() {
        return cur_number;
    }
    public Integer getSold_number() {
        return sold_number;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public void setIncome(Integer income) {
        this.income = income;
    }
    public void setCur_price(Integer cur_price) {
        this.cur_price = cur_price;
    }
    public void setCur_number(Integer cur_number) {
        this.cur_number = cur_number;
    }
    public void setSold_number(Integer sold_number) {
        this.sold_number = sold_number;
    }
}
