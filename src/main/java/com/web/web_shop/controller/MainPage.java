package com.web.web_shop.controller;

import javax.servlet.http.HttpSession;

import com.web.web_shop.Tool.Constant;
import com.web.web_shop.Tool.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * MainPage
 */
@Controller
public class MainPage {
    @Autowired
    private HttpSession session;

    @RequestMapping(value="/")
    public String getMainPage() {
        Integer code = Util.isLogin(session);
        if (code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop";
        return "main_page";
    }

}
