package com.lizij.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.lizij.IWebBinder;
import com.lizij.binder.BinderPool;
import com.lizij.binder.WebBinder;
import com.lizij.jsbridge.MyJsBridge;
import com.lizij.jsbridge.TestJsBridge;
import com.lizij.web.MyRemoteWebViewClient;
import com.lizij.web.MyWebViewClient;
import com.lizij.webview_demo.R;

import java.util.concurrent.Callable;

import bolts.Task;

/**
 * @author lizijian
 */
public class WebActivity extends AppCompatActivity {
    private static final String TAG = WebActivity.class.getSimpleName();
    private WebView webView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_web);

        Intent intent = getIntent();
        TextView mainProcessPid = findViewById(R.id.main_process_pid);
        @SuppressLint("DefaultLocale") String mainProcessText = String.format("Main Process Pid: %d\nThis Process Pid: %d", intent.getIntExtra("pid", 0), Process.myPid());
        mainProcessPid.setText(mainProcessText);

        webView = findViewById(R.id.webView);

        initWebViewWithRemoteJsBridge();
    }

    /**
     * Init a webview with jsbridge called in current process
     */
    private void initWebViewWithJsBridge() {
        if (webView == null) {
            return;
        }

        MyJsBridge jsBridge = new MyJsBridge(webView);
        jsBridge.registerJavaMethod("testJsBridge", new TestJsBridge());

        MyWebViewClient myWebViewClient = new MyWebViewClient(jsBridge);

        webView.setWebViewClient(myWebViewClient);
        webView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl("http://mytest.com");
    }

    /**
     * Init a webview with jsbridge called in main process
     */
    private void initWebViewWithRemoteJsBridge() {
        if (webView == null) {
            return;
        }

        Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                // get BinderPool
                BinderPool binderPool = BinderPool.inst();

                // bind this activity with BinderPoolService in main process
                binderPool.bind(WebActivity.this);

                // get binder from binderPool
                IBinder binder = binderPool.getWebBinder();
                final IWebBinder webBinder = IWebBinder.Stub.asInterface(binder);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // init webview
                        MyRemoteWebViewClient myWebViewClient = new MyRemoteWebViewClient(webBinder);

                        webView.setWebViewClient(myWebViewClient);
                        webView.setWebChromeClient(new WebChromeClient());
                        WebSettings webSettings = webView.getSettings();
                        webSettings.setJavaScriptEnabled(true);

                        // load
                        webView.loadUrl("http://mytest.com");
                    }
                });
                return null;
            }
        });
    }
}
