package com.example.gongguham_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private  static final String TAG = "SignUpActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.SignUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoLoginButton).setOnClickListener(onClickListener);
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
                case R.id.gotoLoginButton:
                    startLoginActivity();
                    break;
            }

        }
    };

    private void signUp() {
        EditText emailE = (EditText) findViewById(R.id.emailEditText);
        String email = emailE.getText().toString();
        EditText passwordE = (EditText) findViewById(R.id.passwordEditText);
        String password = passwordE.getText().toString();
        EditText passwordCheckE = (EditText) findViewById(R.id.passwordCheckEditText);
        String passwordCheck = passwordCheckE.getText().toString();

        if (email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            if (password.equals(passwordCheck)) {
                Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this,
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            startToast("회원가입을 성공적으로 마쳤습니다.");
                                            FirebaseUser user = mAuth.getCurrentUser();
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
    private  void startLoginActivity(){
        Intent intent=new Intent(this,loginActivity.class);
        startActivity(intent);
    }
}