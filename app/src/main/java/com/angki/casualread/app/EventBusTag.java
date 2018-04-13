package com.angki.casualread.app;

/**
 * 事件的总Tag
 * AndroidEventBus
 * 使用地址：https://github.com/hehonghui/AndroidEventBus/blob/master/README-ch.md
 * @author ：Administrator on 2018/3/12 11:49
 * @eamil ：503001231@qq.com
 */


public interface EventBusTag {


    //关闭登录页面的加载框加载
    String Fragment_LoginSignIn_HideLoading = "Fragment_LoginSignIn_HideLoading";
    //微信登录跳转
    String Fragment_LoginSignIn_Launch = "Fragment_LoginSignIn_Launch";
    //更新好友信息(好友关系,传入数据int: 1，对方发来好友邀请；2，对方同意我的好友请求；3，我同意对方好友请求；4，删除好友)
    String Update_Friends_Relationship = "Update_Friends_Relationship";
    //更新好友信息(好友信息)
    String Update_Friends_Data = "Update_Friends_Data";
    //刷新用户信息
    String Update_UserInfo = "Update_UserInfo";
    //刷新好友请求
    String Update_Red_Dot = "Update_Red_Dot";
    //刷新用户积分
    String Update_UserIntegral = "Update_UserIntegral";
    //心动值的实时更新
    String Update_NowHeartValue = "Update_NowHeartValue";
    //切换Fragment(1,注册；2，登录；3，忘记密码)
    String Replace_Fragment = "Replace_Fragment";
}
