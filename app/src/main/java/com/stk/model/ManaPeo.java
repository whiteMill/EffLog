package com.stk.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/1/11.
 */

public class ManaPeo implements Serializable{

    private String USER_ID;
    private String NAME;
    private String PEO_TYPE;
    private String PHONE;
   /* private Boolean isCheck;
    private String firstCharacter;*/

    public String getPHONE() {
        return PHONE;
    }

    public void setPHONE(String PHONE) {
        this.PHONE = PHONE;
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

    public String getPEO_TYPE() {
        return PEO_TYPE;
    }

    public void setPEO_TYPE(String PEO_TYPE) {
        this.PEO_TYPE = PEO_TYPE;
    }
}
