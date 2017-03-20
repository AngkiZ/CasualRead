package com.angki.casualread.util;

import com.angki.casualread.gank.gson.GankDate;
import com.angki.casualread.gank.gson.GankDates;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBean;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBeans;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.TopNewsBean;
import com.angki.casualread.zhihu.gson.ZhihuDailyStory.StoryBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tengyu on 2017/3/15.
 */

public class Utility {


    private static final String TAG = "Utility";


    /**
     * 将知乎每日推荐返回的JSON数据解析成实体类
     */
    public static NewsBeans handleZHDNResponse(String data) {
        try {
            NewsBeans beans = new NewsBeans();
            JSONObject object = new JSONObject(data);
            //解析Date
            beans.setDate(object.optString("date"));
            //解析stories JSON数组
            JSONArray array = object.optJSONArray("stories");
            //如果数组不为空且有长度进行解析
            if (array != null && array.length() > 0) {
                List<NewsBean> stories = new ArrayList<>();
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject object1 = array.optJSONObject(i);
                    NewsBean bean = new NewsBean();
                    //解析非图片数据
                    bean.setType(object1.optInt("type"));
                    bean.setId(object1.optString("id"));
                    bean.setGa_prefix(object1.optString("ga_prefix"));
                    bean.setTitle(object1.optString("title"));
                    //解析图片数据
                    JSONArray imgArr = object1.optJSONArray("images");
                    //检查图片数组是否有数据，如果有进行解析
                    if (imgArr != null && imgArr.length() > 0) {
                        List<String> images = new ArrayList<>();
                        for (int j = 0; j < imgArr.length(); ++j){
                            images.add(imgArr.optString(j));
                        }
                        //添加到bean中
                        bean.setImages(images);
                    }
                    stories.add(bean);
                }
                beans.setStories(stories);
            }
            //解析top_stories数组数据
            array = object.optJSONArray("top_stories");
            //检查数组是否有数据，如果有则进行解析
            if (array != null && array.length() > 0) {
                List<TopNewsBean> topStories = new ArrayList<>();
                for (int i = 0; i < array.length(); ++i) {
                    object = array.optJSONObject(i);
                    TopNewsBean bean = new TopNewsBean();
                    //开始解析数据
                    bean.setType(object.optInt("type"));
                    bean.setId(object.optString("id"));
                    bean.setGa_prefix(object.optString("ga_prefix"));
                    bean.setTitle(object.optString("title"));
                    bean.setImage(object.optString("image"));
                    //添加到集合中
                    topStories.add(bean);
                }
                beans.setTopStories(topStories);
            }
            return beans;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析知乎日报Json数据
     */
    public static StoryBean handleZHNResponse(String data) {

        try {

            StoryBean bean = new StoryBean();
            JSONObject object = new JSONObject(data);
            //解析非图片数据
            bean.setBody(object.optString("body"));
            bean.setTitle(object.optString("title"));
            bean.setImage_source(object.optString("image_source"));
            bean.setId(object.optInt("id"));
            bean.setShare_url(object.optString("share_url"));
            //解析图片数据
            bean.setImage(object.optString("image"));
            return bean;

        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 解析Gank Json数据
     */
    public static GankDates handleGankResponse(String data) {

        try {

            GankDates bens = new GankDates();
            JSONObject object = new JSONObject(data);
            //解析数据
            bens.setError(object.optString("error"));
            //解析results数组
            JSONArray array = object.optJSONArray("results");
            //如果数组不为空且有长度进行解析
            if (array != null && array.length() > 0) {
                List<GankDate> gankdate = new ArrayList<>();
                for (int i = 0; i < array.length(); ++i) {
                    object = array.optJSONObject(i);
                    GankDate bean = new GankDate();
                    //开始解析非图片数据

                }
            }
        }
    }

}
