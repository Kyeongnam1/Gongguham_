package com.example.gongguham_;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TransmissionGuestActivity extends AppCompatActivity {

    Button payButton, completeButton;
    TextView positionText, nameText, bankText, accountNumberText, emailText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent1 = getIntent();
        String dbTitle = intent1.getStringExtra("dbTitle");
        setContentView(R.layout.activity_transmission_guest);
        payButton = findViewById(R.id.pay_Button);
        positionText = findViewById(R.id.applicant_position_text);
        nameText = findViewById(R.id.applicant_name_text);
        bankText = findViewById(R.id.bank_name_text);
        accountNumberText = findViewById(R.id.account_number_text);
        emailText = findViewById(R.id.applicant_email_text);
        completeButton = findViewById(R.id.complete_Button);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(dbTitle)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        positionText.setText("글쓴이");
                        nameText.setText(document.getData().get("name1").toString());
                        bankText.setText(document.getData().get("accountValue1").toString());
                        accountNumberText.setText(document.getData().get("account1").toString());
                        emailText.setText(document.getData().get("postEmail").toString());
                    }
                    else {
                        Log.d(TAG, "No such document");
                    }
                }else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransmissionGuestActivity.this, PayInfoActivity.class);
                startActivity(intent);
            }
        });

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransmissionGuestActivity.this, DeliveryProgressActivity.class);
                startActivity(intent);
            }
        });



    }


}
