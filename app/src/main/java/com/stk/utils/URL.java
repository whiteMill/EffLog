package com.stk.utils;

/**
 * Created by wangl on 2016/12/26.
 */

public class URL {
    //登陆
    public static final String LOGIN = "http://114.55.117.10:8080/effLog/effAppController/Login";
    //添加承诺
    public static final String ADD_PROMISE = "http://114.55.117.10:8080/effLog/effAppController/addPromise";
    //更新电话
    public static final String UPDATE_PHONE = "http://114.55.117.10:8080/effLog/effAppController/updatePhone";
    //更新密码
    public static final String UPDATE_PASSWORD = "http://114.55.117.10:8080/effLog/effAppController/updatePass";
    //日志提交
    public static final String LOG_COMMIT = "http://114.55.117.10:8080/effLog/eLogCommitController/LogCommit";
    //日志查询
    public static final String LOG_QUERY = "http://114.55.117.10:8080/effLog/eLogQueryController/logQuery";
    //根据LOG_INDEX和LOG_ID查询日志
    public static final String LOG_QUERY_LOG_INDEX = "http://114.55.117.10:8080/effLog/eLogQueryController/queryLogDetail";
    //删除日志
    public static final String DELETE_LOG = "http://114.55.117.10:8080/effLog/eLogQueryController/deleteLog";
    //删除单条日志
    public static final String DELETE_SINGLE_LOG = "http://114.55.117.10:8080/effLog/eLogQueryController/deleteSingleLog";
    //更新单条日志
    public static final String UPDATE_SINGLE_LOG = "http://114.55.117.10:8080/effLog/eLogQueryController/updateSingleLog";
    //提交评论
    public static final String COMMIT_COMMENT = "http://114.55.117.10:8080/effLog/eLogQueryController/commitComment";
    //查询评论
    public static final String QUERY_COMMENT = "http://114.55.117.10:8080/effLog/eLogQueryController/QueryComment";
    //最近日志查询
    public static final String RECENT_LOG = "http://114.55.117.10:8080/effLog/eLogQueryController/recentLog";
    //日志更新
    public static final String UPDATE_LOG = "http://114.55.117.10:8080/effLog/eLogQueryController/updateLogState";
    //所有用户查询
    public static final String ALL_USER = "http://114.55.117.10:8080/effLog/eLogCommitController/QueryAllUser";
    //商机提交
    public static final String ADD_BUSSINESS = "http://114.55.117.10:8080/effLog/effBussController/addBuss";
    //查询商机
    public static final String QUERY_BUSS = "http://114.55.117.10:8080/effLog/effBussController/queryBuss";
    //添加商机跟进信息
    public static final String ADD_BUSS_INFO = "http://114.55.117.10:8080/effLog/effBussController/addBussItem";
    //查询商机跟进信息
    public static final String QUERY_BUSS_INFO = "http://114.55.117.10:8080/effLog/effBussController/queryBussInfo";
    //更新商机跟进信息
    public static final String UPDATE_BUSS_INFO = "http://114.55.117.10:8080/effLog/effBussController/updateBussInfo";
    //删除商机跟进信息
    public static final String DELTE_BUSS_INFO = "http://114.55.117.10:8080/effLog/effBussController/deleteBussInfo";
    //更新商机
    public static final String UPDATE_BUSS = "http://114.55.117.10:8080/effLog/effBussController/updateBuss";
    //删除商机
    public static final String DELETE_BUSS = "http://114.55.117.10:8080/effLog/effBussController/deleteBuss";
    //商机转项目
    public static final String BUSS_TO_PRO = "http://114.55.117.10:8080/effLog/effBussController/bussToPro";
    //添加项目
    public static final String ADD_PROJECT= "http://114.55.117.10:8080/effLog/effProController/addPro";
    //项目查询
    public static final String QUERY_PROJECT= "http://114.55.117.10:8080/effLog/effProController/queryPro";
    //项目条目查询
    public static final String QUERY_PRO_INFO = "http://114.55.117.10:8080/effLog/effProController/queryProItem";
    //项目条目提交
    public static final String ADD_PRO_INFO = "http://114.55.117.10:8080/effLog/effProController/addProItem";
    //删除项目条目
    public static final String DELTE_PRO_ITEM = "http://114.55.117.10:8080/effLog/effProController/deleteProItem";
    //更新项目条目
    public static final String UPDATE_PRO_ITEM = "http://114.55.117.10:8080/effLog/effProController/updateProItem";
    //更新项目
    public static final String UPDATE_PROJECT = "http://114.55.117.10:8080/effLog/effProController/updatePro";
    //删除项目
    public static final String DELETE_PROJECT  = "http://114.55.117.10:8080/effLog/effProController/deleteProject";
    //更新项目完成状态
    public static final String UPDATE_PROJECT_FLAG  = "http://114.55.117.10:8080/effLog/effProController/updateProjectFlag";
    //查询当天未完成项目条目
    public static final String QUERY_PROJECT_DAY  = "http://114.55.117.10:8080/effLog/proBussController/queryDayProItem";
    //更新当天项目条目
    public static final String UPDATE_PROJECT_DAY  = "http://114.55.117.10:8080/effLog/proBussController/updateDayProItem";
    //查询当天未完成商机跟进信息
    public static final String QUERY_BUSS_DAY  = "http://114.55.117.10:8080/effLog/proBussController/queryDayBussItem";
    //更新当天商机跟进信息
    public static final String UPDATE_BUSS_DAY  = "http://114.55.117.10:8080/effLog/proBussController/updateDayBussItem";
    //查询日程推送记录
    public static final String QUERY_LOG_PUSH  = "http://114.55.117.10:8080/effLog/ePushController/QueryLogPushRecord";
    //查询日志状态更新
    public static final String QUERY_LOG_PUSH_STATE  = "http://114.55.117.10:8080/effLog/ePushController/QueryPushLogChange";
    //查询商机状态更新
    public static final String QUERY_BUSS_PUSH_STATE  = "http://114.55.117.10:8080/effLog/ePushController/QueryPushBussChange";
    //查询项目状态更新
    public static final String QUERY_PRO_PUSH_STATE  = "http://114.55.117.10:8080/effLog/ePushController/QueryPushProChange";
    //软件升级
    public static final String APP_DOWNLOAD="http://114.55.117.10:8080/effLog/update/update.xml";
    //根据LEADER_ID
    public static final String UQERY_USER_BY_LEADER="http://114.55.117.10:8080/effLog/eLogCommitController/QueryAllUserByPrior";
    //筛选查询
    public static final String SORT_LOG="http://114.55.117.10:8080/effLog/eLogQueryController/sortLogQuery";
    //消息界面根据user_id和log_index查询日志
    public static final String MESS_LOG_DETAIL="http://114.55.117.10:8080/effLog/eLogQueryController/queryMessLogDetail";
    //查询单个商机
    public static final String MESS_BUSS_SINGLE="http://114.55.117.10:8080/effLog/effBussController/querySingleBuss";
    //查询单个项目
    public static final String MESS_PRO_SINGLE="http://114.55.117.10:8080/effLog/effProController/querySinglePro";
    //商机设置提醒
    public static final String BUSS_SET_REMIND="http://114.55.117.10:8080/effLog/effBussController/setMindPush";
    //项目设置提醒
    public static final String PRO_SET_REMIND="http://114.55.117.10:8080/effLog/effProController/setMindPush";

}
