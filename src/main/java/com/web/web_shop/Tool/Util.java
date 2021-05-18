package com.web.web_shop.Tool;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.web.web_shop.beans.User;

import com.web.web_shop.beans.DataCommodity;
import com.web.web_shop.beans.Commodity;

/**
 * IpUtil
 */
public class Util {

    public static String getIpAddr(HttpServletRequest request) {
        String ip_addr = null;
        try {
            ip_addr = request.getHeader("x-forward-for");
            if(ip_addr == null || ip_addr.length() == 0 || "unknown".equalsIgnoreCase(ip_addr)) {
                ip_addr=request.getHeader("Proxy-Client-IP");
            }
            if(ip_addr == null || ip_addr.length() == 0 || "unknown".equalsIgnoreCase(ip_addr)) {
                ip_addr=request.getHeader("WL-Proxy-Client-IP");
            }
            if(ip_addr == null || ip_addr.length() == 0 || "unknown".equalsIgnoreCase(ip_addr)) {
                ip_addr = request.getRemoteAddr();
                if(ip_addr.equals("127.0.0.1")) {
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (Exception e) {
                        return "";
                    }
                    ip_addr = inet.getHostAddress();
                }
            }
        } catch (Exception e) {
            ip_addr = "";
        }
        return ip_addr;
    }

    public static String getDateNow() {
        String date_str = "";
        Date now = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        date_str = ft.format(now);
        return date_str;
    }

    public static Integer isLogin(HttpSession session) {
        if (session == null || session.getAttribute("user") == null)
            return Constant.UserType.NOT_USER;
        User user = (User)session.getAttribute("user");
        return user.getType();
    }

    public static List<DataCommodity> tran2DataCommodityList(List<Commodity> commodities, String seller_name) {
        List<DataCommodity> data_commodities = null;
        if(commodities == null) return null;
        data_commodities = new ArrayList<DataCommodity>();
        for(Commodity commodity : commodities) {
            DataCommodity data_commodity = tran2DataCommodity(commodity);
            //DataCommodity data_commodity = new DataCommodity();
            //data_commodity.setCid(commodity.getCid());
            //data_commodity.setName(commodity.getName());
            //data_commodity.setType(commodity.getType());
            //data_commodity.setPrice(commodity.getPrice());
            //data_commodity.setCom_status(commodity.getComStatus());
            //data_commodity.setTotal_number(commodity.getTotalNumber());
            //data_commodity.setCur_number(commodity.getCurNumber());
            //data_commodity.setDescription(commodity.getDescription());
            data_commodity.setSeller_name(seller_name);
            data_commodities.add(data_commodity);
        }
        return data_commodities;
    }

    public static DataCommodity tran2DataCommodity(Commodity commodity) {
        if(commodity == null) return null;
        DataCommodity data_commodity = new DataCommodity();
        data_commodity.setCid(commodity.getCid());
        data_commodity.setName(commodity.getName());
        data_commodity.setType(commodity.getType());
        data_commodity.setPrice(commodity.getPrice());
        data_commodity.setCom_status(commodity.getComStatus());
        data_commodity.setTotal_number(commodity.getTotalNumber());
        data_commodity.setCur_number(commodity.getCurNumber());
        data_commodity.setDescription(commodity.getDescription());
        return data_commodity;
    }
    public static class Certify {
        public static Boolean isUpperCase(char c) {
            return c >= 'A' && c <= 'Z';
        }
        public static Boolean isLowerCase(char c) {
            return c >= 'a' && c <= 'z';
        }
        public static Boolean isDigital(char c) {
            return c >= '0' && c <= '9';
        }
        public static Boolean certifyUsername(String username) {
            if(username == null || username.length() > Constant.Limitation.USERNAME_LENGTH || username.length() == 0) {
                return false;
            }
            if(!(isUpperCase(username.charAt(0)) || isLowerCase(username.charAt(0)))) {
                return false;
            }
            for(int i = 1; i < username.length(); i++) {
                char c = username.charAt(i);
                if(!(isUpperCase(c) || isLowerCase(c) || isDigital(c) || c == '_' )) {
                    return false;
                }
            }
            return true;
        }

        public static Boolean certifyPassword(String password) {
            if( password == null || password.length() > Constant.Limitation.PASSWORD_LENGTH || password.length() == 0) {
                return false;
            }
            Boolean hasUpperCase = false;
            Boolean hasDigital = false;
            Boolean hasLowerCase = false;
            for(int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                if(c < ' ' || c > '~') {
                    return false;
                }
                hasDigital = hasDigital || isDigital(c);
                hasUpperCase = hasUpperCase || isUpperCase(c);
                hasLowerCase = hasLowerCase || isLowerCase(c);
            }
            return hasDigital && hasUpperCase && hasLowerCase;
        }

        public static Boolean certifyNumber(String price) {
            if( price == null) return false;
            for(int i = 0; i < price.length(); i++) {
                if(!isDigital(price.charAt(i)))
                    return false;
            }
            return true;
        }

        public static Boolean certifyMail(String mail) {
            return !( mail == null ||mail.length() > Constant.Limitation.MAIL_LENGTH || mail.length() == 0);
        }

        public static Boolean certifyShopname(String shopname) {
            return !( shopname == null ||shopname.length() > Constant.Limitation.SHOPNAME_LENGTH || shopname.length() == 0);
        }

        public static Boolean certifyCommodityName(String commodity_name) {
            return !( commodity_name == null ||commodity_name.length() > Constant.Limitation.COMMODITY_NAME_LENGTH || commodity_name.length() == 0);
        }

        public static Boolean certifyCommodityDescription(String commodity_description) {
            if(commodity_description == null || commodity_description.length() == 0)
                return true;
            return !( commodity_description.length() > Constant.Limitation.COMMODITY_DESCRIPTION_LENGTH );
        }

        public static Boolean certifyCommodityComStatus(String comStatus) {
            return comStatus != null && certifyNumber(comStatus) && Integer.parseInt(comStatus) <= Constant.Limitation.CommodityStatus_LENGTH;
        }
    }
}