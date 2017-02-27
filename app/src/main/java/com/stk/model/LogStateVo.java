package com.stk.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/1/19.
 */

public class LogStateVo implements Serializable{
    private String USER_ID;
    private String NAME;
    private String LOG_TIME;
    private String MESSAGE;
    private String POINT;
    private String LOG_INDEX;
    private String LOG_TYPE;
    private String LOG_ID;
    private String FLAG;

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getLOG_TIME() {
        return LOG_TIME;
    }

    public void setLOG_TIME(String LOG_TIME) {
        this.LOG_TIME = LOG_TIME;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getPOINT() {
        return POINT;
    }

    public void setPOINT(String POINT) {
        this.POINT = POINT;
    }

    public String getLOG_INDEX() {
        return LOG_INDEX;
    }

    public void setLOG_INDEX(String LOG_INDEX) {
        this.LOG_INDEX = LOG_INDEX;
    }

    public String getLOG_TYPE() {
        return LOG_TYPE;
    }

    public void setLOG_TYPE(String LOG_TYPE) {
        this.LOG_TYPE = LOG_TYPE;
    }

    public String getLOG_ID() {
        return LOG_ID;
    }

    public void setLOG_ID(String LOG_ID) {
        this.LOG_ID = LOG_ID;
    }

    @Override
    public String toString() {
        return "LogStateVo{" +
                "FLAG='" + FLAG + '\'' +
                ", LOG_ID='" + LOG_ID + '\'' +
                ", LOG_TYPE='" + LOG_TYPE + '\'' +
                ", LOG_INDEX='" + LOG_INDEX + '\'' +
                ", POINT='" + POINT + '\'' +
                ", MESSAGE='" + MESSAGE + '\'' +
                ", LOG_TIME='" + LOG_TIME + '\'' +
                ", NAME='" + NAME + '\'' +
                ", USER_ID='" + USER_ID + '\'' +
                '}';
    }
}
