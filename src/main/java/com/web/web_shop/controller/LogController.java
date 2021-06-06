package com.web.web_shop.controller;

import java.util.List;

import com.web.web_shop.DAO.OnlineRecordRepository;
import com.web.web_shop.DAO.OperationRecordRepository;
import com.web.web_shop.beans.OnlineRecord;
import com.web.web_shop.beans.OperationRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * LogController
 */
@Controller
@RequestMapping(value="/log")
public class LogController {

    @Autowired
    OnlineRecordRepository onlineRecordRepository;
    @Autowired
    OperationRecordRepository operationRecordRepository;

    @RequestMapping(value = "/online",method = RequestMethod.GET)
    public String getOnlineLog(ModelMap modelMap) {
        List<OnlineRecord> online_records = onlineRecordRepository.findAll();
        modelMap.addAttribute("online_records",online_records);
        return "online_record";
    }

    @RequestMapping(value = "/operation",method = RequestMethod.GET)
    public String getOperationRecord(ModelMap modelMap) {
        List<OperationRecord> operation_records = operationRecordRepository.getRecords();
        modelMap.addAttribute("operation_records",operation_records);
        return "operation_record";
    }
}
