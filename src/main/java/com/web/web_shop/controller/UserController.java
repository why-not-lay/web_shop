package com.web.web_shop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.web.web_shop.DAO.CommodityRepository;
import com.web.web_shop.DAO.OnlineRecordRepository;
import com.web.web_shop.DAO.UserRepository;
import com.web.web_shop.Tool.Constant;
import com.web.web_shop.Tool.Util;
import com.web.web_shop.beans.APIResult;
import com.web.web_shop.beans.Commodity;
import com.web.web_shop.beans.DataCommodity;
import com.web.web_shop.beans.DataOrder;
import com.web.web_shop.beans.OnlineRecord;
import com.web.web_shop.beans.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * UserController
 */
@Controller
@RequestMapping(value="/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private OnlineRecordRepository onlineRecordRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;

    @RequestMapping(value = "/certify",method = RequestMethod.GET)
    public String getCertityPage() {
        Integer code = Util.isLogin(session);
        if (code == Constant.UserType.USER)
            return "redirect:/";
        if (code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop";
        return "certify_user";
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(
        @RequestParam(required = false)String username,
        @RequestParam(required = false) String password) {

        Integer code = Util.isLogin(session);
        if (code == Constant.UserType.USER)
            return "redirect:/";
        if (code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop";

        if(!Util.Certify.certifyUsername(username)) {
            return "login";
        }
        if(!Util.Certify.certifyPassword(password)) {
            return "login";
        }
        User user = userRepository.findByUsernameAndTypeAndStatus(username,Constant.UserType.USER,Constant.RecordStatus.EXIST);
        if(user == null) {
            return "login";
        }
        if(user.getPassword().equals(password)) {
            OnlineRecord online = new OnlineRecord();
            online.setAfterLogin(user.getUid(),Util.getIpAddr(request), Util.getDateNow());
            session.setAttribute("user", user);
            session.setAttribute("online", online);

            return "redirect:/";
        }
        return "login";
    }


    //@RequestMapping(value="/register",method=RequestMethod.GET)
    //public String getRegisterPage() {
    //    Integer code = Util.isLogin(session);
    //    if (code == Constant.UserType.USER)
    //        return "redirect:/";
    //    if (code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
    //        return "redirect:/shop";
    //    return "register";
    //}

    @RequestMapping(value="/register",method=RequestMethod.POST)
    public String register(
        @RequestParam(required=false) String username,
        @RequestParam(required=false) String password,
        @RequestParam(required=false) String mail) {

        Integer code = Util.isLogin(session);
        if (code == Constant.UserType.USER)
            return "redirect:/";
        if (code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop";

        if(!Util.Certify.certifyUsername(username)) {
            return "register";
        }
        if(!Util.Certify.certifyPassword(password)) {
            return "register";
        }
        if(!Util.Certify.certifyMail(mail)) {
            return "register";
        }
        User new_user = null;
        User user = userRepository.findByUsernameAndTypeAndStatus(username,Constant.UserType.USER,Constant.RecordStatus.EXIST);
        if(user == null) {
            new_user = new User();
            String ip_addr = Util.getIpAddr(request);
            new_user.tran2User(username, password, mail, ip_addr);
            userRepository.save(new_user);

            OnlineRecord online = new OnlineRecord();
            online.setAfterLogin(new_user.getUid(),new_user.getCreate_ip(), new_user.getCreate_date());
            session.setAttribute("user", new_user);
            session.setAttribute("online", online);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "logout")
    public String logout() {
        Integer code = Util.isLogin(session);
        if (code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop/";
        if (code == Constant.UserType.USER) {
            OnlineRecord online = (OnlineRecord)session.getAttribute("online");
            online.setBeforeLogout(Util.getIpAddr(request), Util.getDateNow());
            onlineRecordRepository.save(online);
            session.removeAttribute("user");
            session.removeAttribute("online");
        }
        return "redirect:/";
    }

    @RequestMapping(value = "order",method = RequestMethod.GET)
    public String order(
        @CookieValue(value = "order",defaultValue = "") String json,
        HttpServletResponse response,
        ModelMap modelMap) {
        Integer code = Util.isLogin(session);
        if (code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop/";
        if (code == Constant.UserType.NOT_USER)
            return "redirect:/";

        ObjectMapper mapper = new ObjectMapper();
        CollectionType java_type = mapper.getTypeFactory().constructCollectionType(List.class, DataOrder.class);
        List<DataOrder> orders = null;
        try {
            orders = mapper.readValue(json, java_type);
        } catch(Exception e) {
            e.printStackTrace();
        }
        //DataOrder[] orders = mapper.readValue(order,DataOrder[].class);
        for(DataOrder order: orders) {
            System.out.println(order.getCid());
            System.out.println(order.getNumber());
        }

        List<Commodity> commodities = new ArrayList<Commodity>();
        Integer total = 0;

        for(DataOrder order : orders) {
            Commodity commodity = commodityRepository.findByCidAndStatusAndComStatus(order.getCid(), Constant.RecordStatus.EXIST, Constant.CommodityStatus.ON_SALE);
            if(commodity == null) {
                return "redirect:/";
            }
            if(commodity.getCurNumber() < order.getNumber()) {
                return "redirect:/";
            }
            commodity.setCurNumber(order.getNumber());
            total +=order.getNumber() * commodity.getPrice();
            commodities.add(commodity);
        }
        modelMap.addAttribute("order", commodities);
        modelMap.addAttribute("total", total);

        Cookie cookie = new Cookie("order",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "order";
    }

    @RequestMapping(value = "finishTrade",method = RequestMethod.GET)
    public String finishTrade() {
        Integer code = Util.isLogin(session);
        if (code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop/";
        if (code == Constant.UserType.NOT_USER)
            return "redirect:/";
        return "message";
    }

}
