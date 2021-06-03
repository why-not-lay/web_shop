package com.web.web_shop.controller;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web.web_shop.DAO.CommodityRepository;
import com.web.web_shop.DAO.UserRepository;
import com.web.web_shop.Tool.Constant;
import com.web.web_shop.Tool.Util;
import com.web.web_shop.beans.APIResult;
import com.web.web_shop.beans.Commodity;
import com.web.web_shop.beans.DataCommodity;
import com.web.web_shop.beans.OperationRecord;
import com.web.web_shop.beans.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * CommodityController
 */
@RestController
@RequestMapping(value = "/commodity")
public class CommodityController {

    @Autowired
    CommodityRepository commodityRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HttpSession session;
    @Autowired
    HttpServletRequest request;


    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public APIResult addCommodity(
        @RequestParam(required = false) String shopname,
        @RequestParam(required = false) String price,
        @RequestParam(required = false) String number,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) String type) {

        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER || code == Constant.UserType.NOT_USER)
            return APIResult.createNG("错误操作");
        if(code == Constant.UserType.ADMIN)
            return APIResult.createNG("错误操作");

        if(!Util.Certify.certifyShopname(shopname))
            return APIResult.createNG("商品格式有误");
        if(!Util.Certify.certifyNumber(price))
            return APIResult.createNG("价格格式有误");
        if(!Util.Certify.certifyNumber(number))
            return APIResult.createNG("数目格式有误");
        if(!Util.Certify.certifyNumber(type))
            return APIResult.createNG("种类格式有误");
        if(!Util.Certify.certifyCommodityDescription(description))
            return APIResult.createNG("描述格式有误");


        User user = (User)session.getAttribute("user");

        Commodity commodity = new Commodity();
        commodity.setUid(user.getUid());
        commodity.setName(shopname);
        commodity.setType(Integer.parseInt(type));
        commodity.setPrice(Integer.parseInt(price));
        commodity.setStatus(Constant.RecordStatus.EXIST);
        commodity.setComStatus(Constant.CommodityStatus.OFF_SALE);
        commodity.setCurNumber(Integer.parseInt(number));
        commodity.setTotalNumber(Integer.parseInt(number));
        commodity.setDescription(description);
        commodity.setPicDir(DigestUtils.md5DigestAsHex(shopname.getBytes()));
        commodityRepository.save(commodity);


        return APIResult.createOKMessage("添加成功");
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public APIResult updateCommodity(
        @RequestParam(required = false) String cid,
        @RequestParam(required = false) String shopname,
        @RequestParam(required = false) String price,
        @RequestParam(required = false) String number,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String comStatus) {

        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER || code == Constant.UserType.NOT_USER)
            return APIResult.createNG("错误操作");
        if(code == Constant.UserType.ADMIN)
            return APIResult.createNG("错误操作");

        if(!Util.Certify.certifyNumber(cid))
            return APIResult.createNG("格式错误");
        Commodity commodity = commodityRepository.findByCidAndStatus(Long.parseLong(cid), Constant.RecordStatus.EXIST);
        if(commodity == null)
            return APIResult.createNG("没有该商品");
        User user = (User)session.getAttribute("user");
        if(user.getUid() != commodity.getUid())
            return APIResult.createNG("错误操作");

        if(Util.Certify.certifyShopname(shopname)) {
            commodity.setName(shopname);
        }
        if(Util.Certify.certifyNumber(price)) {
            commodity.setPrice(Integer.parseInt(price));
        }
        if(Util.Certify.certifyNumber(number)) {
            Integer cur = commodity.getCurNumber();
            Integer now = Integer.parseInt(number);
            Integer accumulation = now - cur;
            commodity.setCurNumber(now);
            commodity.setTotalNumber(commodity.getTotalNumber()+accumulation);
        }
        if(Util.Certify.certifyNumber(type)) {
            commodity.setType(Integer.parseInt(type));
        }
        if(description != null && Util.Certify.certifyCommodityDescription(description)) {
            commodity.setDescription(description);
        }
        if(Util.Certify.certifyCommodityComStatus(comStatus)) {
            commodity.setComStatus(Integer.parseInt(comStatus));
        }

        commodityRepository.save(commodity);

        return APIResult.createOKMessage("修改成功");
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public APIResult deleteCommodity(@RequestParam(required = false) String cid) {
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER || code == Constant.UserType.NOT_USER)
            return APIResult.createNG("错误操作");
        if(code == Constant.UserType.ADMIN)
            return APIResult.createNG("错误操作");

        if(!Util.Certify.certifyNumber(cid))
            return APIResult.createNG("格式错误");

        Commodity commodity = commodityRepository.findByCidAndStatus(Long.parseLong(cid), Constant.RecordStatus.EXIST);
        if(commodity == null)
            return APIResult.createNG("没有该商品");

        commodityRepository.updateStatusByCid(Long.parseLong(cid),Constant.RecordStatus.DELETED);

        return APIResult.createOKMessage("删除成功");
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public APIResult getCommodity(
        @RequestParam(required = false,defaultValue = "0") String page,
        @RequestParam(required = false,defaultValue = "10") String number,
        @RequestParam(required = false,defaultValue = "10") String type) {

        if(!Util.Certify.certifyNumber(page))
            return APIResult.createNG("格式错误");
        if(!Util.Certify.certifyNumber(number))
            return APIResult.createNG("格式错误");
        if(!Util.Certify.certifyNumber(type))
            return APIResult.createNG("格式错误");

        Integer code = Util.isLogin(session);
        List<Commodity> commodities = null;
        List<DataCommodity> data_commodities = null;
        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(number));
        User user = (User)session.getAttribute("user");
        Integer commodity_type = Integer.parseInt(type);

        if(code == Constant.UserType.SELLER) {
            //卖家
            Page<Commodity> commodities_container = commodityRepository.findByUidAndStatus(user.getUid(), Constant.RecordStatus.EXIST, pageable);
            commodities = commodities_container.getContent();
            data_commodities = Util.tran2DataCommodityList(commodities,null,false);
        } else if(code == Constant.UserType.ADMIN) {
            //管理员
            List<User> sellers = userRepository.findByParentAndStatus(user.getUid(),Constant.RecordStatus.EXIST);
            data_commodities = new ArrayList<DataCommodity>();
            for(User seller : sellers) {
                Page<Commodity> commodities_container = commodityRepository.findByUidAndStatus(seller.getUid(), Constant.RecordStatus.EXIST, pageable);
                commodities = commodities_container.getContent();
                List<DataCommodity> part_data_comdities = Util.tran2DataCommodityList(commodities,user.getUsername(),false);
                for(DataCommodity data_commodity: part_data_comdities) {
                    data_commodities.add(data_commodity);
                }
            }
        } else {
            //用户
            Page<Commodity> commodities_container_onSale = null;
            if(commodity_type == 10) {
                commodities_container_onSale = commodityRepository.findByStatusAndComStatus(Constant.RecordStatus.EXIST, Constant.CommodityStatus.ON_SALE, pageable);
            } else {
                commodities_container_onSale = commodityRepository.findByStatusAndComStatusAndType(Constant.RecordStatus.EXIST, Constant.CommodityStatus.ON_SALE,commodity_type, pageable);
            }
            List<Commodity> commodities_onSale = commodities_container_onSale.getContent();
            data_commodities = new ArrayList<DataCommodity>();
            data_commodities.addAll(Util.tran2DataCommodityList(commodities_onSale,null,true));
        }
        return APIResult.createOK(data_commodities);
    }

    private OperationRecord getOperationRecordWithObject() {
        OperationRecord operation_record = new OperationRecord();
        operation_record.setIp(Util.getIpAddr(request));
        operation_record.setDate(.);
        return operation_record;
    }

}
