package com.example.gongguham_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";   // 사용자 정보
    private View loginButton, logoutbutton;  // 로그인 상태면 로그아웃 버튼 띄우게, 로그아웃 상태면 로그인 버튼 띄우기 위해
    private TextView nickname;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.login);
        logoutbutton = findViewById(R.id.logout);
        nickname = findViewById(R.id.nickname);
        profileImage = findViewById(R.id.profile);

        // callback 처리는 똑같음
        Function2<OAuthToken, Throwable, Unit> callback = (oAuthToken, throwable) -> {    // invoke 가 callback로 호출, token호출되면 로그인 성공 null이면 실패
            if(oAuthToken != null){ // 정상 호출
                Log.e(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
            }
            if(throwable != null) { // 결과 오류시
                Log.i(TAG, "로그인 실패",throwable);
            }
            updateKaKaoLoginUi();
            return null;
        };

        // login, logout button activation
        // login버튼 클릭시 login 수행 -> 애뮬에 카카오톡 설치여부에 따라 로그인 방식이 다름
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)) { // 카톡 설치되어 있음
                    UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, callback);


                }
                else{  // 카톡 설치 안되어있음
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, callback);
                }
            }
        });
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserApiClient.getInstance().logout(throwable -> { // logout 호출시 invoke로 callback 전달
                    updateKaKaoLoginUi();
                    return null;
                });
            }
        });

        updateKaKaoLoginUi();
    }

    private void updateKaKaoLoginUi(){
        UserApiClient.getInstance().me((user, throwable) -> {
            if(user!=null){     // user 로그인 상태

                Log.i(TAG, "invoke: id=" + user.getId());
                Log.i(TAG, "invoke: email=" + user.getKakaoAccount().getEmail());
                Log.i(TAG, "invoke: nickname=" + user.getKakaoAccount().getProfile().getNickname());
                Log.i(TAG, "invoke: gender=" + user.getKakaoAccount().getGender());
                Log.i(TAG, "invoke: age=" + user.getKakaoAccount().getAgeRange());

                nickname.setText(user.getKakaoAccount().getProfile().getNickname()); // user nickname set
                Glide.with(profileImage).load(user.getKakaoAccount().getProfile().getThumbnailImageUrl()).circleCrop().into(profileImage); // user profile image


                loginButton.setVisibility(View.GONE);
                logoutbutton.setVisibility(View.VISIBLE);
            }else{              // user 로그아웃 상태
                nickname.setText(null);
                profileImage.setImageBitmap(null);

                loginButton.setVisibility(View.VISIBLE);
                logoutbutton.setVisibility(View.GONE);
            }
            return null;
        });
    }



}

/* key hash logcat에 띄우기
          Log.d("getKeyHash", "" + getKeyHash(MainActivity.this));
*/

// key hash 얻기위한 코드
    /*
    public static String getKeyHash(final Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null)
                return null;

            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return android.util.Base64.encodeToString(md.digest(), android.util.Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    */