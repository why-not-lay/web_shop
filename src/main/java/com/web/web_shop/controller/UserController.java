package com.web.web_shop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web.web_shop.DAO.CommodityRepository;
import com.web.web_shop.DAO.OnlineRecordRepository;
import com.web.web_shop.DAO.UserRepository;
import com.web.web_shop.Tool.Constant;
import com.web.web_shop.Tool.Util;
import com.web.web_shop.beans.APIResult;
import com.web.web_shop.beans.Commodity;
import com.web.web_shop.beans.DataCommodity;
import com.web.web_shop.beans.OnlineRecord;
import com.web.web_shop.beans.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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


    @RequestMapping(value="/login",method =RequestMethod.GET)
    public String getLoginPage() {
        // 判断是否已经登录，已经登录则重定向
        Integer code = Util.isLogin(session);
        if (code == Constant.UserType.USER)
            return "redirect:/";
        if (code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop";

        return "login";
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


    @RequestMapping(value="/register",method=RequestMethod.GET)
    public String getRegisterPage() {
        Integer code = Util.isLogin(session);
        if (code == Constant.UserType.USER)
            return "redirect:/";
        if (code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop";
        return "register";
    }

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

    @RequestMapping(value = "commodity", method = RequestMethod.POST)
    public String getCommodityPage(@RequestParam(required = false) String cid, ModelMap model) {
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.ADMIN || code ==Constant.UserType.SELLER)
            return "/shop/";

        DataCommodity data_commodity = null;
        Commodity commodity_onSale = commodityRepository.findByCidAndStatusAndComStatus(Long.parseLong(cid), Constant.RecordStatus.EXIST, Constant.CommodityStatus.ON_SALE);
        Commodity commodity_outOfStock = commodityRepository.findByCidAndStatusAndComStatus(Long.parseLong(cid), Constant.RecordStatus.EXIST, Constant.CommodityStatus.OUT_OF_STOCK);
        if(commodity_onSale != null) {
            data_commodity = Util.tran2DataCommodity(commodity_onSale);
        }
        if(commodity_outOfStock != null) {
            data_commodity = Util.tran2DataCommodity(commodity_outOfStock);
        }
        if(data_commodity == null)
            return "redirect:/";
        model.addAttribute("commodity", data_commodity);
        return "commodity";
    }
}
