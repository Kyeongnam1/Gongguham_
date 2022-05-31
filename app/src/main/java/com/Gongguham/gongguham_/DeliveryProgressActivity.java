package com.Gongguham.gongguham_;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class DeliveryProgressActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private GuestFragment guestFragment = new GuestFragment();
    private HostFragment hostFragment = new HostFragment();

    @Override public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        Intent intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String dbTitle=intent.getStringExtra("dbTitle");
        String role = intent.getStringExtra("role");
        setContentView(R.layout.activity_delivery_progress);
        if(role.equals("글쓴이"))
        {
            Bundle bundle = new Bundle(1);
            bundle.putString("dbTitle",dbTitle);
            hostFragment.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.progress_layout, hostFragment).commitAllowingStateLoss();
        }
        else if(role.equals("참여자"))
        {
            Bundle bundle = new Bundle(1);
            bundle.putString("dbTitle",dbTitle);
            guestFragment.setArguments(bundle);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.progress_layout, guestFragment).commitAllowingStateLoss();
        }

    }
    public void refresh() //새로고침
    {
        finish();//인텐트 종료
        overridePendingTransition(0, 0);//인텐트 효과 없애기
        Intent intent = getIntent(); //인텐트
        startActivity(intent); //액티비티 열기
        overridePendingTransition(0, 0);//인텐트 효과 없애기
    }
}