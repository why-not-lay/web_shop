package com.web.web_shop.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Shop
 */
@Entity
@Table(name = "shop")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;
    @Column(name = "uid",nullable = false)
    private Long uid;
    @Column(name = "name",columnDefinition = "char(32)",nullable = false)
    private String name;
    @Column(name = "create_date",columnDefinition = "char(19)",nullable = false)
    private String create_date;
    @Column(name = "status",nullable = false)
    private Integer status;

    public Long getSid() {
        return sid;
    }
    public Long getUid() {
        return uid;
    }
    public String getName() {
        return name;
    }
    public Integer getStatus() {
        return status;
    }
    public String getCreate_date() {
        return create_date;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }
    public void setUid(Long uid) {
        this.uid = uid;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
}
