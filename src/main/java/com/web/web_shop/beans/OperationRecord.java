package com.web.web_shop.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * OperationRecord
 */
@Entity
@Table(name = "operation_record")
public class OperationRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long oid;
    @Column(name = "main_uid",nullable = false)
    Long mainUid;
    @Column(name = "operation_type",nullable = false)
    Integer operationType;
    @Column(name = "object_id",nullable = false)
    Long objectId;
    @Column(name = "object_type",nullable = false)
    Integer objectType;
    @Column(name = "ip", columnDefinition = "char(15)",nullable = false)
    String ip;
    @Column(name = "date", columnDefinition = "char(19)",nullable = false)
    String date;
    @Column(name = "status",nullable = false)
    Integer status;
    @Column(name = "content",columnDefinition = "varchar(512)")
    String content;

    public void setStatus(Integer status) {
        this.status = status;
    }
    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public void setOid(Long oid) {
        this.oid = oid;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setMainUid(Long mainUid) {
        this.mainUid = mainUid;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setObjectType(Integer objectType) {
        this.objectType = objectType;
    }
    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }

    public Long getOid() {
        return oid;
    }
    public String getIp() {
        return ip;
    }
    public String getDate() {
        return date;
    }
    public Long getMainUid() {
        return mainUid;
    }
    public String getContent() {
        return content;
    }
    public Long getObjectId() {
        return objectId;
    }
    public Integer getObjectType() {
        return objectType;
    }
    public Integer getOperationType() {
        return operationType;
    }
    public Integer getStatus() {
        return status;
    }
}
