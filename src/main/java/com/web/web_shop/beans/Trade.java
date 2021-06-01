package com.web.web_shop.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="trade")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;
    @Column(name = "buyer_uid",nullable=false)
    private Long buyerUid;
    @Column(name = "seller_uid",nullable=false)
    private Long sellerUid;
    @Column(name="cid",nullable=false)
    private Long cid;
    @Column(name="price",nullable=false)
    private Integer price;
    @Column(name = "number",nullable = false)
    private Integer number;
    @Column(name = "start_date",columnDefinition = "char(19)",nullable = false)
    private String startDate;
    @Column(name = "finish_date",columnDefinition = "char(19)")
    private String finishDate;
    @Column(name = "status",nullable = false)
    private Integer status;
    @Column(name = "trade_ip",columnDefinition = "char(15)",nullable = false)
    private String tradeIp;
    @Column(name = "total",nullable = false)
    private Integer total;
    @Column(name = "finished",nullable = false)
    private Integer finished;
    @Column(name="name",nullable=false)
    private String name;

    public String getName() {
        return name;
    }
    public Long getCid() {
        return cid;
    }
    public Long getTid() {
        return tid;
    }
    public String getStartDate() {
        return startDate;
    }
    public Long getBuyerUid() {
        return buyerUid;
    }
    public Integer getPrice() {
        return price;
    }
    public Integer getTotal() {
        return total;
    }
    public Integer getNumber() {
        return number;
    }
    public Long getSellerUid() {
        return sellerUid;
    }
    public Integer getStatus() {
        return status;
    }
    public String getTradeIp() {
        return tradeIp;
    }
    public Integer getFinished() {
        return finished;
    }
    public String getFinishDate() {
        return finishDate;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }
    public void setTid(Long tid) {
        this.tid = tid;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public void setTotal(Integer total) {
        this.total = total;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public void setBuyerUid(Long buyerUid) {
        this.buyerUid = buyerUid;
    }
    public void setTradeIp(String tradeIp) {
        this.tradeIp = tradeIp;
    }
    public void setSellerUid(Long sellerUid) {
        this.sellerUid = sellerUid;
    }
    public void setFinished(Integer finished) {
        this.finished = finished;
    }
    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }
    public void setName(String name) {
        this.name = name;
    }
}
