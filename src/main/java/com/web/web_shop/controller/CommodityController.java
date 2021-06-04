package com.web.web_shop.controller;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web.web_shop.DAO.CommodityRepository;
import com.web.web_shop.DAO.OperationRecordRepository;
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
    private CommodityRepository commodityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OperationRecordRepository operationRecordRepository;
    @Autowired
    private HttpSession session;
    @Autowired
    private HttpServletRequest request;


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

        String content = normalizeKeyValuePair("shoppname",shopname);
        content += normalizeKeyValuePair("price",price);
        content += normalizeKeyValuePair("number",number);
        content += normalizeKeyValuePair("description",description);
        content += normalizeKeyValuePair("type",type);

        OperationRecord operation_record = getOperationRecordWithObject();
        operation_record.setMainUid(user.getUid());
        operation_record.setObjectId(commodity.getCid());
        operation_record.setContent(content);
        operation_record.setOperationType(Constant.OPERATION_OBJECT.CREATE);
        operationRecordRepository.save(operation_record);

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

        String content = "";
        if(Util.Certify.certifyShopname(shopname)
                && !shopname.equals(commodity.getName())) {

            commodity.setName(shopname);
            content += normalizeKeyValuePair("shoppname",shopname);
        }
        if(Util.Certify.certifyNumber(price)
                && !price.equals(commodity.getPrice().toString())) {

            commodity.setPrice(Integer.parseInt(price));
            content += normalizeKeyValuePair("price",price);
        }
        if(Util.Certify.certifyNumber(number)
                && !price.equals(commodity.getCurNumber().toString())) {

            Integer cur = commodity.getCurNumber();
            Integer now = Integer.parseInt(number);
            Integer accumulation = now - cur;
            commodity.setCurNumber(now);
            commodity.setTotalNumber(commodity.getTotalNumber()+accumulation);
            content += normalizeKeyValuePair("number",number);
        }
        if(Util.Certify.certifyNumber(type)
                && !type.equals(commodity.getType().toString())) {

            commodity.setType(Integer.parseInt(type));
            content += normalizeKeyValuePair("type",type);
        }
        if(description != null
                && Util.Certify.certifyCommodityDescription(description)
                && !description.equals(commodity.getDescription())) {

            commodity.setDescription(description);
            content += normalizeKeyValuePair("description",description);
        }
        if(Util.Certify.certifyCommodityComStatus(comStatus)) {
            commodity.setComStatus(Integer.parseInt(comStatus));
        }

        commodityRepository.save(commodity);

        OperationRecord operation_record = getOperationRecordWithObject();
        operation_record.setMainUid(user.getUid());
        operation_record.setContent(content);
        operation_record.setObjectId(commodity.getCid());
        operation_record.setOperationType(Constant.OPERATION_OBJECT.UPDATE);
        operationRecordRepository.save(operation_record);

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

        User user = (User)session.getAttribute("user");
        OperationRecord operation_record = getOperationRecordWithObject();
        operation_record.setMainUid(user.getUid());
        operation_record.setContent("");
        operation_record.setObjectId(commodity.getCid());
        operation_record.setOperationType(Constant.OPERATION_OBJECT.DELETE);
        operationRecordRepository.save(operation_record);

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
        } else if(code == Constant.UserType.USER) {
            //普通用户
            Page<Commodity> commodities_container_onSale = null;
            if(commodity_type == 10) {
                Integer favourite_type = user.getFavouriteType();
                if(favourite_type == -1) {
                    commodities_container_onSale = commodityRepository.findByStatusAndComStatus(Constant.RecordStatus.EXIST, Constant.CommodityStatus.ON_SALE, pageable);
                } else {
                    commodities_container_onSale = commodityRepository.findByfavouriteType(favourite_type,pageable);
                }
            } else {
                commodities_container_onSale = commodityRepository.findByStatusAndComStatusAndType(Constant.RecordStatus.EXIST, Constant.CommodityStatus.ON_SALE,commodity_type, pageable);
            }
            List<Commodity> commodities_onSale = commodities_container_onSale.getContent();
            data_commodities = new ArrayList<DataCommodity>();
            data_commodities.addAll(Util.tran2DataCommodityList(commodities_onSale,null,true));

        } else if(code == Constant.UserType.NOT_USER) {
            //非用户
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

        if(code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER) {
            String content = normalizeKeyValuePair("page",page);
            content = normalizeKeyValuePair("number",number);
            content = normalizeKeyValuePair("type",type);
            OperationRecord operation_record = getOperationRecordWithObject();
            operation_record.setMainUid(user.getUid());
            operation_record.setContent(content);
            operation_record.setObjectId(-1L);
            operation_record.setOperationType(Constant.OPERATION_OBJECT.SEARCH);
            operationRecordRepository.save(operation_record);
        }

        return APIResult.createOK(data_commodities);
    }

    private OperationRecord getOperationRecordWithObject() {
        OperationRecord operation_record = new OperationRecord();
        operation_record.setIp(Util.getIpAddr(request));
        operation_record.setDate(Util.getDateNow());
        operation_record.setObjectType(Constant.OBJECT_TYPE.COMMODITY);
        operation_record.setStatus(Constant.RecordStatus.EXIST);
        return operation_record;
    }
    private String normalizeKeyValuePair(String key,String value) {
        return String.format("%s=%s;", key,value);
    }

}
