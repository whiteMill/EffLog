package com.stk.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/1/16.
 */

public class ProInfoVo implements Serializable {
    private String  PROJECT_ID ;
    private String  USER_ID;
    private String  NAME;
    private String  ITEM_MESS;
    private String  ITEM_TIME;
    private String  ITEM_FLAG;
    private String  ITEM_BEGIN_TIME;
    private String  ITEM_END_TIME;
    private String  PRO_ITEM_ID;
    private String  ITEM_MARKER;

    public String getITEM_MARKER() {
        return ITEM_MARKER;
    }

    public void setITEM_MARKER(String ITEM_MARKER) {
        this.ITEM_MARKER = ITEM_MARKER;
    }

    public String getPROJECT_ID() {
        return PROJECT_ID;
    }

    public void setPROJECT_ID(String PROJECT_ID) {
        this.PROJECT_ID = PROJECT_ID;
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

    public String getITEM_MESS() {
        return ITEM_MESS;
    }

    public void setITEM_MESS(String ITEM_MESS) {
        this.ITEM_MESS = ITEM_MESS;
    }

    public String getITEM_TIME() {
        return ITEM_TIME;
    }

    public void setITEM_TIME(String ITEM_TIME) {
        this.ITEM_TIME = ITEM_TIME;
    }

    public String getITEM_FLAG() {
        return ITEM_FLAG;
    }

    public void setITEM_FLAG(String ITEM_FLAG) {
        this.ITEM_FLAG = ITEM_FLAG;
    }

    public String getITEM_BEGIN_TIME() {
        return ITEM_BEGIN_TIME;
    }

    public void setITEM_BEGIN_TIME(String ITEM_BEGIN_TIME) {
        this.ITEM_BEGIN_TIME = ITEM_BEGIN_TIME;
    }

    public String getITEM_END_TIME() {
        return ITEM_END_TIME;
    }

    public void setITEM_END_TIME(String ITEM_END_TIME) {
        this.ITEM_END_TIME = ITEM_END_TIME;
    }

    public String getPRO_ITEM_ID() {
        return PRO_ITEM_ID;
    }

    public void setPRO_ITEM_ID(String PRO_ITEM_ID) {
        this.PRO_ITEM_ID = PRO_ITEM_ID;
    }
}
