package com.web.web_shop.beans;

/**
 * DataTrade
 */
public class DataTrade {

    private String name;
    private String user;
    private Integer price;
    private Integer number;

    public void setName(String name) {
        this.name = name;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }
    public String getUser() {
        return user;
    }
    public Integer getPrice() {
        return price;
    }
    public Integer getNumber() {
        return number;
    }

}
