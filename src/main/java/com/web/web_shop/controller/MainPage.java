package com.web.web_shop.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.web.web_shop.DAO.CommodityRepository;
import com.web.web_shop.Tool.Constant;
import com.web.web_shop.Tool.Util;
import com.web.web_shop.beans.APIResult;
import com.web.web_shop.beans.Commodity;
import com.web.web_shop.beans.DataCommodity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * MainPage
 */
@Controller
public class MainPage {
    @Autowired
    private HttpSession session;

    @Autowired
    private CommodityRepository commodityRepository;

    @RequestMapping(value="/")
    public String getMainPage() {
        Integer code = Util.isLogin(session);
        if (code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop";
        return "main_page";
    }

    @RequestMapping(value="/search",method = RequestMethod.GET)
    @ResponseBody
    public APIResult search(
        @RequestParam(value="key", required = false) String key,
        @RequestParam(value = "type", defaultValue = "10") String type) {
        Integer code = Util.isLogin(session);
        if( code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER) {
            return APIResult.createNG("错误操作");
        }
        if(!Util.Certify.certifyNumber(type)) {
            return APIResult.createNG("无该类型");
        }


        List<Commodity> commodities = null;
        if("10".equals(type)) {
            commodities = commodityRepository.searchByKey(key);
        } else {
            commodities = commodityRepository.searchByKeyAndType(key,Integer.parseInt(type));
        }
        List<DataCommodity> data_commodities = Util.tran2DataCommodityList(commodities, null, true);
        System.out.println(data_commodities.size());
        return APIResult.createOK(data_commodities);
    }
}
