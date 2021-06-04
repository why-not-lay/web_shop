package com.web.web_shop.Service;

import java.util.HashMap;
import java.util.List;

import com.web.web_shop.DAO.CommodityRepository;
import com.web.web_shop.DAO.TradeRepository;
import com.web.web_shop.DAO.UserRepository;
import com.web.web_shop.DAO.ViewRecordRepository;
import com.web.web_shop.Tool.Constant;
import com.web.web_shop.beans.Commodity;
import com.web.web_shop.beans.Trade;
import com.web.web_shop.beans.User;
import com.web.web_shop.beans.ViewRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.View;

/**
 * DataExtract
 */
@Service
public class DataExtract {

    @Autowired
    private  TradeRepository tradeRepository;
    @Autowired
    private  CommodityRepository commodityRepository;
    @Autowired
    private  ViewRecordRepository viewRecordRepository;
    @Autowired
    private  UserRepository userRepository;

    public void userDataExtract(Long uid) {
        User user = userRepository.findByUid(uid);
        Integer type = getFavouriteType(uid);
        user.setFavouriteType(type);
        userRepository.save(user);
    }

    private Integer getFavouriteType(Long uid) {
        List<Trade> trades = tradeRepository.findByBuyerUid(uid);
        List<ViewRecord> views = viewRecordRepository.findByUid(uid);
        Integer favourite_type = -1;
        Integer favourite_type_count = 0;
        HashMap<Integer,Integer> type_count = new HashMap<Integer,Integer>();
        for(Trade trade: trades) {
            Integer type = getCommodityTypeByCid(trade.getCid());
            if(type != -1 || type != Constant.COMMODITY_TYPE.UNFLITER) {
                if(type_count.containsKey(type)) {
                    Integer count = type_count.get(type);
                    type_count.put(type,count+1);
                } else {
                    type_count.put(type,1);
                }
            }
        }
        for(ViewRecord view : views) {
            Integer type = getCommodityTypeByCid(view.getCid());
            if(type != -1 || type != Constant.COMMODITY_TYPE.UNFLITER) {
                if(type_count.containsKey(type)) {
                    Integer count = type_count.get(type);
                    type_count.put(type,count+1);
                } else {
                    type_count.put(type,1);
                }
            }
        }
        for(Integer type : type_count.keySet()) {
            if(type_count.get(type) > favourite_type_count) {
                favourite_type = type;
                favourite_type_count = type_count.get(type);
            }
        }
        return favourite_type;
    }

    private Integer getCommodityTypeByCid(Long cid) {
        Commodity commodity = commodityRepository.findByCid(cid);
        if(commodity == null) {
            return -1;
        }
        return commodity.getType();
    }

}
