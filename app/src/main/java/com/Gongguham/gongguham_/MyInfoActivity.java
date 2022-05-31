package com.Gongguham.gongguham_;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyInfoActivity extends AppCompatActivity {

    private String TAG = "MyInfoActivity";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        final TextView nameTextView = findViewById(R.id.nameText);
        final TextView phoneNumberTextView = findViewById(R.id.phoneNumberText);
        final TextView genderTextView = findViewById(R.id.genderText);
        final TextView accountValueTextView = findViewById(R.id.accountValueText);
        final TextView accountTextView = findViewById(R.id.accountText);
        final TextView emailTextView = findViewById(R.id.emailText);
        final TextView birthDayTextView = findViewById(R.id.birthdayText);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.e(TAG, "DocumentSnapshot data: " + document.getData());

                            nameTextView.setText(document.getData().get("name").toString());
                            phoneNumberTextView.setText(document.getData().get("phoneNumber").toString());
                            genderTextView.setText(document.getData().get("gender").toString());
                            accountValueTextView.setText(document.getData().get("accountValue").toString());
                            accountTextView.setText(document.getData().get("account").toString());
                            emailTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            birthDayTextView.setText(document.getData().get("birthday").toString());
                        } else {
                            Log.e(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
}