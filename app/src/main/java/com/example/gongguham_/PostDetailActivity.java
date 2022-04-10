package com.example.gongguham_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PostDetailActivity extends AppCompatActivity {

    TextView postDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        postDetail = findViewById(R.id.post_info);

        Intent intent = getIntent();

        PostItem postItem = intent.getParcelableExtra("post_indx");
        StringBuffer sb = new StringBuffer();
        
        sb.append("글 제목: " + postItem.getPostTitle() + "\n");
        sb.append("만남 장소: "+postItem.getMeetingArea() + "\n");
        sb.append("마감 시간: " + postItem.getCloseTime() + "\n");
        sb.append("최대 인원: " + postItem.getMaxPerson() + "\n");
        sb.append("글 인덱스 번호: " + postItem.getPostIdx() + "\n");

        postDetail.setText(sb);
    }
}