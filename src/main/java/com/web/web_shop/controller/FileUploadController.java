package com.web.web_shop.controller;

import javax.servlet.http.HttpSession;

import com.web.web_shop.DAO.CommodityRepository;
import com.web.web_shop.Exception.StorageFileNotFoundException;
import com.web.web_shop.Service.StorageService;
import com.web.web_shop.Tool.Constant;
import com.web.web_shop.Tool.Util;
import com.web.web_shop.beans.APIResult;
import com.web.web_shop.beans.Commodity;

import org.apache.catalina.loader.ResourceEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileUploadController
 */
@Controller
public class FileUploadController {

    @Autowired
    CommodityRepository commodityRepository;

    @Autowired()
    private StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value = "/pic/upload", method = RequestMethod.POST)
    @ResponseBody
    public APIResult handleFileUpload(
        @RequestParam("file") MultipartFile file,
        @RequestParam("cid") String cid,
        HttpSession session) {
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER || code == Constant.UserType.NOT_USER)
            return APIResult.createNG("错误操作");
        if(!Util.Certify.certifyNumber(cid)) {
            return APIResult.createNG("格式错误");
        }

        String content_type = file.getContentType();
        if("image/png".equals(content_type) || "image/jpg".equals(content_type) ||"image/jpeg".equals(content_type)) {
            Commodity commodity = commodityRepository.findByCidAndStatus(Long.parseLong(cid), Constant.RecordStatus.EXIST);
            if(commodity == null) return APIResult.createNG("格式错误");
            String pathname = commodity.getPicDir() + '/' + file.getOriginalFilename();
            storageService.store(file,pathname);
        } else
            return APIResult.createNG("格式错误");
        return APIResult.createOK("上传成功");
    }

    @RequestMapping(value = "/pic/get",method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> serverFile(@RequestParam(value = "cid") String cid) {
        Commodity commodity = null;
        if(Util.Certify.certifyNumber(cid)) {
            commodity = commodityRepository.findByCidAndStatus(Long.parseLong(cid), Constant.RecordStatus.EXIST);
        }
        String dirname = "";
        if(commodity != null) {
            dirname = commodity.getPicDir();
        }
        Resource file = storageService.loadAsResourceByDirname(dirname);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFoun(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
