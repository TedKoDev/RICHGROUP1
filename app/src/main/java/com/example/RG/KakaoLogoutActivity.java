package com.example.RG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class KakaoLogoutActivity extends AppCompatActivity {


    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_logout);
        getSupportActionBar().hide();

        webview =findViewById(R.id.webview);

        webview.loadUrl("https://kauth.kakao.com/oauth/logout?client_id=f41bb16e8aa72a6b43d4f4727c481489&logout_redirect_uri=http://3.38.117.213/logoutdone.html");


    }
}