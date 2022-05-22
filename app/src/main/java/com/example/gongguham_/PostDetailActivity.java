package com.example.gongguham_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PostDetailActivity extends AppCompatActivity {

    TextView postDetail;
    Button tmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postDetail = findViewById(R.id.post_info);

        tmBtn = findViewById(R.id.tmbtn);




        tmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostDetailActivity.this, TransmissionActivity.class);
                startActivity(intent);
            }
        });


    }


}