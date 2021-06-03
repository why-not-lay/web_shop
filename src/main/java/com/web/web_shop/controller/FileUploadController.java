package com.web.web_shop.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web.web_shop.DAO.CommodityRepository;
import com.web.web_shop.DAO.OperationRecordRepository;
import com.web.web_shop.Exception.StorageFileNotFoundException;
import com.web.web_shop.Service.StorageService;
import com.web.web_shop.Tool.Constant;
import com.web.web_shop.Tool.Util;
import com.web.web_shop.beans.APIResult;
import com.web.web_shop.beans.Commodity;
import com.web.web_shop.beans.OperationRecord;
import com.web.web_shop.beans.User;

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
    @Autowired
    OperationRecordRepository operationRecordRepository;
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpSession session;

    @Autowired()
    private StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value = "/pic/upload", method = RequestMethod.POST)
    public String handleFileUpload(
        @RequestParam("file") MultipartFile file,
        @RequestParam("cid") String cid) {
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER || code == Constant.UserType.NOT_USER)
            return "redirect:/";
        if(!Util.Certify.certifyNumber(cid)) {
            return "redirect:/shop";
        }

        String content_type = file.getContentType();
        if("image/png".equals(content_type) || "image/jpg".equals(content_type) ||"image/jpeg".equals(content_type)) {
            Commodity commodity = commodityRepository.findByCidAndStatus(Long.parseLong(cid), Constant.RecordStatus.EXIST);
            if(commodity == null) return "redirect:/shop";
            String pathname = commodity.getPicDir() + '/' + file.getOriginalFilename();
            storageService.store(file,pathname);

            String content = normalizeKeyValuePair("cid",cid);
            content += normalizeKeyValuePair("filename",file.getOriginalFilename());
            User user = (User)session.getAttribute("user");
            OperationRecord operation_record = getOperationRecordWithNone();
            operation_record.setMainUid(user.getUid());
            operation_record.setContent(content);
            operation_record.setOperationType(Constant.OPERATION_NONE.UPLOAD_PIC);
            operationRecordRepository.save(operation_record);
        } else {
            return "redirect:/shop";
        }
        return "redirect:/shop";
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

        User user = (User)session.getAttribute("user");
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.ADMIN || code == Constant.UserType.SELLER) {
            String content = normalizeKeyValuePair("cid",cid);
            OperationRecord operation_record = getOperationRecordWithNone();
            operation_record.setMainUid(user.getUid());
            operation_record.setOperationType(Constant.OPERATION_NONE.GET_PIC);
            operation_record.setContent(content);
            operationRecordRepository.save(operation_record);
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFoun(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    private OperationRecord getOperationRecordWithNone() {
        OperationRecord operation_record = new OperationRecord();
        operation_record.setIp(Util.getIpAddr(request));
        operation_record.setDate(Util.getDateNow());
        operation_record.setObjectId(-1L);
        operation_record.setObjectType(Constant.OBJECT_TYPE.NONE);
        operation_record.setStatus(Constant.RecordStatus.EXIST);
        return operation_record;
    }
    private String normalizeKeyValuePair(String key,String value) {
        return String.format("%s=%s;", key,value);
    }


}
