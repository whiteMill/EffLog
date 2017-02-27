package com.stk.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by admin on 2017/1/16.
 */

public class ProjectVo implements Serializable{
    private String PROJECT_ID;
    private String PROJECT_NAME;
    private String PROJECT_TIME;
    private String PROJECT_BEGIN_TIME;
    private String PROJECT_END_TIME;
    private String PROJECT_INFO;
    private String USER_ID;
    private String NAME;
    private List<ManaPeo> MANA_PEO;

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

    public String getPROJECT_TIME() {
        return PROJECT_TIME;
    }

    public void setPROJECT_TIME(String PROJECT_TIME) {
        this.PROJECT_TIME = PROJECT_TIME;
    }

    public String getPROJECT_BEGIN_TIME() {
        return PROJECT_BEGIN_TIME;
    }

    public void setPROJECT_BEGIN_TIME(String PROJECT_BEGIN_TIME) {
        this.PROJECT_BEGIN_TIME = PROJECT_BEGIN_TIME;
    }

    public String getPROJECT_END_TIME() {
        return PROJECT_END_TIME;
    }

    public void setPROJECT_END_TIME(String PROJECT_END_TIME) {
        this.PROJECT_END_TIME = PROJECT_END_TIME;
    }

    public String getPROJECT_INFO() {
        return PROJECT_INFO;
    }

    public void setPROJECT_INFO(String PROJECT_INFO) {
        this.PROJECT_INFO = PROJECT_INFO;
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

    public List<ManaPeo> getMANA_PEO() {
        return MANA_PEO;
    }

    public void setMANA_PEO(List<ManaPeo> MANA_PEO) {
        this.MANA_PEO = MANA_PEO;
    }
}
