package com.angki.casualread.util;

import com.angki.casualread.gank.gson.GankDatas;
import com.angki.casualread.gank.gson.GankData;
import com.angki.casualread.gank.gson.GankWelfareData;
import com.angki.casualread.gank.gson.GankWelfareDatas;
import com.angki.casualread.joke.gson.JokeData;
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
    public static GankDatas handleGankResponse(String data) {

        try {

            GankDatas beans = new GankDatas();
            JSONObject object = new JSONObject(data);
            //解析数据
            beans.setError(object.optString("error"));
            //解析results数组
            JSONArray array = object.optJSONArray("results");
            //如果数组不为空且有长度进行解析
            if (array != null && array.length() > 0) {
                List<GankData> gankdate = new ArrayList<>();
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject object1 = array.optJSONObject(i);
                    GankData bean = new GankData();
                    //开始解析非图片数据
                    bean.setDesc(object1.optString("desc"));
                    bean.setType(object1.optString("type"));
                    bean.setUrl(object1.optString("url"));
                    bean.setWho(object1.optString("who"));
                    //解析图片数据
                    JSONArray imgArr = object1.optJSONArray("images");
                    //检查图片数组是否有数据，如果有进行解析
                    if (imgArr != null && imgArr.length() > 0) {
                        List<String> images = new ArrayList<>();
                        for (int j = 0; j < imgArr.length(); ++j) {
                            images.add(imgArr.optString(j));
                        }
                        //添加到bean中
                        bean.setImages(images);
                    }
                    gankdate.add(bean);
                }
                beans.setResults(gankdate);
            }
            return beans;
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析GankWelfare数据
     */
    public static GankWelfareDatas handleGankWelfareResponse(String data) {

        try {
            GankWelfareDatas beans = new GankWelfareDatas();
            JSONObject object = new JSONObject(data);
            //开始解析数据
            beans.setError(object.optString("error"));
            //解析results数组
            JSONArray array = object.optJSONArray("results");
            //如果数组不为空且有长度进行解析
            if (array != null && array.length() > 0) {
                List<GankWelfareData> gankwelfare = new ArrayList<>();
                for (int i = 0; i < array.length(); ++i) {
                    object = array.optJSONObject(i);
                    GankWelfareData bean = new GankWelfareData();
                    bean.set_id(object.optString("_id"));
                    bean.setDesc(object.optString("desc"));
                    bean.setType(object.optString("type"));
                    bean.setUrl(object.optString("url"));
                    bean.setWho(object.optString("who"));

                    gankwelfare.add(bean);
                }
                beans.setResults(gankwelfare);
            }
            return beans;
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解析糗事百科
     */
    public static List<JokeData> handleJokeResponse(String data) {

        try {
            JSONObject object = new JSONObject(data);
            String  reason = object.optString("reason");
            //如果请求成功
            if (reason.equals("Success")) {
                //获取数据json
                object = object.optJSONObject("result");
                JSONArray datas = object.optJSONArray("data");
                //有数据则进行解析
                if (datas != null && datas.length() > 0) {
                    List<JokeData> beans = new ArrayList<>();
                    for (int i = 0; i < datas.length(); ++i) {
                        object = datas.optJSONObject(i);
                        beans.add(new JokeData(
                                object.optString("content"),
                                object.optString("unixtime"),
                                object.optString("hashId"),
                                object.optString("updatetime")
                        ));
                    }
                    return beans;
                }
            }
            return null;
        }catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
