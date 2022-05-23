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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TransmissionGuestActivity extends AppCompatActivity {

    Button payButton, completeButton;
    TextView positionText, nameText, bankText, accountNumberText, emailText, priceText;
    String userName;
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
        priceText = findViewById(R.id.send_price_text);
        completeButton = findViewById(R.id.complete_Button);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentUserReference = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        documentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            userName = document.getData().get("name").toString();
                        }
                    }
                }
            }
        });



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

                        int fee = Integer.parseInt(document.getData().get("deliveryFee").toString());
                        int curPerson = Integer.parseInt(document.getData().get("curPerson").toString());
                        int curFee = fee/curPerson;
                        String order = document.getData().get(userName).toString();
                        String food_Price = "foodPrice"+ order;
                        String food_Name = "foodName"+order;
                        String foodName = document.getData().get(food_Name).toString();
                        int foodPrice = Integer.parseInt(document.getData().get(food_Price).toString());

                        int price = foodPrice + curFee;
                        String priceText_source = foodName +"값: "+ Integer.toString(foodPrice)+" +배달요금 값: "+ Integer.toString(curFee)+" ="+Integer.toString(price)+"원";
                        priceText.setText(priceText_source);
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
