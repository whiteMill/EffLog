package com.stk.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/1/19.
 */

public class LogPushVo implements Serializable{
    private String LOG_ID;
    private String LOG_INDEX;
    private String USER_ID;
    private String LOG_TIME;
    private String LOG_CONTENT;
    private String FLAG;
    private String LOG_TYPE;

    public String getLOG_TYPE() {
        return LOG_TYPE;
    }

    public void setLOG_TYPE(String LOG_TYPE) {
        this.LOG_TYPE = LOG_TYPE;
    }

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }

    public String getLOG_ID() {
        return LOG_ID;
    }

    public void setLOG_ID(String LOG_ID) {
        this.LOG_ID = LOG_ID;
    }

    public String getLOG_INDEX() {
        return LOG_INDEX;
    }

    public void setLOG_INDEX(String LOG_INDEX) {
        this.LOG_INDEX = LOG_INDEX;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getLOG_TIME() {
        return LOG_TIME;
    }

    public void setLOG_TIME(String LOG_TIME) {
        this.LOG_TIME = LOG_TIME;
    }

    public String getLOG_CONTENT() {
        return LOG_CONTENT;
    }

    public void setLOG_CONTENT(String LOG_CONTENT) {
        this.LOG_CONTENT = LOG_CONTENT;
    }

    @Override
    public String toString() {
        return "LogPushVo{" +
                "LOG_TYPE='" + LOG_TYPE + '\'' +
                ", FLAG='" + FLAG + '\'' +
                ", LOG_CONTENT='" + LOG_CONTENT + '\'' +
                ", LOG_TIME='" + LOG_TIME + '\'' +
                ", USER_ID='" + USER_ID + '\'' +
                ", LOG_INDEX='" + LOG_INDEX + '\'' +
                ", LOG_ID='" + LOG_ID + '\'' +
                '}';
    }
}
