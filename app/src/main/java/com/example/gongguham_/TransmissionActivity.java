package com.example.gongguham_;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TransmissionActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private  TransmissionFragment transmissionFragment = new TransmissionFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmission);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, transmissionFragment).commitAllowingStateLoss();
    }


}
