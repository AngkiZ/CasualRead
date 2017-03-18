package com.angki.casualread.util;

import com.angki.casualread.zhihu.gson.ZhihuDailyNews.StoriesBean;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.StoriesBeans;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.TopStoriesBean;

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
     * 将知乎每日推荐返回的JSON数据解析成Weather实体类
     */
    public static StoriesBeans handleZHDNResponse(String data) {
        try {
            StoriesBeans beans = new StoriesBeans();
            JSONObject object = new JSONObject(data);
            //解析Date
            beans.setDate(object.optString("date"));
            //解析stories JSON数组
            JSONArray array = object.optJSONArray("stories");
            //如果数组不为空且有长度进行解析
            if (array != null && array.length() > 0) {
                List<StoriesBean> stories = new ArrayList<>();
                for (int i = 0; i < array.length(); ++i) {
                    JSONObject object1 = array.optJSONObject(i);
                    StoriesBean bean = new StoriesBean();
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
                List<TopStoriesBean> topStories = new ArrayList<>();
                for (int i = 0; i < array.length(); ++i) {
                    object = array.optJSONObject(i);
                    TopStoriesBean bean = new TopStoriesBean();
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






//    public static ZhihuDailyNews handleZHDNResponse(String response){
//
//        try{
//            Log.d(TAG, "ZHDNContent: 1" );
//            JSONObject jsonObject = new JSONObject(response);
//            Log.d(TAG, "ZHDNContent: 2" );
//            JSONArray jsonArray = jsonObject.getJSONArray("");
//
//            String ZHDNContent = jsonArray.getJSONObject(0).toString();
//            return new Gson().fromJson(response, ZhihuDailyNews.class);
//        }catch (Exception e){
//            Log.d(TAG, "handleZHDNResponse: e.printStackTrace();");
//            e.printStackTrace();
//        }
//        Log.d(TAG, "正常加载 ");
//        return null;
//    }


}
