package com.web.web_shop.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DataOrder
 */
public class DataOrder {
    @JsonProperty("c")
    private Long cid;
    @JsonProperty("n")
    private Integer number;
    private Integer price;
    private String name;

    public Long getCid() {
        return cid;
    }
    public Integer getNumber() {
        return number;
    }
    public Integer getPrice() {
        return price;
    }
    public String getName() {
        return name;
    }
    public void setCid(Long cid) {
        this.cid = cid;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public void setName(String name) {
        this.name = name;
    }
}
