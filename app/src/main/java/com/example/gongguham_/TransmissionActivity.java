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
        Intent intent1 = getIntent();
        String dbTitle = intent1.getStringExtra("dbTitle");
        setContentView(R.layout.activity_transmission);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, transmissionFragment).commitAllowingStateLoss();

        finishbtn = findViewById(R.id.finishbutton);

        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransmissionActivity.this, DeliveryProgressActivity.class);
                intent.putExtra("dbTitle", dbTitle);
                startActivity(intent);
            }
        });


    }


}
