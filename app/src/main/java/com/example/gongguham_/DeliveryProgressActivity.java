package com.example.gongguham_;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        setContentView(R.layout.activity_delivery_progress);
        Bundle bundle = new Bundle(1);
        bundle.putString("dbTitle",dbTitle);
        hostFragment.setArguments(bundle);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.progress_layout, hostFragment).commitAllowingStateLoss();




    }
}