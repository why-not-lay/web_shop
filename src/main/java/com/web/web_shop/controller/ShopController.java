package com.web.web_shop.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web.web_shop.beans.APIResult;
import com.web.web_shop.beans.Commodity;
import com.web.web_shop.beans.DataGrade;
import com.web.web_shop.beans.DataSeller;
import com.web.web_shop.beans.OnlineRecord;
import com.web.web_shop.beans.OperationRecord;
import com.web.web_shop.beans.Shop;
import com.web.web_shop.beans.Trade;
import com.web.web_shop.beans.User;
import com.web.web_shop.DAO.UserRepository;
import com.web.web_shop.Tool.Constant;
import com.web.web_shop.Tool.Util;
import com.web.web_shop.DAO.CommodityRepository;
import com.web.web_shop.DAO.OnlineRecordRepository;
import com.web.web_shop.DAO.OperationRecordRepository;
import com.web.web_shop.DAO.ShopRepository;
import com.web.web_shop.DAO.TradeRepository;

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
    private TradeRepository tradeRepository;
    @Autowired
    private CommodityRepository commodityRepository;
    @Autowired
    private OnlineRecordRepository onlineRecordRepository;
    @Autowired
    private OperationRecordRepository operationRecordRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;

    @RequestMapping(value = "", method= RequestMethod.GET )
    public String getShopPage(ModelMap modelMap) {
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.NOT_USER  )
            return "redirect:/shop/certify";
        if(code == Constant.UserType.USER)
            return "redirect:/";
        User user = (User)session.getAttribute("user");
        modelMap.addAttribute("user",user);
        if(code == Constant.UserType.SELLER )
            return "seller_main";
        return "admin_main";
    }

    @RequestMapping(value = "/certify", method= RequestMethod.GET )
    public String getShopCertify() {
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER)
            return "redirect:/";
        if(code== Constant.UserType.ADMIN || code == Constant.UserType.SELLER)
            return "redirect:/shop";
        return "certify_shop";
    }

    @RequestMapping(value="/reset",method = RequestMethod.POST)
    @ResponseBody
    public APIResult restePassword(
        @RequestParam(value = "uid", required= false) String uid,
        @RequestParam(value = "password", required = false) String password) {
        Integer code = Util.isLogin(session);
        if(code == Constant.UserType.USER)
            return APIResult.createNG("????????????");
        if(code == Constant.UserType.NOT_USER)
            return APIResult.createNG("????????????");
        if(code == Constant.UserType.SELLER)
            return APIResult.createNG("????????????");
        if(!Util.Certify.certifyNumber(uid))
            return APIResult.createNG("????????????");
        if(!Util.Certify.certifyPassword(password))
            return APIResult.createNG("????????????");
        User user = userRepository.findByUidAndStatus(Long.parseLong(uid),Constant.RecordStatus.EXIST);
        if(user == null)
            return APIResult.createNG("????????????");
        user.setPassword(password);
        userRepository.save(user);

        User admin = (User)session.getAttribute("user");
        String content = normalizeKeyValuePair("uid",uid);
        OperationRecord operation_record = getOperationRecordWithObject();
        operation_record.setMainUid(admin.getUid());
        operation_record.setOperationType(Constant.OPERATION_OBJECT.UPDATE);
        operation_record.setObjectId(user.getUid());
        operation_record.setContent(content);
        operationRecordRepository.save(operation_record);


        return APIResult.createOKMessage("????????????");
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
            return "certify_shop";
        }
        if(!Util.Certify.certifyPassword(password)) {
            return "certify_shop";
        }
        if(!Util.Certify.certifyNumber(type)) {
            return "certify_shop";
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
            return "redirect:/shop/";
        }
        return "certify_shop";
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
            return "certify_shop";
        }
        if(!Util.Certify.certifyPassword(password)) {
            return "certify_shop";
        }
        if(!Util.Certify.certifyShopname(shopname)) {
            return "certify_shop";
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
            return "certify_shop";
        }
        return "redirect:/shop";
    }

    @RequestMapping(value = "/logout")
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

    @RequestMapping(value="/seller", method = RequestMethod.GET)
    @ResponseBody
    public APIResult getSeller() {
        Integer code = Util.isLogin(session);
        if(code != Constant.UserType.ADMIN)
            return APIResult.createNG("????????????");
        User admin = (User)session.getAttribute("user");
        List<User> sellers = userRepository.findByParentAndStatus(admin.getUid(),Constant.RecordStatus.EXIST);
        List<DataSeller> data_sellers = new ArrayList<DataSeller>();
        for(User seller:sellers) {
            data_sellers.add(Util.tran2DataSeller(seller));
        }

        return APIResult.createOK(data_sellers);
    }

    @RequestMapping(value = "/grade",method = RequestMethod.GET)
    @ResponseBody
    public APIResult getGrade(
        @RequestParam(required = false,defaultValue = "999999") String uid,
        @RequestParam(required = false) String all) {
        Integer code = Util.isLogin(session);
        if(code != Constant.UserType.ADMIN)
            return APIResult.createNG("????????????");
        if(!Util.Certify.certifyNumber(uid))
            return APIResult.createNG("????????????");

        String content = normalizeKeyValuePair("uid",uid);
        content += normalizeKeyValuePair("all",all);
        OperationRecord operation_record = getOperationRecordWithNone();
        operation_record.setContent(content);
        operation_record.setObjectId(-1L);
        List<DataGrade> data_grades = new ArrayList<DataGrade>();
        if(all != null) {
            User admin = (User)session.getAttribute("user");
            List<User> sellers = userRepository.findByParentAndStatus(admin.getUid(),Constant.RecordStatus.EXIST);
            for(User seller: sellers) {
                data_grades.addAll(getDataGradesByUid(seller.getUid()));
            }

            operation_record.setMainUid(admin.getUid());
            operation_record.setOperationType(Constant.OPERATION_NONE.GET_SELLER_GRADE);
        } else {
            data_grades.addAll(getDataGradesByUid(Long.parseLong(uid)));

            User seller = (User)session.getAttribute("user");
            operation_record.setMainUid(seller.getUid());
            operation_record.setOperationType(Constant.OPERATION_NONE.GET_COMMODITY_GRADE);
        }
        operationRecordRepository.save(operation_record);
        return APIResult.createOK(data_grades);
    }


    @RequestMapping(value = "/registerSeller", method = RequestMethod.POST)
    @ResponseBody
    public APIResult registerSeller(
        @RequestParam(required=false) String username,
        @RequestParam(required=false) String password) {

        Integer code = Util.isLogin(session);
        if(code != Constant.UserType.ADMIN)
            return APIResult.createNG("????????????");
        if(username == null || password == null)
            return APIResult.createNG("????????????");

        User old_user = userRepository.findByUsernameAndTypeAndStatus(username, Constant.UserType.SELLER,Constant.RecordStatus.EXIST);
        if(old_user != null)
            return APIResult.createNG("????????????");
        User user = new User();
        User user_parent = (User)session.getAttribute("user");
        String ip_addr = Util.getIpAddr(request);
        user.tran2Seller(username, password,user_parent.getUid(), ip_addr);
        userRepository.save(user);

        String content = normalizeKeyValuePair("username",username);
        OperationRecord operation_record = getOperationRecordWithObject();
        operation_record.setMainUid(user_parent.getUid());
        operation_record.setOperationType(Constant.OPERATION_OBJECT.CREATE);
        operation_record.setObjectId(user.getUid());
        operation_record.setContent(content);
        operationRecordRepository.save(operation_record);

        return APIResult.createOKMessage("????????????");
    }

    @RequestMapping(value = "/deleteSeller", method = RequestMethod.GET)
    @ResponseBody
    public APIResult registerSeller(
        @RequestParam(required=false) String uid) {

        Integer code = Util.isLogin(session);
        if(code != Constant.UserType.ADMIN)
            return APIResult.createNG("????????????");
        if(!Util.Certify.certifyNumber(uid))
            return APIResult.createNG("????????????");

        userRepository.updateStatusByUid(Long.parseLong(uid),Constant.RecordStatus.DELETED);

        User user = (User)session.getAttribute("user");
        String content = normalizeKeyValuePair("uid",uid);
        OperationRecord operation_record = getOperationRecordWithObject();
        operation_record.setMainUid(user.getUid());
        operation_record.setObjectId(Long.parseLong(uid));
        operation_record.setOperationType(Constant.OPERATION_OBJECT.DELETE);
        operation_record.setContent(content);
        operationRecordRepository.save(operation_record);

        return APIResult.createOKMessage("????????????");
    }


    private OperationRecord getOperationRecordWithNone() {
        OperationRecord operation_record = new OperationRecord();
        operation_record.setIp(Util.getIpAddr(request));
        operation_record.setDate(Util.getDateNow());
        operation_record.setObjectType(Constant.OBJECT_TYPE.NONE);
        operation_record.setStatus(Constant.RecordStatus.EXIST);
        return operation_record;
    }

    private OperationRecord getOperationRecordWithObject() {
        OperationRecord operation_record = new OperationRecord();
        operation_record.setIp(Util.getIpAddr(request));
        operation_record.setDate(Util.getDateNow());
        operation_record.setObjectType(Constant.OBJECT_TYPE.USER);
        operation_record.setStatus(Constant.RecordStatus.EXIST);
        return operation_record;
    }
    private String normalizeKeyValuePair(String key,String value) {
        return String.format("%s=%s;", key,value);
    }


    private List<DataGrade> getDataGradesByUid(Long uid) {
        User user = userRepository.findByUid(uid);
        List<Trade> trades = tradeRepository.findBySellerUidAndFinished(uid, Constant.Finished.FINISH);
        HashMap<Long,DataGrade> data_grade_mappng = new HashMap<Long,DataGrade>();
        for(Trade trade : trades) {
            Long cid = trade.getCid();
            DataGrade data_grade = null;
            if(data_grade_mappng.containsKey(cid)) {
                data_grade = data_grade_mappng.get(cid);
                data_grade.setIncome(data_grade.getIncome() + trade.getTotal());
                data_grade_mappng.put(cid,data_grade);
            } else {
                Commodity commodity = commodityRepository.findByCid(cid);
                data_grade_mappng.put(cid,Util.tran2DataGrad(commodity,trade,user.getUsername()));
            }
        }
        List<DataGrade> data_grades = new ArrayList<DataGrade>();
        for(Long key: data_grade_mappng.keySet()) {
            data_grades.add(data_grade_mappng.get(key));
        }
        return data_grades;
    }

}
