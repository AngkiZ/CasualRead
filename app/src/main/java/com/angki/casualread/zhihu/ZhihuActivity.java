package com.angki.casualread.zhihu;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.angki.casualread.R;
import com.angki.casualread.util.Api;
import com.angki.casualread.util.HttpUtil;
import com.angki.casualread.util.ToastUtil;
import com.angki.casualread.util.Utility;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBeans;
import com.angki.casualread.zhihu.gson.ZhihuDailyStory.StoryBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ZhihuActivity extends AppCompatActivity {

    private WebView webView;

    private Toolbar toolbar;

    private CollapsingToolbarLayout collapsingToolbar;

    private ImageView imageView;

    private StoryBean storyBean = new StoryBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihu_layout);

        String newsId = getIntent().getStringExtra("news_id");
        GetData(newsId);

    }

    private void GetData(final String newsId) {

        String url = Api.ZHIHU_NEWS + newsId;

        HttpUtil.sendOkHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ZhihuActivity.this);
                        String JSON = prefs.getString("Data", null);
                        storyBean = Utility.handleZHNResponse(JSON);
                        String html = CombinedData(storyBean);
                        LoadModule(html);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //获取Json数据
                final String responseData = response.body().string();
                //解析Json数据
                storyBean = Utility.handleZHNResponse(responseData);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences.Editor editor = PreferenceManager
                                .getDefaultSharedPreferences(ZhihuActivity.this).edit();
                        editor.putString("Data", responseData);
                        editor.apply();
                        String html = CombinedData(storyBean);
                        LoadModule(html);
                    }
                });
            }
        });
    }

    /**
     * 组合数据
     */
    private String CombinedData(StoryBean storyBean) {

        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";
        String data = storyBean.getBody();
        data = data.replace("<div class=\"img-place-holder\">", "");
        data = data.replace("<div class=\"headline\">", "");

        String html = new StringBuffer()
                .append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n")
                .append("<head>\n")
                .append("\t<meta charset=\"utf-8\" />")
                .append(css)
                .append("\n</head>\n")
                .append(data)
                .append("</body></html>").toString();

        return html;
    }

    /**
     * 加载组件
     */
    private void LoadModule(String result) {

        toolbar = (Toolbar) findViewById(R.id.activity_zhihu_layout_collapsing_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.activity_zhihu_layout_collapsing);
        imageView = (ImageView) findViewById(R.id.activity_zhihu_layout_collapsing_image);
        webView = (WebView) findViewById(R.id.activity_zhihu_layout_webview);
        test();
        //设置标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(storyBean.getTitle());
        Glide.with(this).load(storyBean.getImage())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
        //设置正文
        webView.loadDataWithBaseURL("x-data://base",result,"text/html","utf-8",null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 销毁Webview
     */
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    private void test() {

        WebSettings ws = webView.getSettings();
        ConnectivityManager manager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        }else {
            ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }
}
