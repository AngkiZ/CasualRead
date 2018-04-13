package com.angki.casualread.mvp.ui.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.angki.casualread.R;
import com.angki.casualread.mvp.model.entity.bean.dbGank;
import com.angki.casualread.app.util.NetworkStatus;
import com.angki.casualread.app.util.ToastUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

public class ActivityGank extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView webView;
    private FloatingActionButton floatingActionButton;
    private boolean isCollection;//判断是否收藏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank_layout);

        String url = getIntent().getStringExtra("gank_url");
        //判断是否有网
        boolean isnetwork = new NetworkStatus().judgment(ActivityGank.this);

        LoadMoudle(url);
        SiteWebView(isnetwork);

    }

    private void LoadMoudle(String url) {

        toolbar = (Toolbar) findViewById(R.id.activity_gank_layout_toolbar);
        webView = (WebView) findViewById(R.id.activity_gank_layout_webview);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.activity_gank_layout_float);

        //设置标题栏
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        //设置webview
        webView.loadUrl(url);
        //设置不用系统浏览器打开,直接显示在当前Webview
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            //设置加载错误时的回调
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("onReceivedError", "errorCode: " + errorCode);
                switch (errorCode) {
                    case ERROR_HOST_LOOKUP:
                        ToastUtil.showToast(ActivityGank.this, "没有网络也没有缓存233");
                        break;
                }
            }
        });
        //设置WebChromeClient类
        webView.setWebChromeClient(new WebChromeClient() {

            String mtitle;
            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                mtitle = title;
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    toolbar.setTitle(mtitle);
                } else {
                    //加载中
                    toolbar.setTitle("加载中~");
                }
            }
        });
        //设置悬浮按钮的图案，判断这个内容是否被收藏
        final List<dbGank> news = DataSupport.select("db_gank_id", "db_gank_url", "db_gank_collection")
                .where("db_gank_url = ?", url)
                .find(dbGank.class);
        if (news.size() != 0) {
            if (news.get(0).isDb_gank_collection()){
                floatingActionButton.setImageResource(R.mipmap.ic_on_star);
                isCollection = true;
            }else {
                floatingActionButton.setImageResource(R.mipmap.ic_no_star);
                isCollection = false;
            }
        } else {
            floatingActionButton.setImageResource(R.mipmap.ic_no_star);
            isCollection = false;
        }
        //设置FloatingActionButton的点击事件
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbGank mdbGank = new dbGank();
                Log.d("----", "floatingActionButton: " + isCollection);
                if (isCollection) {
                    mdbGank.setToDefault("db_gank_collection");
                    floatingActionButton.setImageResource(R.mipmap.ic_no_star);
                    Snackbar.make(view, "取消收藏~", Snackbar.LENGTH_SHORT).show();
                    isCollection = false;
                }else {
                    mdbGank.setDb_gank_collection(true);
                    floatingActionButton.setImageResource(R.mipmap.ic_on_star);
                    Snackbar.make(view, "已收藏~", Snackbar.LENGTH_SHORT).show();
                    isCollection = true;
                }
                mdbGank.updateAll("db_gank_id = ?", news.get(0).getDb_gank_id());
                floatingActionButton.invalidate();//刷新floatingActionButton的图标
            }
        });
    }

    /**
     * 设置WebView
     */
    private void SiteWebView(boolean b) {

        WebSettings ws = webView.getSettings();
        // 告诉WebView启用JavaScript执行,默认的是false
        ws.setJavaScriptEnabled(true);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        if (b) {
            ws.setCacheMode(WebSettings.LOAD_DEFAULT);//有网时加载
        }else {
            ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没有网时加载
        }
    }

    /**
     * 点击返回上一页面而不是退出浏览器
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置返回按钮
     * @param item
     * @return
     */
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
        //将各个组件置null，清除内存
        toolbar = null;
        floatingActionButton = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        end();
    }
}
