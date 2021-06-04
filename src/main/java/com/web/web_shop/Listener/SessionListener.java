package com.web.web_shop.Listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.web.web_shop.DAO.OnlineRecordRepository;
import com.web.web_shop.Service.DataExtract;
import com.web.web_shop.Tool.Util;
import com.web.web_shop.beans.OnlineRecord;
import com.web.web_shop.beans.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * SessionListener
 */
@WebListener
@Component
public class SessionListener implements HttpSessionListener {
    @Autowired
    private OnlineRecordRepository onlineRecordRepository;
    @Autowired
    private DataExtract data_extract;

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        User user = (User)session.getAttribute("user");
        if(user != null) {
            System.out.println("用户注销");
            OnlineRecord online = (OnlineRecord)session.getAttribute("online");
            online.setBeforeLogout(online.getLoginIp(), Util.getDateNow());
            onlineRecordRepository.save(online);
            data_extract.userDataExtract(user.getUid());

        }
        String uid_str = (String)session.getAttribute("uid");
        System.out.println(uid_str);
        if(uid_str != null) {
            System.out.println("数据提取开始");
            data_extract.userDataExtract(Long.parseLong(uid_str));
            System.out.println("数据提取完成");
        }
    }

}
