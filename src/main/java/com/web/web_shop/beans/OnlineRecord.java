package com.web.web_shop.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.web.web_shop.Tool.Constant;

/**
 * OnlineRecord
 */
@Entity
@Table(name = "online_record")
public class OnlineRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long oid;
    @Column(name = "uid",nullable = false)
    Long uid;
    @Column(name = "login_date",columnDefinition = "char(19)",nullable = false)
    String loginDate;
    @Column(name = "login_ip",columnDefinition = "char(15)",nullable = false)
    String loginIp;
    @Column(name = "logout_date",columnDefinition = "char(19)",nullable = false)
    String logoutDate;
    @Column(name = "logout_ip",columnDefinition = "char(15)",nullable = false)
    String logoutIp;
    @Column(name = "status",nullable = false)
    Integer status;

    public Long getOid() {
        return oid;
    }
    public Long getUid() {
        return uid;
    }
    public String getLoginIp() {
        return loginIp;
    }
    public Integer getStatus() {
        return status;
    }
    public String getLogoutIp() {
        return logoutIp;
    }
    public String getLoginDate() {
        return loginDate;
    }
    public String getLogoutDate() {
        return logoutDate;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }
    public void setUid(Long uid) {
        this.uid = uid;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }
    public void setLogoutIp(String logoutIp) {
        this.logoutIp = logoutIp;
    }
    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }
    public void setLogoutDate(String logoutDate) {
        this.logoutDate = logoutDate;
    }

    public void setAfterLogin(Long uid, String login_ip, String login_date) {
        this.uid = uid;
        this.loginIp = login_ip;
        this.loginDate = login_date;
        this.status = Constant.RecordStatus.EXIST;
        this.logoutIp = "waiting";
        this.logoutDate = "waiting";
    }

    public void setBeforeLogout(String logout_ip, String logout_date) {
        this.logoutIp = logout_ip;
        this.logoutDate = logout_date;
    }
}
