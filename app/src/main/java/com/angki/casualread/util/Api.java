package com.angki.casualread.util;

/**
 * Created by tengyu on 2017/3/15.
 * 项目中所用到的API
 *
 */

public class Api {

    /**
     *  API说明：
     *  API 均由 知乎（Zhihu.Inc） 提供，
     *  来源https://github.com/izzyleung/ZhihuDailyPurify/wiki/%E7%9F%A5%E4%B9%8E%E6%97%A5%E6%8A%A5-API-%E5%88%86%E6%9E%90
     *  拼接latest如：“http://news-at.zhihu.com/api/4/news/latest”获取最新消息
     *  拼接id号如：“http://news-at.zhihu.com/api/4/news/3892357”获取消息内容
     */
    public static final String ZHIHU_NEWS = "http://news-at.zhihu.com/api/4/news/";

    /**
     * API说明：
     * API 均由聚合数据提供，
     * 来源：https://www.juhe.cn/
     * 拼接"?key=您申请的KEY&page=1&pagesize=10"
     * page为当前页数，pagesize返回条数
     */
    public static final String JOKE = "http://japi.juhe.cn/joke/content/text.from";

    /**
     *
     */
}
