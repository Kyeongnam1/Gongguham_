package com.example.gongguham_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

// SHA1: DD:FF:8D:F8:C7:0C:7B:01:8E:49:3C:48:B8:8B:B4:6E:8A:E2:95:05


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView phoneNumberTextE;

    private Button SignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String phonenumber = intent.getStringExtra("phonenumber");

        setContentView(R.layout.activity_sign_up);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.SignUpButton).setOnClickListener(onClickListener);
       // findViewById(R.id.phoneSignUpButton).setOnClickListener(onClickListener);

        phoneNumberTextE = findViewById(R.id.phoneNumberText);
        phoneNumberTextE.setText(phonenumber);

        SignUpButton = findViewById(R.id.SignUpButton);
      //  phoneSignUpButton = findViewById(R.id.phoneSignUpButton);
/*
        if (phonenumber == null){
            SignUpButton.setVisibility(View.GONE);
            phoneSignUpButton.setVisibility(View.VISIBLE);
        }else{
            SignUpButton.setVisibility(View.VISIBLE);
            phoneSignUpButton.setVisibility(View.GONE);
        }

 */
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }

    View.OnClickListener onClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.SignUpButton:
                    Log.e("클릭", "클릭");
                    signUp();
                    break;
            }

        }
    };

    private void signUp() {
        EditText emailE = (EditText) findViewById(R.id.emailEditText);
        String email = emailE.getText().toString(); //이메일
        EditText passwordE = (EditText) findViewById(R.id.passwordEditText);
        String password = passwordE.getText().toString(); //비밀번호
        EditText passwordCheckE = (EditText) findViewById(R.id.passwordCheckEditText);
        String passwordCheck = passwordCheckE.getText().toString(); //비밀번호 체크

        // 원본
        if (email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            if (password.equals(passwordCheck)) {
                Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this,
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            startToast("회원가입을 성공적으로 마쳤습니다.");
                                            startmemberInitActivity();
                                        } else {
                                            if (task.getException() != null) {
                                                // If sign in fails, display a message to the user.
                                                startToast(task.getException().toString());
                                            }
                                        }
                                    }
                                });
            } else {
                startToast("비밀번호가 일치하지 않습니다.");
            }
        } else {
            startToast("이메일 또는 비밀번호를 입력해주세요.");
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

    private  void startmemberInitActivity(){
        Intent intent=new Intent(this,memberInitActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("phonenumber",phoneNumberTextE.getText().toString());
        startActivity(intent);
    }

}