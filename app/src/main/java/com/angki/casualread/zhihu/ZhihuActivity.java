package com.angki.casualread.zhihu;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.angki.casualread.util.NetworkStatus;
import com.angki.casualread.util.ToastUtil;
import com.angki.casualread.util.Utility;
import com.angki.casualread.util.dbUtil;
import com.angki.casualread.zhihu.db.dbZhihuNews;
import com.angki.casualread.zhihu.db.dbZhihuStors;
import com.angki.casualread.zhihu.gson.ZhihuDailyNews.NewsBeans;
import com.angki.casualread.zhihu.gson.ZhihuDailyStory.StoryBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ZhihuActivity extends AppCompatActivity {

    private WebView webView;

    private Toolbar toolbar;

    private CollapsingToolbarLayout collapsingToolbar;

    private ImageView imageView;

    private StoryBean storyBean = new StoryBean();

    private dbZhihuStors mdbZhihuStors;

    private FloatingActionButton floatingActionButton;

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

                final List<dbZhihuStors> data = DataSupport
                        .where("db_zs_id like ?", "%" + newsId + "%")
                        .find(dbZhihuStors.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (data.size() == 0) {

                            ToastUtil.showToast(getApplicationContext(), "哇，没有网络也没有缓存");
                        } else {
                            String html = CombinedData(data.get(0));
                            LoadModule(html, data.get(0));
                        }
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
    private void LoadModule(String result, dbZhihuStors mdbZhihuStors) {

        toolbar = (Toolbar) findViewById(R.id.activity_zhihu_layout_collapsing_toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.activity_zhihu_layout_collapsing);
        imageView = (ImageView) findViewById(R.id.activity_zhihu_layout_collapsing_image);
        webView = (WebView) findViewById(R.id.activity_zhihu_layout_webview);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.activity_zhihu_layout_float);
        //设置webview的缓存机制
        WebSettings ws = webView.getSettings();
        boolean b = new NetworkStatus().judgment(getApplicationContext());
        if (b) {
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
        collapsingToolbar.setTitle(mdbZhihuStors.getDb_zs_title());
        Glide.with(this).load(mdbZhihuStors.getDb_zs_image())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
        //设置正文
        webView.loadDataWithBaseURL("x-data://base",result,"text/html","utf-8",null);

        final List<dbZhihuNews> news = DataSupport.select("db_zn_id", "db_zn_collection")
                .where("db_zn_id = ?", mdbZhihuStors.getDb_zs_id())
                .find(dbZhihuNews.class);
        if (news.get(0).isDb_zn_collection()) {
            floatingActionButton.setImageResource(R.mipmap.ic_on_star);
        }else {
            floatingActionButton.setImageResource(R.mipmap.ic_no_star);
        }
        //设置FloatingActionButton的点击事件
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbZhihuNews mdbZhihuNews = new dbZhihuNews();
                if (news.get(0).isDb_zn_collection()) {
                    mdbZhihuNews.setDb_zn_collection(false);
                    floatingActionButton.setImageResource(R.mipmap.ic_no_star);
                }else {
                    mdbZhihuNews.setDb_zn_collection(true);
                    floatingActionButton.setImageResource(R.mipmap.ic_on_star);
                }
                mdbZhihuNews.updateAll("db_zn_id = ?", news.get(0).getDb_zn_id());
            }
        });
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

}
