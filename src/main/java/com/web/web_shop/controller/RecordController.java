package com.web.web_shop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web.web_shop.DAO.CommodityRepository;
import com.web.web_shop.DAO.OperationRecordRepository;
import com.web.web_shop.DAO.TradeRepository;
import com.web.web_shop.DAO.UserRepository;
import com.web.web_shop.DAO.ViewRecordRepository;
import com.web.web_shop.Tool.Constant;
import com.web.web_shop.Tool.Util;
import com.web.web_shop.beans.APIResult;
import com.web.web_shop.beans.Commodity;
import com.web.web_shop.beans.DataTrade;
import com.web.web_shop.beans.DataView;
import com.web.web_shop.beans.OperationRecord;
import com.web.web_shop.beans.Trade;
import com.web.web_shop.beans.User;
import com.web.web_shop.beans.ViewRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * RecordController
 */
@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    ViewRecordRepository viewRecordRepository;

    @Autowired
    CommodityRepository commodityRepository;

    @Autowired
    OperationRecordRepository operationRecordRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    HttpSession session;

    @Autowired
    HttpServletRequest request;

    @RequestMapping(value="/view",method = RequestMethod.GET)
    public String fetchViewRecord(
        @CookieValue(value ="vc") String view_cid,
        @CookieValue(value = "vb") String view_start,
        @CookieValue(value = "ve") String view_end) {

        if("".equals(view_cid) || "".equals(view_start) || "".equals(view_end)) {
            return null;
        }
        Long cid = Long.parseLong(view_cid);
        Long start = Long.parseLong(view_start);
        Long end = Long.parseLong(view_end);

        System.out.println(cid);
        System.out.println(start);
        System.out.println(end);

        Commodity commodity = commodityRepository.findByCid(cid);

        String ip = Util.getIpAddr(request);
        ViewRecord view_record = new ViewRecord();
        view_record.setCid(cid);
        view_record.setUidSeller(commodity.getUid());
        view_record.setStatus(Constant.RecordStatus.EXIST);
        view_record.setOutIp(ip);
        view_record.setEnterIp(ip);
        view_record.setOutTimestamp(end);
        view_record.setEnterTimestamp(start);

        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER) {
            User user = (User)session.getAttribute("user");
            view_record.setUid(user.getUid());
        } else if(code == Constant.UserType.NOT_USER) {
            view_record.setUid(-1L);
        }
        viewRecordRepository.save(view_record);
        return null;
    }

    @RequestMapping(value = "/view/get",method = RequestMethod.GET)
    public APIResult getViewRecord(
        @RequestParam(value = "page",defaultValue = "0") String page,
        @RequestParam(value = "number",defaultValue = "10")String number) {
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER || code == Constant.UserType.NOT_USER)
            return APIResult.createNG("错误操作");
        if(code == Constant.UserType.ADMIN)
            return APIResult.createNG("错误操作");

        if(!Util.Certify.certifyNumber(page))
            return APIResult.createNG("格式错误");
        if(!Util.Certify.certifyNumber(number))
            return APIResult.createNG("格式错误");
        User seller = (User)session.getAttribute("user");
        //if(!Util.Certify.certifyNumber(uid))
        //    return APIResult.createNG("格式错误");
        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(number));
        Page<ViewRecord> view_container = viewRecordRepository.findByUidSellerAndStatus(seller.getUid(), Constant.RecordStatus.EXIST, pageable);
        List<ViewRecord> view_records = view_container.getContent();
        List<DataView> data_views = new ArrayList<DataView>();
        for(ViewRecord view : view_records) {
            User user = userRepository.findByUid(view.getUid());
            Commodity commodity = commodityRepository.findByCid(view.getCid());
            data_views.add(Util.tran2DataView(view, commodity.getName(), user.getUsername()));
        }

        String content = normalizeKeyValuePair("page",page);
        content += normalizeKeyValuePair("number",number);
        OperationRecord operation_record = getOperationRecordWithNone();
        operation_record.setMainUid(seller.getUid());
        operation_record.setOperationType(Constant.OPERATION_NONE.GET_VIEW);
        operation_record.setContent(content);
        operationRecordRepository.save(operation_record);

        return APIResult.createOK(data_views);
    }

    @RequestMapping(value = "/trade/get",method = RequestMethod.GET)
    public APIResult getTradeRecord(
        @RequestParam(value = "page",defaultValue = "0") String page,
        @RequestParam(value = "number",defaultValue = "10")String number) {
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER || code == Constant.UserType.NOT_USER)
            return APIResult.createNG("错误操作");

        if(!Util.Certify.certifyNumber(page))
            return APIResult.createNG("格式错误");
        if(!Util.Certify.certifyNumber(number))
            return APIResult.createNG("格式错误");

        User seller = (User)session.getAttribute("user");
        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(number));
        Page<Trade> trade_container = tradeRepository.findBySellerUidAndStatusAndFinished(seller.getUid(),Constant.RecordStatus.EXIST, Constant.Finished.FINISH,pageable);
        List<Trade> trades = trade_container.getContent();
        List<DataTrade> data_trades = new ArrayList<DataTrade>();
        for(Trade trade : trades) {
            User user = userRepository.findByUid(trade.getBuyerUid());
            data_trades.add(Util.tran2DataTrade(trade,user.getUsername()));
        }

        String content = normalizeKeyValuePair("page",page);
        content += normalizeKeyValuePair("number",number);
        OperationRecord operation_record = getOperationRecordWithNone();
        operation_record.setMainUid(seller.getUid());
        operation_record.setOperationType(Constant.OPERATION_NONE.GET_TRADE);
        operation_record.setContent(content);
        operationRecordRepository.save(operation_record);

        return APIResult.createOK(data_trades);
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
