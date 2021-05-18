package com.web.web_shop.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.web.web_shop.Tool.Constant;
import com.web.web_shop.Tool.Util;


/**
 * User
 */
@Entity
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    @Column(name="username",columnDefinition="char(16)",nullable=false)
    private String username;
    @Column(name="password",columnDefinition= "char(16)",nullable=false)
    private String password;
    @Column(name="type",nullable=false)
    private Integer type;
    @Column(name="create_date",columnDefinition="char(19)",nullable=false)
    private String create_date;
    @Column(name="parent",nullable=false)
    private Long parent;
    @Column(name="create_ip",columnDefinition="char(15)",nullable=false)
    private String create_ip;
    @Column(name="mail",columnDefinition="char(32)")
    private String mail;
    @Column(name="status",nullable=false)
    private Integer status;

    public Long getUid() {
        return uid;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getMail() {
        return mail;
    }
    public Long getParent() {
        return parent;
    }
    public Integer getType() {
        return type;
    }
    public Integer getStatus() {
        return status;
    }
    public String getCreate_ip() {
        return create_ip;
    }
    public String getCreate_date() {
        return create_date;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public void setParent(Long parent) {
        this.parent = parent;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setCreate_ip(String create_ip) {
        this.create_ip = create_ip;
    }
    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    // 设置为普通用户
    public void tran2User(String username, String password, String mail, String ip_addr) {
        String date = Util.getDateNow();
        this.setUsername(username);
        this.setPassword(password);
        this.setMail(mail);
        this.setStatus(Constant.RecordStatus.EXIST);
        this.setParent((long)-1);
        this.setType(Constant.UserType.USER);
        this.setCreate_ip(ip_addr);
        this.setCreate_date(date);
    }

    //设置为销售员
    public void tran2Seller(String username, String password,Long parent,String ip_addr) {
        String date = Util.getDateNow();
        this.setUsername(username);
        this.setPassword(password);
        this.setMail(null);
        this.setStatus(Constant.RecordStatus.EXIST);
        this.setParent(parent);
        this.setType(Constant.UserType.SELLER);
        this.setCreate_ip(ip_addr);
        this.setCreate_date(date);
    }

    //设置为管理员(店主)
    public void tran2Admin(String username, String password, String ip_addr) {
        String date = Util.getDateNow();
        this.setUsername(username);
        this.setPassword(password);
        this.setMail(null);
        this.setStatus(Constant.RecordStatus.EXIST);
        this.setParent((long)-2);
        this.setType(Constant.UserType.ADMIN);
        this.setCreate_ip(ip_addr);
        this.setCreate_date(date);
    }
}
