package com.stk.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/1/12.
 */

public class ManPeoContact implements Serializable {
    private String USER_ID;
    private String NAME;
    private Boolean isChecked;
    private String firstCharacter;

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

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getFirstCharacter() {
        return firstCharacter;
    }

    public void setFirstCharacter(String firstCharacter) {
        this.firstCharacter = firstCharacter;
    }

    @Override
    public String toString() {
        return "ManPeoContact{" +
                "USER_ID='" + USER_ID + '\'' +
                ", NAME='" + NAME + '\'' +
                ", isChecked=" + isChecked +
                ", firstCharacter='" + firstCharacter + '\'' +
                '}';
    }
}
