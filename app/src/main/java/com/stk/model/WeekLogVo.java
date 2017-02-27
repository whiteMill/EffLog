package com.stk.model;

/**
 * Created by admin on 2016/12/26.
 */

public class WeekLogVo {
    private String  level;
    private String content;

    public WeekLogVo(String level, String content) {
        this.level = level;
        this.content = content;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
