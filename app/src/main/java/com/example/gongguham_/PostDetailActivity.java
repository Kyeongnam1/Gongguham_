package com.example.gongguham_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostDetailActivity extends AppCompatActivity {

    Button tmBtn;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        final TextView titleTextView = findViewById(R.id.post_detail_title);
        final TextView contentTextView = findViewById(R.id.post_detail_content);
        final TextView placeTextView = findViewById(R.id.post_detail_place);
        final TextView hourTextView = findViewById(R.id.post_detail_time_hour);
        final TextView minuteTextView = findViewById(R.id.post_detail_time_minute);
        final TextView maxPersonTextView = findViewById(R.id.post_detail_maxperson);
        tmBtn = findViewById(R.id.tmbtn);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("posts").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            titleTextView.setText(document.getData().get("postTitle").toString());
                            contentTextView.setText(document.getData().get("postContent").toString());
                            placeTextView.setText(document.getData().get("meetingArea").toString());
                            hourTextView.setText(document.getData().get("closeTime_hour").toString());
                            minuteTextView.setText(document.getData().get("closeTime_minute").toString());
                            maxPersonTextView.setText(document.getData().get("maxPerson").toString());
                        }
                    }
                }
            }
        });




        tmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });


    }


}