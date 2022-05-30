package com.example.gongguham_;

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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.FirebaseFirestore;

public class memberInitActivity extends AppCompatActivity {
    String phonenumber;
    private TextView phoneNumberTextE;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memeber_init);

        Intent intent = getIntent();
        phonenumber = intent.getStringExtra("phonenumber");
        phoneNumberTextE = findViewById(R.id.phoneNumberText);
        phoneNumberTextE.setText(phonenumber);


        if(user != null){
            for (UserInfo profile : user.getProviderData()) {
                String name = profile.getDisplayName();
                if (name != null) {
                    if (name.length() == 0) {
                    }else{
                        startMainActivity();
                    }
                }
            }
        }
        Spinner genderSpinner = (Spinner)findViewById(R.id.spinner_gender);
        ArrayAdapter genderAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);

        Spinner accountSpinner = (Spinner)findViewById(R.id.spinner_account);
        ArrayAdapter accountAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_account, android.R.layout.simple_spinner_item);
        accountAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountSpinner.setAdapter(accountAdapter);

        findViewById(R.id.checkButton).setOnClickListener(onClickListener);
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
        startMainActivity();
    }

    View.OnClickListener onClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.checkButton:
                    Log.e("클릭", "클릭");
                    profileUpdate();
                    break;
            }
        }
    };

    private void profileUpdate() {
        EditText nameE = (EditText) findViewById(R.id.nameEditText);
        String name = nameE.getText().toString();
        TextView phoneNumberE = (TextView) findViewById(R.id.phoneNumberText);
        phoneNumberE.setText(phonenumber);
        EditText accountE = (EditText) findViewById(R.id.accountEditText);
        String account = accountE.getText().toString();
        EditText birthdayE = (EditText) findViewById(R.id.birthdayEditText);
        String birthday = birthdayE.getText().toString();

        int point = 0;
        int ReviewTotalScore = 0;
        int ReviewAvScore = 0;
        int ReviewNumber = 0;

        Spinner genderS = (Spinner) findViewById(R.id.spinner_gender);
        String gender = genderS.getSelectedItem().toString();
        Spinner accountS = (Spinner) findViewById(R.id.spinner_account);
        String accountValue = accountS.getSelectedItem().toString();

        if (name.length() > 0  && gender.length() > 0 && accountValue.length() >0 && account.length()>0  && birthday.length() > 5) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            MemberInfo memberInfo = new MemberInfo(name, phonenumber,gender, accountValue, account, birthday, point, ReviewTotalScore, ReviewAvScore, ReviewNumber, "null");

            if (user != null) {
                db.collection("users").document(user.getEmail()).set(memberInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("회원정보 등록을 성공하였습니다.");
                                startMainActivity();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원정보 등록을 실패하였습니다.");
                            }
                        });
            }
        } else {
            startToast("회원정보를 입력해주세요");
        }
    }

    private  void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private  void startMainActivity(){
        Intent intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}