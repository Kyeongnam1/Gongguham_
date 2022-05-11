package com.example.gongguham_;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthenticationActivity extends AppCompatActivity {

    // variable for FirebaseAuth class
    private FirebaseAuth mAuth;
    private EditText user_phonenumber_edt, user_auth_number_edt;

    private Button phone_auth_btn, request_auth_num_btn;

    //private Button gotoLoginButton, gotoSignupButton;

    // string for storing our verification ID
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);

        // below line is for getting instance
        // of our FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();

        // initializing variables for button and Edittext.
        user_phonenumber_edt = findViewById(R.id.user_phonenumber_edt);
        user_auth_number_edt = findViewById(R.id.user_auth_number_edt);
        phone_auth_btn = findViewById(R.id.phone_auth_btn);
        request_auth_num_btn = findViewById(R.id.request_auth_num_btn);


        // 인증 번호 생성 절차
        request_auth_num_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(user_phonenumber_edt.getText().toString())) {
                    // 공백의 휴대폰 번호
                    Toast.makeText(PhoneAuthenticationActivity.this, "올바른 형식의 전화번호를 입력해주세요.\n(국가번호 + 휴대폰 번호)", Toast.LENGTH_SHORT).show();
                } else {
                    // 전화번호 형식시 인증번호 전송
                    String phone = user_phonenumber_edt.getText().toString();
                    sendVerificationCode(phone);
                }
            }
        });

        // 인증 확인 절차
        phone_auth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 인증번호란 공백시
                if (TextUtils.isEmpty(user_auth_number_edt.getText().toString())) {
                    Toast.makeText(PhoneAuthenticationActivity.this, "6자리의 인증번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                } else {
                    verifyCode(user_auth_number_edt.getText().toString());
                }
            }
        });
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 인증번호 일치
                            Intent intent = new Intent(PhoneAuthenticationActivity.this, SignUpActivity.class);
                            Toast.makeText(PhoneAuthenticationActivity.this, "인증번호가 일치합니다.", Toast.LENGTH_LONG).show();
                            intent.putExtra("phonenumber", user_phonenumber_edt.getText().toString());
                            startActivity(intent);
                            finish();
                        } else {
                            // 인증번호 불일치
                            Toast.makeText(PhoneAuthenticationActivity.this, "인증번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void sendVerificationCode(String number) {
        // 인증번호 전송
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)		 // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)				 // Activity (for callback binding)
                        .setCallbacks(mCallBack)		 // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // callback
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks

            // 객체생성
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        // 인증번호 전달시
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(PhoneAuthenticationActivity.this, "인증번호가 전송되었습니다.", Toast.LENGTH_LONG).show();
            // 코드 전송
            verificationId = s;
        }

        // 성공
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            // below line is used for getting OTP code
            // which is sent in phone auth credentials.
            final String code = phoneAuthCredential.getSmsCode();

            // checking if the code
            // is null or not.
            if (code != null) {
                // if the code is not null then
                // we are setting that code to
                // our OTP edittext field.
                user_auth_number_edt.setText(code);

                // after setting this code
                // to OTP edittext field we
                // are calling our verifycode method.
                verifyCode(code);
            }
        }

        // 실패
        @Override
        public void onVerificationFailed(FirebaseException e) {
            // displaying error message with firebase exception.
            Toast.makeText(PhoneAuthenticationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

    // below method is use to verify code from Firebase.
    private void verifyCode(String code) {
        // below line is used for getting getting
        // credentials from our verification id and code.
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // after getting credential we are
        // calling sign in method.
        signInWithCredential(credential);
        Toast.makeText(PhoneAuthenticationActivity.this, "인증번호 확인중..", Toast.LENGTH_SHORT).show();
    }
}
