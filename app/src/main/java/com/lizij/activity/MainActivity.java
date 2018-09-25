package com.lizij.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lizij.jsbridge.MyRemoteJsBridge;
import com.lizij.jsbridge.TestJsBridge;
import com.lizij.webview_demo.R;

/**
 * @author lizijian
 */
public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.btn);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra("pid", Process.myPid());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // register jsbridge function for remote process usage
        MyRemoteJsBridge.inst().registerJavaMethod("testJsBridge", new TestJsBridge());
    }
}
