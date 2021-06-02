package com.web.web_shop.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.web.web_shop.DAO.CommodityRepository;
import com.web.web_shop.DAO.TradeRepository;
import com.web.web_shop.DAO.UserRepository;
import com.web.web_shop.DAO.ViewRecordRepository;
import com.web.web_shop.Tool.Constant;
import com.web.web_shop.Tool.Util;
import com.web.web_shop.beans.APIResult;
import com.web.web_shop.beans.Commodity;
import com.web.web_shop.beans.DataTrade;
import com.web.web_shop.beans.DataView;
import com.web.web_shop.beans.Trade;
import com.web.web_shop.beans.User;
import com.web.web_shop.beans.ViewRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    UserRepository userRepository;

    @Autowired
    TradeRepository tradeRepository;

    @Autowired
    HttpSession session;

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
        Page<ViewRecord> trade_container = viewRecordRepository.findByUidSellerAndStatus(seller.getUid(), Constant.RecordStatus.EXIST, pageable);
        List<ViewRecord> view_records = trade_container.getContent();
        List<DataView> data_views = new ArrayList<DataView>();
        for(ViewRecord view : view_records) {
            User user = userRepository.findByUid(view.getUid());
            Commodity commodity = commodityRepository.findByCid(view.getCid());
            data_views.add(Util.tran2DataView(view, commodity.getName(), user.getUsername()));
        }
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
        return APIResult.createOK(data_trades);
    }

}
