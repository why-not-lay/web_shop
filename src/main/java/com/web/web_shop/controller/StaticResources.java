package com.web.web_shop.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.web.web_shop.DAO.CommodityRepository;
import com.web.web_shop.Tool.Constant;
import com.web.web_shop.Tool.Util;
import com.web.web_shop.beans.APIResult;
import com.web.web_shop.beans.Commodity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


/**
 * StaticResources
 */
@Controller
@RequestMapping(value = "/resources")
public class StaticResources {
    @Autowired
    private HttpSession session;
    @Autowired
    private CommodityRepository commodityRepository;
    @Value("${web.uploadDir}")
    private String uploadDir;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @ResponseBody
    public APIResult uploadFile(@RequestParam(value="pic", required=false) MultipartFile upload_file, @RequestParam(value="cid", required = false) String cid) {
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER || code == Constant.UserType.NOT_USER)
            return APIResult.createNG("错误操作");
        if(code == Constant.UserType.ADMIN)
            return APIResult.createNG("错误操作");

        if(!Util.Certify.certifyNumber(cid))
            return APIResult.createNG("参数错误");
        if(upload_file.isEmpty())
            return APIResult.createNG("参数错误");
        Commodity commodity = commodityRepository.findByCidAndStatus(Long.parseLong(cid),Constant.RecordStatus.EXIST);
        if(commodity == null)
            return APIResult.createNG("参数错误");

        String path = uploadDir + commodity.getPicDir() + "/" + upload_file.getOriginalFilename();
        return APIResult.createNG("上传失败");
    }

    @RequestMapping(value = "/pic",method = RequestMethod.GET)
    public String getPicFile(@RequestParam(value = "cid",required=false)String cid,HttpServletRequest request, HttpServletResponse response) {
        //if(!Util.Certify.certifyNumber(cid))
        //    return null;
        //Integer code = Util.isLogin(session);
        //if(code == Constant.UserType.USER || code == Constant.UserType.NOT_USER)
        //    return null;
        //if(code == Constant.UserType.ADMIN)
        //    return null;
        //Commodity commodity = commodityRepository.findByCidAndStatus(Long.parseLong(cid),Constant.RecordStatus.EXIST);
        //if(commodity == null)
        //    return null;


        return null;
    }

}
