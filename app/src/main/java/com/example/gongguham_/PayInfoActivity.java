package com.example.gongguham_;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class PayInfoActivity extends AppCompatActivity {

    TextView user_cash_amount;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payinfo);
        final TextView accountValueTextView = findViewById(R.id.accountValueText);
        final TextView accountTextView = findViewById(R.id.accountText);

        final String username;

        // 사용자 금액
        user_cash_amount = findViewById(R.id.user_cash_amount);


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
                            user_cash_amount.setText(document.getData().get("point").toString());
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
                    cashCharge();
                    break;

                case R.id.sendButton:
                    cashSend();
                    break;

                case R.id.refundButton:
                    cashReturn();
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

    private void cashCharge(){
        View dialogView = getLayoutInflater().inflate(R.layout.cash_charge, null);
        TextView cash_charge_username = (TextView) dialogView.findViewById(R.id.cash_charge_username);
        EditText charge_cash_amount = (EditText) dialogView.findViewById(R.id.cash_charge_amount);

        //charge_cash_amount.setText(username);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setPositiveButton("충전", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.dismiss();
            }
        })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void cashReturn(){
        View dialogView = getLayoutInflater().inflate(R.layout.cash_return, null);
        TextView cash_return_username = (TextView) dialogView.findViewById(R.id.cash_return_username);
        EditText cash_return_money = (EditText) dialogView.findViewById(R.id.cash_return_amount);
        //cash_return_amount.setText(username);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder
                .setPositiveButton("환급", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void cashSend(){
        View dialogView = getLayoutInflater().inflate(R.layout.cash_send, null);
        TextView cash_send_username = (TextView) dialogView.findViewById(R.id.cash_send_username);

        EditText cash_send_email = (EditText) dialogView.findViewById(R.id.cash_send_email);
        EditText cash_send_money = (EditText) dialogView.findViewById(R.id.cash_send_amount);

        //cash_return_amount.setText(username);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder
                .setPositiveButton("송금", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();
    }




}



