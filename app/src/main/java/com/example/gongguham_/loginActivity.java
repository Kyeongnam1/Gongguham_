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

public class loginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private  static final String TAG = "loginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.loginButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoSignUpButton).setOnClickListener(onClickListener);
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
                case R.id.loginButton:
                    Log.e("클릭", "클릭");
                    login();
                    break;
                case R.id.gotoSignUpButton:
                    Log.e("클릭", "클릭");
                    startSignUpActivity();
                    break;
            }
        }
    };

    private void login() {
        EditText emailE = (EditText) findViewById(R.id.emailEditText);
        String email = emailE.getText().toString();
        EditText passwordE = (EditText) findViewById(R.id.passwordEditText);
        String password = passwordE.getText().toString();

        if (email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                startToast("로그인을 성공적으로 마쳤습니다.");
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
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    private  void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private  void startSignUpActivity(){
        Intent intent=new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }
}