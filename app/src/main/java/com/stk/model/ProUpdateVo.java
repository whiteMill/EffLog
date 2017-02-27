package com.stk.model;

/**
 * Created by admin on 2017/1/17.
 */

public class ProUpdateVo  {
    private String PRO_ITEM_ID;
    private String ITEM_MESS;
    private String ITEM_END_TIME;
    private String PROJECT_NAME;

    public String getPRO_ITEM_ID() {
        return PRO_ITEM_ID;
    }

    public void setPRO_ITEM_ID(String PRO_ITEM_ID) {
        this.PRO_ITEM_ID = PRO_ITEM_ID;
    }

    public String getITEM_MESS() {
        return ITEM_MESS;
    }

    public void setITEM_MESS(String ITEM_MESS) {
        this.ITEM_MESS = ITEM_MESS;
    }

    public String getITEM_END_TIME() {
        return ITEM_END_TIME;
    }

    public void setITEM_END_TIME(String ITEM_END_TIME) {
        this.ITEM_END_TIME = ITEM_END_TIME;
    }

    public String getPROJECT_NAME() {
        return PROJECT_NAME;
    }

    public void setPROJECT_NAME(String PROJECT_NAME) {
        this.PROJECT_NAME = PROJECT_NAME;
    }
}
