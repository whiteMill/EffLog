package com.stk.model;

import java.util.List;

/**
 * Created by admin on 2016/12/28.
 */

public class WorkLogVo {


    /**
     * success : true
     * message : 日志查询成功
     * data : [[{"LOG_SUMMARY":"null","COMMENT_AMOUNT":0,"LOG_MIND":"null","LOG_CONTENT":"123123123","WEATHER":"晴","LOG_TIME":"2016-12-28 10:02","USER_ID":"483aba9c01b94f9bb3283b712f3e924d","UPDATE_TIME":"2016-12-28 10:02","ROLE_NAME":"软件组","LOG_FLAG":"1","NAME":"王垒","END_TIME":"2016-12-28 10:02","BEGIN_TIME":"2016-12-28 10:02","ROLE_ID":"01","LOG_INDEX":"ef14ee71481043bc8cd68b14e28870cd","LOG_ID":"4f61d7f109d2441180fd146d2a46ad3e","LOG_LEVEL":"A"}],[{"LOG_SUMMARY":"null","COMMENT_AMOUNT":0,"LOG_MIND":"null","LOG_CONTENT":"111111111111111111","WEATHER":"晴","LOG_TIME":"2016-12-28 10:04","USER_ID":"483aba9c01b94f9bb3283b712f3e924d","UPDATE_TIME":"2016-12-28 10:04","ROLE_NAME":"软件组","LOG_FLAG":"1","NAME":"王垒","END_TIME":"2016-12-28 10:04","BEGIN_TIME":"2016-12-28 10:04","ROLE_ID":"01","LOG_INDEX":"ac4924d8d2034b2a9e17cd9729e1ac69","LOG_ID":"2e923f8cf3d6498bad1c24701594f047","LOG_LEVEL":"A"},{"LOG_SUMMARY":"null","COMMENT_AMOUNT":0,"LOG_MIND":"null","LOG_CONTENT":"22222222222222222222222","WEATHER":"晴","LOG_TIME":"2016-12-28 10:04","USER_ID":"483aba9c01b94f9bb3283b712f3e924d","UPDATE_TIME":"2016-12-28 10:04","ROLE_NAME":"软件组","LOG_FLAG":"1","NAME":"王垒","END_TIME":"2016-12-28 10:04","BEGIN_TIME":"2016-12-28 10:04","ROLE_ID":"01","LOG_INDEX":"ac4924d8d2034b2a9e17cd9729e1ac69","LOG_ID":"eb3b8b044f4042d7b8a8f384166b639c","LOG_LEVEL":"B"}],[{"LOG_SUMMARY":"null","COMMENT_AMOUNT":0,"LOG_MIND":"null","LOG_CONTENT":"123123333333333","WEATHER":"晴","LOG_TIME":"2016-12-28 10:01","USER_ID":"483aba9c01b94f9bb3283b712f3e924d","UPDATE_TIME":"2016-12-28 10:01","ROLE_NAME":"软件组","LOG_FLAG":"1","NAME":"王垒","END_TIME":"2016-12-28 10:01","BEGIN_TIME":"2016-12-28 10:01","ROLE_ID":"01","LOG_INDEX":"e6dc1fb77a0f4658a9ad0c739479aaab","LOG_ID":"ed0cdd98de95407bb2a3591238173c19","LOG_LEVEL":"A"},{"LOG_SUMMARY":"null","COMMENT_AMOUNT":0,"LOG_MIND":"null","LOG_CONTENT":"1231","WEATHER":"晴","LOG_TIME":"2016-12-28 10:01","USER_ID":"483aba9c01b94f9bb3283b712f3e924d","UPDATE_TIME":"2016-12-28 10:01","ROLE_NAME":"软件组","LOG_FLAG":"1","NAME":"王垒","END_TIME":"2016-12-28 10:01","BEGIN_TIME":"2016-12-28 10:01","ROLE_ID":"01","LOG_INDEX":"e6dc1fb77a0f4658a9ad0c739479aaab","LOG_ID":"5b270feff52047788650bb356159e7a2","LOG_LEVEL":"A"}],[{"LOG_SUMMARY":"null","COMMENT_AMOUNT":0,"LOG_MIND":"null","LOG_CONTENT":"12312312","WEATHER":"晴","LOG_TIME":"2016-12-28 09:56","USER_ID":"483aba9c01b94f9bb3283b712f3e924d","UPDATE_TIME":"2016-12-28 09:56","ROLE_NAME":"软件组","LOG_FLAG":"1","NAME":"王垒","END_TIME":"2016-12-28 09:56","BEGIN_TIME":"2016-12-28 09:56","ROLE_ID":"01","LOG_INDEX":"4e46aa2f9da741a5bd8d862bd665c905","LOG_ID":"a217c7865bfe44b89d8011bcc1a79068","LOG_LEVEL":"A"},{"LOG_SUMMARY":"null","COMMENT_AMOUNT":0,"LOG_MIND":"null","LOG_CONTENT":"12312312","WEATHER":"晴","LOG_TIME":"2016-12-28 09:56","USER_ID":"483aba9c01b94f9bb3283b712f3e924d","UPDATE_TIME":"2016-12-28 09:56","ROLE_NAME":"软件组","LOG_FLAG":"1","NAME":"王垒","END_TIME":"2016-12-28 09:56","BEGIN_TIME":"2016-12-28 09:56","ROLE_ID":"01","LOG_INDEX":"4e46aa2f9da741a5bd8d862bd665c905","LOG_ID":"dae0a2bd65c04dd2ba4c25b6b7711301","LOG_LEVEL":"A"}]]
     */

    private boolean success;
    private String message;
    /**
     * LOG_SUMMARY : null
     * COMMENT_AMOUNT : 0
     * LOG_MIND : null
     * LOG_CONTENT : 123123123
     * WEATHER : 晴
     * LOG_TIME : 2016-12-28 10:02
     * USER_ID : 483aba9c01b94f9bb3283b712f3e924d
     * UPDATE_TIME : 2016-12-28 10:02
     * ROLE_NAME : 软件组
     * LOG_FLAG : 1
     * NAME : 王垒
     * END_TIME : 2016-12-28 10:02
     * BEGIN_TIME : 2016-12-28 10:02
     * ROLE_ID : 01
     * LOG_INDEX : ef14ee71481043bc8cd68b14e28870cd
     * LOG_ID : 4f61d7f109d2441180fd146d2a46ad3e
     * LOG_LEVEL : A
     */

    private List<List<LogVo>> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<List<LogVo>> getData() {
        return data;
    }

    public void setData(List<List<LogVo>> data) {
        this.data = data;
    }


}
