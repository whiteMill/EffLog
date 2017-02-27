package com.stk.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/1/20.
 */

public class ProPushVo implements Serializable{
    private String USER_ID;
    private String PROJECT_ID;
    private String PROJECT_NAME;
    private String NAME;
    private String MESSAGE;
    private String POINT;
    private String PRO_ITEM_ID;
    private String PRO_TIME;
    private String FLAG;

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getPROJECT_ID() {
        return PROJECT_ID;
    }

    public void setPROJECT_ID(String PROJECT_ID) {
        this.PROJECT_ID = PROJECT_ID;
    }

    public String getPROJECT_NAME() {
        return PROJECT_NAME;
    }

    public void setPROJECT_NAME(String PROJECT_NAME) {
        this.PROJECT_NAME = PROJECT_NAME;
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

    public String getPRO_ITEM_ID() {
        return PRO_ITEM_ID;
    }

    public void setPRO_ITEM_ID(String PRO_ITEM_ID) {
        this.PRO_ITEM_ID = PRO_ITEM_ID;
    }

    public String getPRO_TIME() {
        return PRO_TIME;
    }

    public void setPRO_TIME(String PRO_TIME) {
        this.PRO_TIME = PRO_TIME;
    }

    public String getFLAG() {
        return FLAG;
    }

    public void setFLAG(String FLAG) {
        this.FLAG = FLAG;
    }
}
