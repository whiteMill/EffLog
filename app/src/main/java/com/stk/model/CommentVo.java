package com.stk.model;

import java.io.Serializable;

/**
 * Created by admin on 2016/12/7.
 */

public class CommentVo implements Serializable{
    /**
     * COMMENT_TIME : 2016-12-07 15:42
     * TO_NAME : 郭军
     * FROM_NAME : 王磊
     * COMMENT_CONTENT : eqweqw
     * COMMENT_ID : d937f580c2284d1d97356d5fe7e17ed2
     * LOG_INDEX : c3ebe421b319478d9d5b055fb3459d1b
     * TO_ID : 163f8cdca8f343b58b1289a5e5a6a679
     * FROM_ID : a16e0e5662fb4fe79ba25c6b2a5fd108
     */
    private String COMMENT_TIME;
    private String TO_NAME;
    private String FROM_NAME;
    private String COMMENT_CONTENT;
    private String COMMENT_ID;
    private String LOG_INDEX;
    private String TO_ID;
    private String FROM_ID;

    public String getCOMMENT_TIME() {
        return COMMENT_TIME;
    }

    public void setCOMMENT_TIME(String COMMENT_TIME) {
        this.COMMENT_TIME = COMMENT_TIME;
    }

    public String getTO_NAME() {
        return TO_NAME;
    }

    public void setTO_NAME(String TO_NAME) {
        this.TO_NAME = TO_NAME;
    }

    public String getFROM_NAME() {
        return FROM_NAME;
    }

    public void setFROM_NAME(String FROM_NAME) {
        this.FROM_NAME = FROM_NAME;
    }

    public String getCOMMENT_CONTENT() {
        return COMMENT_CONTENT;
    }

    public void setCOMMENT_CONTENT(String COMMENT_CONTENT) {
        this.COMMENT_CONTENT = COMMENT_CONTENT;
    }

    public String getCOMMENT_ID() {
        return COMMENT_ID;
    }

    public void setCOMMENT_ID(String COMMENT_ID) {
        this.COMMENT_ID = COMMENT_ID;
    }

    public String getLOG_INDEX() {
        return LOG_INDEX;
    }

    public void setLOG_INDEX(String LOG_INDEX) {
        this.LOG_INDEX = LOG_INDEX;
    }

    public String getTO_ID() {
        return TO_ID;
    }

    public void setTO_ID(String TO_ID) {
        this.TO_ID = TO_ID;
    }

    public String getFROM_ID() {
        return FROM_ID;
    }

    public void setFROM_ID(String FROM_ID) {
        this.FROM_ID = FROM_ID;
    }

}
