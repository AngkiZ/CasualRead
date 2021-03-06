package com.angki.casualread.mvp.ui.activity;

import android.content.res.Configuration;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.angki.casualread.R;
import com.angki.casualread.mvp.model.api.Api;
import com.angki.casualread.app.util.HttpUtil;
import com.angki.casualread.app.util.NetworkStatus;
import com.angki.casualread.app.util.Utility;
import com.angki.casualread.mvp.ui.adapter.AppBarStateChangeListener;
import com.angki.casualread.mvp.model.entity.bean.dbZhihuNews;
import com.angki.casualread.mvp.model.entity.bean.dbZhihuStors;
import com.angki.casualread.mvp.model.entity.result.ZhihuDailyStory.StoryBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ActivityZhihu extends AppCompatActivity {
    private static final String TAG = "ActivityZhihu";
    private WebView webView;
    private Toolbar toolbar;
    private ImageView imageView;
    private StoryBean storyBean;
    private dbZhihuStors mdbZhihuStors;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton floatingActionButton;
    private boolean isCollection;//是否被收藏
    private List<dbZhihuStors> cache;//缓存
    private boolean isnetwork;//判断是否有网

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isnetwork = new NetworkStatus().judgment(ActivityZhihu.this);
        String newsId = getIntent().getStringExtra("news_id");
        cache = DataSupport.where("db_zs_id like ?", "%" + newsId + "%")
                .find(dbZhihuStors.class);
        //判断是否有网并且有无缓存，两者同时都没有的话便加载另一个布局
        if (cache.size() == 0 && !isnetwork) {
            setContentView(R.layout.activity_zhihu_null_layout);
            toolbar = (Toolbar) findViewById(R.id.activity_zhihu_null_layout_collapsing_toolbar);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }else {
            setContentView(R.layout.activity_zhihu_layout);
            GetData(newsId);
        }
    }

    private void GetData(final String newsId) {

        String url = Api.ZHIHU_NEWS + newsId;

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        String html = CombinedData(cache.get(0));
                        LoadModule(html, cache.get(0));
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取Json数据
                final String responseData = response.body().string();
                //解析Json数据
                storyBean = Utility.handleZHNResponse(responseData);
                //储存数据
                mdbZhihuStors = new dbUtil().dbzhihustorsSave(storyBean);

                Log.d("-----", "storyBean: " + storyBean.getImages().get(0));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String html = CombinedData(mdbZhihuStors);
                        LoadModule(html, mdbZhihuStors);
                    }
                });
            }
        });
    }

    /**
     * 组合数据
     */
    private String CombinedData(dbZhihuStors mdbZhihuStors) {

        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";
        String data = mdbZhihuStors.getDb_zs_body();
        data = data.replace("<div class=\"img-place-holder\">", "");
        data = data.replace("<div class=\"headline\">", "");


        // 根据主题的不同确定不同的加载内容
        String theme = "<body className=\"\" onload=\"onLoaded()\">";
        if ((getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES){

            theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
        }
        String html = new StringBuffer()
                .append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n")
                .append("<head>\n")
                .append("\t<meta charset=\"utf-8\" />")
                .append(css)
                .append("\n</head>\n")
                .append(theme)
                .append(data)
                .append("</body></html>").toString();

        return html;
    }

    /**
     * 加载组件
     */
    private void LoadModule(String result, final dbZhihuStors mdbZhihuStors) {
        //加载控件
        TextView textView = (TextView) findViewById(R.id.activity_zhihu_layout_collapsing_title);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.activity_zhihu_layout_collapsing);
        appBarLayout = (AppBarLayout) findViewById(R.id.activity_zhihu_layout_appBar);
        toolbar = (Toolbar) findViewById(R.id.activity_zhihu_layout_collapsing_toolbar);
        imageView = (ImageView) findViewById(R.id.activity_zhihu_layout_collapsing_image);
        webView = (WebView) findViewById(R.id.activity_zhihu_layout_webview);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.activity_zhihu_layout_float);
        //设置webview的缓存机制
        WebSettings ws = webView.getSettings();
        if (isnetwork) {
            ws.setCacheMode(WebSettings.LOAD_DEFAULT);//有网时加载
        } else {
            ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没有网时加载
        }
        //设置标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //去掉默认标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //设置CollapsingToolbarLayout中的标题
        textView.setText(mdbZhihuStors.getDb_zs_title());
        //监听CollapsingToolbarLayout的折叠、展开状态
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {
                    //展开状态
                    collapsingToolbar.setTitle("");
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    collapsingToolbar.setTitle(mdbZhihuStors.getDb_zs_title());
                }else {
                    //中间状态
                    collapsingToolbar.setTitle("");
                }
            }
        });
        //CollapsingToolbarLayout中的图
        Glide.with(this).load(mdbZhihuStors.getDb_zs_image())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
        //设置正文
        webView.loadDataWithBaseURL("x-data://base",result,"text/html","utf-8",null);
        //设置悬浮按钮的图案，判断这个内容是否被收藏
        final List<dbZhihuNews> news = DataSupport.select("db_zn_id", "db_zn_collection")
                .where("db_zn_id = ?", mdbZhihuStors.getDb_zs_id())
                .find(dbZhihuNews.class);
        if (news.size() != 0) {
            if (news.get(0).isDb_zn_collection()){
                floatingActionButton.setImageResource(R.mipmap.ic_on_star);
                isCollection = true;
            }else {
                floatingActionButton.setImageResource(R.mipmap.ic_no_star);
                isCollection = false;
            }
        }else {
            floatingActionButton.setImageResource(R.mipmap.ic_no_star);
            isCollection = false;
        }
        //设置FloatingActionButton的点击事件
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbZhihuNews mdbZhihuNews = new dbZhihuNews();
                if (isCollection) {
                    mdbZhihuNews.setToDefault("db_zn_collection");
                    floatingActionButton.setImageResource(R.mipmap.ic_no_star);
                    Snackbar.make(view, "取消收藏~", Snackbar.LENGTH_SHORT).show();
                    isCollection = false;
                }else {
                    mdbZhihuNews.setDb_zn_collection(true);
                    floatingActionButton.setImageResource(R.mipmap.ic_on_star);
                    Snackbar.make(view, "已收藏~", Snackbar.LENGTH_SHORT).show();
                    isCollection = true;
                }
                mdbZhihuNews.updateAll("db_zn_id = ?", news.get(0).getDb_zn_id());
                floatingActionButton.invalidate();//刷新floatingActionButton的图标
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 销毁所占内存
     */
    private void end() {
        //销毁Webview
        if (webView != null) {
            //加载内容置为null
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            //清除历史
            webView.clearHistory();
            //在父容器中把webview删除
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        //移除CollapsingToolbarLayout监听事件
        appBarLayout.removeOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                if( state == State.EXPANDED ) {
                    //展开状态
                    collapsingToolbar.setTitle("");
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    collapsingToolbar.setTitle(mdbZhihuStors.getDb_zs_title());
                }else {
                    //中间状态
                    collapsingToolbar.setTitle("");
                }
            }
        });
        //清除图片
        Glide.clear(imageView);//停止加载
        Glide.get(this).clearMemory();//清理内存缓存
        //将各个组件置null，清除内存
        imageView = null;
        toolbar = null;
        storyBean = null;
        mdbZhihuStors = null;
        floatingActionButton = null;
        cache.clear();
        cache = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (toolbar.getId() != R.id.activity_zhihu_null_layout_collapsing_toolbar){
            end();
        }
    }
}
