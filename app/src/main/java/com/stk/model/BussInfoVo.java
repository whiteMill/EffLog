package com.stk.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/1/12.
 */

public class BussInfoVo implements Serializable{

    private String  BUSS_ID ;
    private String  USER_ID;
    private String  NAME;
    private String  BUSS_MESS;
    private String  INFO_TIME;
    private String  INFO_FLAG;
    private String  INFO_BEGIN_TIME;
    private String  INFO_END_TIME;
    private String  BUSS_INFO_ID;

    public String getBUSS_ID() {
        return BUSS_ID;
    }

    public void setBUSS_ID(String BUSS_ID) {
        this.BUSS_ID = BUSS_ID;
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

    public String getBUSS_MESS() {
        return BUSS_MESS;
    }

    public void setBUSS_MESS(String BUSS_MESS) {
        this.BUSS_MESS = BUSS_MESS;
    }

    public String getINFO_TIME() {
        return INFO_TIME;
    }

    public void setINFO_TIME(String INFO_TIME) {
        this.INFO_TIME = INFO_TIME;
    }

    public String getINFO_FLAG() {
        return INFO_FLAG;
    }

    public void setINFO_FLAG(String INFO_FLAG) {
        this.INFO_FLAG = INFO_FLAG;
    }

    public String getINFO_BEGIN_TIME() {
        return INFO_BEGIN_TIME;
    }

    public void setINFO_BEGIN_TIME(String INFO_BEGIN_TIME) {
        this.INFO_BEGIN_TIME = INFO_BEGIN_TIME;
    }

    public String getINFO_END_TIME() {
        return INFO_END_TIME;
    }

    public void setINFO_END_TIME(String INFO_END_TIME) {
        this.INFO_END_TIME = INFO_END_TIME;
    }

    public String getBUSS_INFO_ID() {
        return BUSS_INFO_ID;
    }

    public void setBUSS_INFO_ID(String BUSS_INFO_ID) {
        this.BUSS_INFO_ID = BUSS_INFO_ID;
    }
}
