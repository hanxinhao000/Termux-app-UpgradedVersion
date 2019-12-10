package main.java.com.termux.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;

import com.termux.R;

public class LunTanActivity extends AppCompatActivity {

    private EditText et_sousuo;
    private ImageView sousuo;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lun_tan);


        et_sousuo = findViewById(R.id.et_sousuo);
        sousuo = findViewById(R.id.sousuo);
        webview = findViewById(R.id.webview);


        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        //设置进度条

        et_sousuo.setText("http://45.205.175.163:29952/luntan/upload/forum.php?forumlist=1&mobile=2");

        webview.loadUrl("http://45.205.175.163:29952/luntan/upload/forum.php?forumlist=1&mobile=2");

        sousuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.loadUrl(et_sousuo.getText().toString());
            }
        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // webView加载web资源
                view.loadUrl(url);
                return true;
            }
        });
    }


}
