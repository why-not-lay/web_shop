package com.web.web_shop.Listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.web.web_shop.DAO.OnlineRecordRepository;
import com.web.web_shop.Tool.Util;
import com.web.web_shop.beans.OnlineRecord;
import com.web.web_shop.beans.User;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * SessionListener
 */
@WebListener
public class SessionListener implements HttpSessionListener {
    @Autowired
    HttpSession session;
    @Autowired
    OnlineRecordRepository onlineRecordRepository;

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        User user = (User)session.getAttribute("user");
        if(user == null) return;
        OnlineRecord online = (OnlineRecord)session.getAttribute("online");
        online.setBeforeLogout(online.getLoginIp(), Util.getDateNow());
        onlineRecordRepository.save(online);
    }

}
