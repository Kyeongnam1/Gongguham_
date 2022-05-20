package com.example.gongguham_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class TransmissionActivity extends AppCompatActivity {

    Button finishbtn;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private  TransmissionFragment transmissionFragment = new TransmissionFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transmission);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, transmissionFragment).commitAllowingStateLoss();

        finishbtn = findViewById(R.id.finishbutton);

        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransmissionActivity.this, DeliveryProgressActivity.class);
                startActivity(intent);
            }
        });


    }


}
