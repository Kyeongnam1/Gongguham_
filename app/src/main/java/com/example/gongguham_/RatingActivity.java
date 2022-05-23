package com.example.gongguham_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;

public class RatingActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference mDatabase;
    double totalScore;
    double averageScore;
    double number;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Intent intent1 = getIntent();
        String email = intent1.getStringExtra("EMAIL");
        String collection = email + "review";
        btn = findViewById(R.id.rate_submit_btn);

        TextView usernameTextView = findViewById(R.id.rating_username);
        usernameTextView.setText(email);

        Spinner rateSpinner = (Spinner)findViewById(R.id.spinner_review_rate);
        ArrayAdapter rateAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_rate, android.R.layout.simple_spinner_item);
        rateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rateSpinner.setAdapter(rateAdapter);

        Spinner rateS = (Spinner) findViewById(R.id.spinner_review_rate);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int rate = Integer.parseInt(rateS.getSelectedItem().toString());
                ReviewInfo reviewInfo = new ReviewInfo(user.getEmail(), rate);

                if (user != null) {
                    Calendar calendar= Calendar.getInstance(); //현재 시간을 가지고 있는 객체
                    String time = calendar.get(Calendar.YEAR)+ "." + (calendar.get(Calendar.MONTH)+1) + "." + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.HOUR_OF_DAY) +  "시" + calendar.get(Calendar.MINUTE) + "분" + calendar.get(Calendar.SECOND) + "초";
                    //db에 컬렉션 만들어서 유저마다 리뷰 필드 만들기
                    db.collection(collection).document(time).set(reviewInfo) //유저가 중복으로 리뷰를 적었을때 반영하기위해 time document
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startToast("평가하였습니다.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });

                    //user 컬렉션 수정하기

                    mDatabase = db.collection("users").document(email);
                    mDatabase.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            if (task.isSuccessful()){
                                if(document != null){
                                    if(document.exists()){
                                        totalScore = Double.parseDouble(document.getData().get("reviewTotalScore").toString());
                                        averageScore = Double.parseDouble(document.getData().get("reviewAvScore").toString());
                                        number = Double.parseDouble(document.getData().get("reviewNumber").toString());

                                        totalScore += rate;
                                        number += 1;
                                        averageScore = totalScore / number;

                                        ReviewToUserInfo reviewToUserInfo = new ReviewToUserInfo(totalScore, averageScore, number);

                                        db.collection("users").document(email).set(reviewToUserInfo, SetOptions.merge())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });


                                    }
                                }
                            }

                        }
                    });
                }
                finish();
            }
        });
    }
    private  void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}