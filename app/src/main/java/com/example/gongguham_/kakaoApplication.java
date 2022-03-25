package com.example.gongguham_;

import android.app.Application;
import com.kakao.sdk.common.KakaoSdk;

public class kakaoApplication extends Application {

    @Override
    public void onCreate(){
        super.onCreate();
        KakaoSdk.init(this, "c0c052ba88bd5263807182a1ac6489dd");
    }
}
