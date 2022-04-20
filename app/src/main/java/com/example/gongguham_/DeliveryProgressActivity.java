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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_progress);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.progress_layout, hostFragment).commitAllowingStateLoss();


    }
}