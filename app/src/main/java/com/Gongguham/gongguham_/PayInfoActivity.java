package com.Gongguham.gongguham_;

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
import androidx.appcompat.app.AlertDialog;
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

public class PayInfoActivity extends AppCompatActivity {

    TextView user_cash_amount;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    String collection = user.getEmail() + "history";
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
        findViewById(R.id.historyButton).setOnClickListener(onClickListener);

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
                case R.id.historyButton:
                    cashHistory();
                    break;
            }
        }
    };

    private void cashHistory(){
        Intent intent = new Intent(getApplicationContext(), CashHistory.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

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


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            cash_charge_username.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            user_cash_amount.setText(document.getData().get("point").toString());

                            int point = Integer.parseInt(document.getData().get("point").toString());

                            builder.setPositiveButton("충전", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    EditText charge_cash_amount = (EditText) dialogView.findViewById(R.id.cash_charge_amount);
                                    String amount = charge_cash_amount.getText().toString();
                                    int amount_I = Integer.parseInt(amount);
                                    int plus = point + amount_I;
                                    user_cash_amount.setText(Integer.toString(plus));
                                    Calendar calendar= Calendar.getInstance(); //현재 시간을 가지고 있는 객체
                                    String time = calendar.get(Calendar.YEAR)+ "." + (calendar.get(Calendar.MONTH)+1) + "." + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.HOUR_OF_DAY) +  "시" + calendar.get(Calendar.MINUTE) + "분" + calendar.get(Calendar.SECOND) + "초";

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                    PayInfo payInfo = new PayInfo(plus);
                                    HistoryInfo historyInfo = new HistoryInfo(user.getEmail(), amount_I, 0, plus, "공구함", time);

                                    if (user != null) {
                                        db.collection("users").document(user.getEmail()).set(payInfo, SetOptions.merge())
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        startToast("충전하였습니다.");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });

                                        db.collection(collection).document(time).set(historyInfo)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        startToast("내역 갱신");
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                    }
                                                });
                                    }
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
                }
            }
        });
    }

    private void cashReturn(){
        View dialogView = getLayoutInflater().inflate(R.layout.cash_return, null);
        TextView cash_return_username = (TextView) dialogView.findViewById(R.id.cash_return_username);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            cash_return_username.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            user_cash_amount.setText(document.getData().get("point").toString());

                            int point = Integer.parseInt(document.getData().get("point").toString());

                            builder.setPositiveButton("환급", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    EditText cash_return_money = (EditText) dialogView.findViewById(R.id.cash_return_amount);
                                    String amount = cash_return_money.getText().toString();
                                    Calendar calendar= Calendar.getInstance(); //현재 시간을 가지고 있는 객체
                                    String time = calendar.get(Calendar.YEAR)+ "." + (calendar.get(Calendar.MONTH)+1) + "." + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.HOUR_OF_DAY) +  "시" + calendar.get(Calendar.MINUTE) + "분" + calendar.get(Calendar.SECOND) + "초";

                                    int amount_I = Integer.parseInt(amount);
                                    int minus = point - amount_I;
                                    if(minus<0){
                                        startToast("잔액이 부족합니다.");
                                    }else{
                                        user_cash_amount.setText(Integer.toString(minus));

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                                        PayInfo payInfo = new PayInfo(minus);
                                        HistoryInfo historyInfo = new HistoryInfo(user.getEmail(), 0, amount_I, minus, "공구함", time);

                                        if (user != null) {
                                            db.collection("users").document(user.getEmail()).set(payInfo, SetOptions.merge())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            startToast("환급하였습니다.");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });

                                            db.collection(collection).document(time).set(historyInfo)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            startToast("내역 갱신");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                        }
                                        dialog.dismiss();
                                    }

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
                }
            }
        });
    }

    private void cashSend(){
        View dialogView = getLayoutInflater().inflate(R.layout.cash_send, null);
        TextView cash_send_username = (TextView) dialogView.findViewById(R.id.cash_send_username);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            cash_send_username.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            user_cash_amount.setText(document.getData().get("point").toString());

                            int point = Integer.parseInt(document.getData().get("point").toString());


                            builder.setPositiveButton("송금", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    EditText cash_send_email = (EditText) dialogView.findViewById(R.id.cash_send_email);
                                    EditText cash_send_money = (EditText) dialogView.findViewById(R.id.cash_send_amount);
                                    String email = cash_send_email.getText().toString();
                                    String amount = cash_send_money.getText().toString();

                                    Calendar calendar= Calendar.getInstance(); //현재 시간을 가지고 있는 객체
                                    String time = calendar.get(Calendar.YEAR)+ "." + (calendar.get(Calendar.MONTH)+1) + "." + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.HOUR_OF_DAY) +  "시" + calendar.get(Calendar.MINUTE) + "분" + calendar.get(Calendar.SECOND) + "초";

                                    int amount_I = Integer.parseInt(amount);
                                    int minus = point - amount_I;

                                    if(minus<0){
                                        startToast("잔액이 부족합니다.");
                                    }else{
                                        user_cash_amount.setText(Integer.toString(minus));

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                                        PayInfo payInfo1 = new PayInfo(minus);

                                        HistoryInfo historyInfoS = new HistoryInfo(user.getEmail(), 0, amount_I, minus, email, time);

                                        if (user != null) {
                                            db.collection("users").document(user.getEmail()).set(payInfo1, SetOptions.merge())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            startToast("송금하였습니다.");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });

                                            DocumentReference documentReference2 = FirebaseFirestore.getInstance().collection("users").document(email);
                                            documentReference2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document != null) {
                                                            if (document.exists()) {
                                                                int pointTo = Integer.parseInt(document.getData().get("point").toString());
                                                                int plus = pointTo + amount_I;
                                                                String collection2 = email + "history";

                                                                HistoryInfo historyInfoR = new HistoryInfo(email, amount_I, 0, plus, user.getEmail(), time);
                                                                PayInfo payInfo2 = new PayInfo(plus);

                                                                db.collection("users").document(email).set(payInfo2, SetOptions.merge())
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

                                                                db.collection(collection2).document(time).set(historyInfoR)
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

                                            db.collection(collection).document(time).set(historyInfoS)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            startToast("내역 갱신");
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                        }
                                                    });
                                        }
                                        dialog.dismiss();
                                    }

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
                }
            }
        });
    }
}