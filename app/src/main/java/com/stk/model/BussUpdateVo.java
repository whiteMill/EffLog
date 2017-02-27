package com.stk.model;

/**
 * Created by admin on 2017/1/18.
 */

public class BussUpdateVo {
    private String BUSS_INFO_ID;
    private String BUSS_MESS;
    private String INFO_END_TIME;
    private String BUSS_NAME;

    public String getBUSS_INFO_ID() {
        return BUSS_INFO_ID;
    }

    public void setBUSS_INFO_ID(String BUSS_INFO_ID) {
        this.BUSS_INFO_ID = BUSS_INFO_ID;
    }

    public String getBUSS_MESS() {
        return BUSS_MESS;
    }

    public void setBUSS_MESS(String BUSS_MESS) {
        this.BUSS_MESS = BUSS_MESS;
    }

    public String getINFO_END_TIME() {
        return INFO_END_TIME;
    }

    public void setINFO_END_TIME(String INFO_END_TIME) {
        this.INFO_END_TIME = INFO_END_TIME;
    }

    public String getBUSS_NAME() {
        return BUSS_NAME;
    }

    public void setBUSS_NAME(String BUSS_NAME) {
        this.BUSS_NAME = BUSS_NAME;
    }
}
