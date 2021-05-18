package com.web.web_shop.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Commodity
 */
@Entity
@Table(name = "commodity")
public class Commodity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid;
    @Column(name ="name",columnDefinition = "char(128)", nullable = false)
    private String name;
    @Column(name = "cur_number",nullable = false)
    private Integer curNumber;
    @Column(name = "total_number",nullable = false)
    private Integer totalNumber;
    @Column(name = "description",columnDefinition = "char(255)")
    private String description;
    @Column(name = "price",nullable = false)
    private Integer price;
    @Column(name = "pic_dir",columnDefinition = "char(32)",nullable=false)
    private String picDir;
    @Column(name = "uid",nullable = false)
    private Long uid;
    @Column(name = "status",nullable = false)
    private Integer status;
    @Column(name = "type",nullable = false)
    private Integer type;
    @Column(name = "com_status",nullable = false)
    private Integer comStatus;

    public Long getCid() {
        return cid;
    }
    public Long getUid() {
        return uid;
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
    public Integer getStatus() {
        return status;
    }
    public String getDescription() {
        return description;
    }
    public String getPicDir() {
        return picDir;
    }
    public Integer getComStatus() {
        return comStatus;
    }
    public Integer getCurNumber() {
        return curNumber;
    }
    public Integer getTotalNumber() {
        return totalNumber;
    }


    public void setCid(Long cid) {
        this.cid = cid;
    }
    public void setUid(Long uid) {
        this.uid = uid;
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
    public void setStatus(Integer status) {
        this.status = status;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPicDir(String picDir) {
        this.picDir = picDir;
    }
    public void setComStatus(Integer comStatus) {
        this.comStatus = comStatus;
    }
    public void setCurNumber(Integer curNumber) {
        this.curNumber = curNumber;
    }
    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }
}
