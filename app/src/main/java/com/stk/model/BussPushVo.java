package com.stk.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/1/20.
 */

public class BussPushVo implements Serializable{
    private String USER_ID;
    private String BUSS_ID;
    private String BUSS_NAME;
    private String NAME;
    private String MESSAGE;
    private String POINT;
    private String BUSS_INFO_ID;
    private String BUSS_TIME;
    private String FLAG;

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getBUSS_ID() {
        return BUSS_ID;
    }

    public void setBUSS_ID(String BUSS_ID) {
        this.BUSS_ID = BUSS_ID;
    }

    public String getBUSS_NAME() {
        return BUSS_NAME;
    }

    public void setBUSS_NAME(String BUSS_NAME) {
        this.BUSS_NAME = BUSS_NAME;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
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

    public String getBUSS_INFO_ID() {
        return BUSS_INFO_ID;
    }

    public void setBUSS_INFO_ID(String BUSS_INFO_ID) {
        this.BUSS_INFO_ID = BUSS_INFO_ID;
    }

    public String getBUSS_TIME() {
        return BUSS_TIME;
    }

    public void setBUSS_TIME(String BUSS_TIME) {
        this.BUSS_TIME = BUSS_TIME;
    }

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }
}
