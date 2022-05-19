package com.example.gongguham_;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

public class PayInfoActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payinfo);
        final TextView accountValueTextView = findViewById(R.id.accountValueText);
        final TextView accountTextView = findViewById(R.id.accountText);


        Spinner accountSpinner = (Spinner) findViewById(R.id.spinner_account);
        ArrayAdapter accountAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_account, android.R.layout.simple_spinner_item);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(accountAdapter);

        findViewById(R.id.changeAccountButton).setOnClickListener(onClickListener);
        findViewById(R.id.chargeButton).setOnClickListener(onClickListener);
        findViewById(R.id.sendButton).setOnClickListener(onClickListener);
        findViewById(R.id.refundButton).setOnClickListener(onClickListener);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            accountValueTextView.setText(document.getData().get("accountValue").toString());
                            accountTextView.setText(document.getData().get("account").toString());
                        }
                    }
                }
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.changeAccountButton:
                    Log.e("클릭", "클릭");
                    accountUpdate();
                    break;

                case R.id.chargeButton:
                    startToast("추후 지원예정입니다.");
                    break;

                case R.id.sendButton:
                    startToast("추후 지원예정입니다.");
                    break;

                case R.id.refundButton:
                    startToast("추후 지원예정입니다.");
                    break;
            }
        }
    };

    private void accountUpdate() {
        EditText accountC = (EditText) findViewById(R.id.changeAccountEditText);
        String account = accountC.getText().toString();

        Spinner accountS = (Spinner) findViewById(R.id.spinner_account);
        String accountValue = accountS.getSelectedItem().toString();

        if (account.length() > 0) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            accountInfo accountInfo = new accountInfo(accountValue, account);

            if (user != null) {
                db.collection("users").document(user.getEmail()).set(accountInfo, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("계좌 변경을 성공하였습니다.");
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("계좌변경을 실패하였습니다.");
                            }
                        });
            }


        }

    }

    private  void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}



