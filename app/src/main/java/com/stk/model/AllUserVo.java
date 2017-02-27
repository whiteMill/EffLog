package com.stk.model;

import java.io.Serializable;

/**
 * Created by admin on 2017/1/4.
 */

public class AllUserVo implements Serializable{
    /**
     * PHONE : 18758031162
     * USER_ID : 483aba9c01b94f9bb3283b712f3e924d
     * ROLE_ID : 01
     * PERMISSION_NAME : 普通用户
     * USER_NAME : wanglei
     * ROLE_NAME : 软件组
     * PERMISSION_ID : 01
     * NAME : 王垒
     */
        private String PHONE;
        private String USER_ID;
        private String ROLE_ID;
        private String PERMISSION_NAME;
        private String USER_NAME;
        private String ROLE_NAME;
        private String PERMISSION_ID;
        private String NAME;

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

        public String getROLE_ID() {
            return ROLE_ID;
        }

        public void setROLE_ID(String ROLE_ID) {
            this.ROLE_ID = ROLE_ID;
        }

        public String getPERMISSION_NAME() {
            return PERMISSION_NAME;
        }

        public void setPERMISSION_NAME(String PERMISSION_NAME) {
            this.PERMISSION_NAME = PERMISSION_NAME;
        }

        public String getUSER_NAME() {
            return USER_NAME;
        }

        public void setUSER_NAME(String USER_NAME) {
            this.USER_NAME = USER_NAME;
        }

        public String getROLE_NAME() {
            return ROLE_NAME;
        }

        public void setROLE_NAME(String ROLE_NAME) {
            this.ROLE_NAME = ROLE_NAME;
        }

        public String getPERMISSION_ID() {
            return PERMISSION_ID;
        }

        public void setPERMISSION_ID(String PERMISSION_ID) {
            this.PERMISSION_ID = PERMISSION_ID;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

}
