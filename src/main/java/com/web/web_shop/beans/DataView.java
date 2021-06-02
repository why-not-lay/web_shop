package com.web.web_shop.beans;

/**
 * DataView
 */
public class DataView {

    private String name;
    private String user;
    private String start_time;
    private String leave_time;
    private Long duration;

    public void setName(String name) {
        this.name = name;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setDuration(Long duration) {
        this.duration = duration;
    }
    public void setLeave_time(String leave_time) {
        this.leave_time = leave_time;
    }
    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getName() {
        return name;
    }
    public String getUser() {
        return user;
    }
    public Long getDuration() {
        return duration;
    }
    public String getLeave_time() {
        return leave_time;
    }
    public String getStart_time() {
        return start_time;
    }
}
