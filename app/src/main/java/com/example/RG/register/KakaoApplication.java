package com.example.RG.register;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    private static KakaoApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;


        KakaoSdk.init(this, "602a2d05cdac0e251b9fa7bc1a93a590");
    }
	
}
