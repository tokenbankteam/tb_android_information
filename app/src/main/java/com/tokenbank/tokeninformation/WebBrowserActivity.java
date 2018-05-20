package com.tokenbank.tokeninformation;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokenbank.tokeninformation.contants.BundleConstant;
import com.tokenbank.tokeninformation.util.SnackBarUtil;

/**
 * Author: Clement
 * Create: 2018/5/20
 * Desc:
 */

public class WebBrowserActivity extends AppCompatActivity {

    private TextView tvTitle;
    private Toolbar toolbar;
    private WebView webView;
    private ProgressBar progressBar;

    private String mTitle;
    private String mUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_browser);

        initData();
        initView();
        initWebView();
    }

    private void initData() {
        mTitle = getIntent().getStringExtra(BundleConstant.TITLE);
        mUrl = getIntent().getStringExtra(BundleConstant.URL);
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        toolbar = findViewById(R.id.toolbar);
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);


        //将toolbar作为独立的控件使用，不和ActionBar进行关联
        tvTitle.setText(mTitle);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //返回上一层
                onBackPressed();
            }
        });
        toolbar.inflateMenu(R.menu.menu_detail);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_share:
                        shareText(mTitle + " " + mUrl);
                        break;
                    case R.id.menu_copy:
                        copyText(mUrl);
                        SnackBarUtil.show(toolbar, getString(R.string.copied));
                        break;
                    case R.id.menu_open:
                        openWeb(mUrl);
                        break;
                }
                return true;
            }
        });
    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        //处理加载进度
        webView.setWebChromeClient(new CustomWebChromeClient());
        webView.setWebViewClient(new CustomWebViewClient());
        //加载网页
        webView.loadUrl(mUrl);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            this.finish();
        }
    }

    public static void startWebBrowserActivity(Activity activity, String title, String url) {
        Intent intent = new Intent(activity, WebBrowserActivity.class);
        intent.putExtra(BundleConstant.TITLE, title);
        intent.putExtra(BundleConstant.URL, url);
        activity.startActivity(intent);
    }

    /**
     * 辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
     */
    class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
            progressBar.setProgress(newProgress);
        }
    }

    /**
     * 帮助WebView处理各种通知、请求事件的
     */
    class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
        }
    }

    /**
     * 分享文字
     *
     * @param text
     */
    private void shareText(String text) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setType("text/plain");
        //检查是否有满足的activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            //设置分享的标题
            startActivity(Intent.createChooser(intent, getString(R.string.share_to)));
        } else {
            SnackBarUtil.show(tvTitle, getString(R.string.no_app));
        }
    }

    /**
     * 复制文本到系统剪切板
     *
     * @param text
     */
    private void copyText(String text) {
        // 得到剪贴板管理器
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));
    }

    /**
     * 用浏览器打开
     *
     * @param url
     */
    private void openWeb(String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri contentUri = Uri.parse(url);
        intent.setData(contentUri);
        //检查是否有满足的activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            //设置分享的标题
            startActivity(Intent.createChooser(intent, getString(R.string.web_select)));
        } else {
            SnackBarUtil.show(tvTitle, getString(R.string.no_browser));
        }
    }
}
