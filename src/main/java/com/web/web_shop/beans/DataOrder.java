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

    public Long getCid() {
        return cid;
    }
    public Integer getNumber() {
        return number;
    }
    public void setCid(Long cid) {
        this.cid = cid;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
}
