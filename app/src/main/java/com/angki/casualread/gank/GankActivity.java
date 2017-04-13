package com.angki.casualread.gank;

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
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.angki.casualread.R;
import com.angki.casualread.util.NetworkStatus;
import com.angki.casualread.util.ToastUtil;

public class GankActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private WebView webView;

    private boolean isnetwork;//判断是否有网

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gank_layout);

        String url = getIntent().getStringExtra("gank_url");
        isnetwork = new NetworkStatus().judgment(getApplicationContext());

        LoadMoudle(url);
        SiteWebView(isnetwork);

    }

    private void LoadMoudle(String url) {

        toolbar = (Toolbar) findViewById(R.id.activity_gank_layout_toolbar);
        webView = (WebView) findViewById(R.id.activity_gank_layout_webview);

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
                        ToastUtil.showToast(getApplicationContext(), "没有网络也没有缓存233");
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
