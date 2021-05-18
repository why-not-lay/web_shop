package com.web.web_shop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web.web_shop.beans.APIResult;
import com.web.web_shop.beans.OnlineRecord;
import com.web.web_shop.beans.Shop;
import com.web.web_shop.beans.User;
import com.web.web_shop.DAO.UserRepository;
import com.web.web_shop.Tool.Constant;
import com.web.web_shop.Tool.Util;
import com.web.web_shop.DAO.OnlineRecordRepository;
import com.web.web_shop.DAO.ShopRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * CreateShop
 */
@Controller
@RequestMapping(value = "/shop")
public class ShopController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private OnlineRecordRepository onlineRecordRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;

    @RequestMapping(value = "", method= RequestMethod.GET )
    public String getShopPage() {
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.NOT_USER || code == Constant.UserType.USER)
            return "redirect:/";
        return "shop_main";
    }

    @RequestMapping(value = "/certify", method= RequestMethod.GET )
    public String getShopCertify() {
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER)
            return "redirect:/";
        if(code== Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop";
        return "shop_certify";
    }

    @RequestMapping(value="/login",method = RequestMethod.POST)
    public String loginShop(
        @RequestParam(required = false)String username,
        @RequestParam(required = false) String password,
        @RequestParam(required = false) String type) {

        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER)
            return "redirect:/";
        if(code== Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop";

        if(!Util.Certify.certifyUsername(username)) {
            return "shop_certify";
        }
        if(!Util.Certify.certifyPassword(password)) {
            return "shop_certify";
        }
        if(!Util.Certify.certifyNumber(type)) {
            return "shop_certify";
        }

        User user = null;
        if(type.equals("2")) {
            user = userRepository.findByUsernameAndTypeAndStatus(username,Constant.UserType.SELLER,Constant.RecordStatus.EXIST);
        } else if(type.equals("3")) {
            user = userRepository.findByUsernameAndTypeAndStatus(username, Constant.UserType.ADMIN,Constant.RecordStatus.EXIST);
        }

        if(user != null && user.getPassword().equals(password)) {
            OnlineRecord online = new OnlineRecord();
            online.setAfterLogin(user.getUid(),Util.getIpAddr(request), Util.getDateNow());
            session.setAttribute("user", user);
            session.setAttribute("online", online);
            return "shop_main";
        }
        return "shop_certify";
    }

    @RequestMapping(value = "/register", method= RequestMethod.POST )
    public String registerShop(
        @RequestParam(required=false) String username,
        @RequestParam(required=false) String password,
        @RequestParam(required=false) String shopname) {

        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER)
            return "redirect:/";
        if(code== Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop";

        if(!Util.Certify.certifyUsername(username)) {
            return "shop_certify";
        }
        if(!Util.Certify.certifyPassword(password)) {
            return "shop_certify";
        }
        if(!Util.Certify.certifyShopname(shopname)) {
            return "shop_certify";
        }

        User new_user = null;
        User user = userRepository.findByUsernameAndTypeAndStatus(username,Constant.UserType.ADMIN,Constant.RecordStatus.EXIST);
        if(user == null) {
            new_user = new User();
            String ip_addr = Util.getIpAddr(request);
            new_user.tran2Admin(username, password, ip_addr);
            userRepository.save(new_user);

            OnlineRecord online = new OnlineRecord();
            online.setAfterLogin(new_user.getUid(),Util.getIpAddr(request), new_user.getCreate_date());

            Shop shop = new Shop();
            shop.setUid(new_user.getUid());
            shop.setName(shopname);
            shop.setCreate_date(Util.getDateNow());
            shop.setStatus(Constant.RecordStatus.EXIST);
            shopRepository.save(shop);
            session.setAttribute("online", online);
            session.setAttribute("user", new_user);
        } else {
            return "shop_certify";
        }
        return "redirect:/shop/";
    }

    @RequestMapping(value = "logout")
    public String logoutShop() {
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER)
            return "redirect:/";
        if(code == Constant.UserType.NOT_USER)
            return "redirect:/shop/certify";
        OnlineRecord online = (OnlineRecord)session.getAttribute("online");
        online.setBeforeLogout(Util.getIpAddr(request), Util.getDateNow());
        onlineRecordRepository.save(online);
        session.removeAttribute("user");
        session.removeAttribute("online");
        return "redirect:/";
    }

    @RequestMapping(value = "registerSeller", method = RequestMethod.POST)
    @ResponseBody
    public APIResult registerSeller(
        @RequestParam(required=false) String username,
        @RequestParam(required=false) String password) {

        Integer code = Util.isLogin(session);
        if(code != Constant.UserType.ADMIN)
            return APIResult.createNG("错误操作");
        if(username == null || password == null)
            return APIResult.createNG("参数缺失");

        User old_user = userRepository.findByUsernameAndTypeAndStatus(username, Constant.UserType.SELLER,Constant.RecordStatus.EXIST);
        if(old_user != null)
            return APIResult.createOK("重复添加");
        User user = new User();
        User user_parent = (User)session.getAttribute("user");
        String ip_addr = Util.getIpAddr(request);
        user.tran2Seller(username, password,user_parent.getUid(), ip_addr);
        userRepository.save(user);
        return APIResult.createOK(user);
    }
}
