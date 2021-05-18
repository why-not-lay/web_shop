package com.web.web_shop.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ViewRecord
 */
@Entity
@Table(name = "view_record")
public class ViewRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long vid;
    @Column(name = "uid",nullable = false)
    Long uid;
    @Column(name = "cid",nullable = false)
    Long cid;
    @Column(name = "enter_ip",columnDefinition = "char(15)", nullable = false)
    String enterIp;
    @Column(name = "enter_timestamp",nullable = false)
    Long enterTimestamp;
    @Column(name = "out_ip",columnDefinition = "char(15)",nullable = false)
    String outIp;
    @Column(name = "out_timestamp",nullable = false)
    Long outTimestamp;
    @Column(name = "status",nullable = false)
    Integer status;

    public Long getCid() {
        return cid;
    }
    public Long getUid() {
        return uid;
    }
    public Long getVid() {
        return vid;
    }
    public String getOutIp() {
        return outIp;
    }
    public String getEnterIp() {
        return enterIp;
    }
    public Integer getStatus() {
        return status;
    }
    public Long getOutTimestamp() {
        return outTimestamp;
    }
    public Long getEnterTimestamp() {
        return enterTimestamp;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }
    public void setUid(Long uid) {
        this.uid = uid;
    }
    public void setVid(Long vid) {
        this.vid = vid;
    }
    public void setOutIp(String outIp) {
        this.outIp = outIp;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public void setEnterIp(String enterIp) {
        this.enterIp = enterIp;
    }
    public void setOutTimestamp(Long outTimestamp) {
        this.outTimestamp = outTimestamp;
    }
    public void setEnterTimestamp(Long enterTimestamp) {
        this.enterTimestamp = enterTimestamp;
    }
}
