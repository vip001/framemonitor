package com.vip001.framemonitor.exam;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.vip001.framemonitor.BaseActivity;
import com.vip001.framemonitor.R;
import com.vip001.monitor.core.FrameMonitorManager;

/**
 * Created by xxd on 2018/8/14
 */
public class Example3Activity extends BaseActivity {
    private Button mStopBtn;
    private Button mStartBtn;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        mStartBtn = (Button) this.findViewById(R.id.start);
        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameMonitorManager.getInstance().startFlowCal();
            }
        });
        mStopBtn = (Button) this.findViewById(R.id.stop);
        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameMonitorManager.getInstance().stopFlowCal();
            }
        });
        mWebView = (WebView) this.findViewById(R.id.webview);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://www.baidu.com/");
    }


}
