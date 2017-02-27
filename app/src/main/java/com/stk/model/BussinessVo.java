package com.stk.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangl 2017/1/10.
 */

public class BussinessVo implements Serializable{
    private String BUSS_ID;
    private String BUSS_NAME;
    private String BUSS_TIME;
    private String BUSS_BEGIN_TIME;
    private String BUSS_END_TIME;
    private String BUSS_RESOURCE;
    private String BUSS_INFO;
    private String USER_ID;
    private String NAME;
    private List<ManaPeo> MANA_PEO;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }

    public String getBUSS_RESOURCE() {
        return BUSS_RESOURCE;
    }

    public void setBUSS_RESOURCE(String BUSS_RESOURCE) {
        this.BUSS_RESOURCE = BUSS_RESOURCE;
    }

    public String getBUSS_INFO() {
        return BUSS_INFO;
    }

    public void setBUSS_INFO(String BUSS_INFO) {
        this.BUSS_INFO = BUSS_INFO;
    }

    public String getBUSS_BEGIN_TIME() {
        return BUSS_BEGIN_TIME;
    }

    public void setBUSS_BEGIN_TIME(String BUSS_BEGIN_TIME) {
        this.BUSS_BEGIN_TIME = BUSS_BEGIN_TIME;
    }

    public String getBUSS_END_TIME() {
        return BUSS_END_TIME;
    }

    public void setBUSS_END_TIME(String BUSS_END_TIME) {
        this.BUSS_END_TIME = BUSS_END_TIME;
    }

    public List<ManaPeo> getMANA_PEO() {
        return MANA_PEO;
    }

    public void setMANA_PEO(List<ManaPeo> MANA_PEO) {
        this.MANA_PEO = MANA_PEO;
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

    public String getBUSS_TIME() {
        return BUSS_TIME;
    }

    public void setBUSS_TIME(String BUSS_TIME) {
        this.BUSS_TIME = BUSS_TIME;
    }



}
