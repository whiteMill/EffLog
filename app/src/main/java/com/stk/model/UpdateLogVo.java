package com.stk.model;

/**
 * Created by admin on 2016/12/30.
 */

public class UpdateLogVo {
    private String LOG_ID;
    private String LOG_FLAG;
    private String LOG_MIND;
    private String LOG_INDEX;
    private String LOG_SUMMARY;
    private String UPDATE_TIME;
    private String LOG_TYPE;

    public String getLOG_ID() {
        return LOG_ID;
    }

    public void setLOG_ID(String LOG_ID) {
        this.LOG_ID = LOG_ID;
    }

    public String getLOG_FLAG() {
        return LOG_FLAG;
    }

    public void setLOG_FLAG(String LOG_FLAG) {
        this.LOG_FLAG = LOG_FLAG;
    }

    public String getLOG_MIND() {
        return LOG_MIND;
    }

    public void setLOG_MIND(String LOG_MIND) {
        this.LOG_MIND = LOG_MIND;
    }

    public String getLOG_INDEX() {
        return LOG_INDEX;
    }

    public void setLOG_INDEX(String LOG_INDEX) {
        this.LOG_INDEX = LOG_INDEX;
    }

    public String getLOG_SUMMARY() {
        return LOG_SUMMARY;
    }

    public void setLOG_SUMMARY(String LOG_SUMMARY) {
        this.LOG_SUMMARY = LOG_SUMMARY;
    }

    public String getUPDATE_TIME() {
        return UPDATE_TIME;
    }

    public void setUPDATE_TIME(String UPDATE_TIME) {
        this.UPDATE_TIME = UPDATE_TIME;
    }

    public String getLOG_TYPE() {
        return LOG_TYPE;
    }

    public void setLOG_TYPE(String LOG_TYPE) {
        this.LOG_TYPE = LOG_TYPE;
    }

    @Override
    public String toString() {
        return "{" +
                "LOG_ID='" + LOG_ID + '\'' +
                ", LOG_FLAG='" + LOG_FLAG + '\'' +
                ", LOG_MIND='" + LOG_MIND + '\'' +
                ", LOG_INDEX='" + LOG_INDEX + '\'' +
                ", LOG_SUMMARY='" + LOG_SUMMARY + '\'' +
                ", UPDATE_TIME='" + UPDATE_TIME + '\'' +
                ", LOG_TYPE='" + LOG_TYPE + '\'' +
                '}';
    }
}
