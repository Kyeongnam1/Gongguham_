package com.example.gongguham_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class passwordChangeActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.passwordChangeButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.passwordChangeButton:
                    Log.e("클릭", "클릭");
                    change();
                    break;
            }
        }
    };

    private void change() {
        EditText change = (EditText) findViewById(R.id.passwordChangeEditText);
        EditText change2 = (EditText) findViewById(R.id.passwordChangeEditText2);

        String newPassword = change.getText().toString();
        String newPassword2 = change2.getText().toString();

        if(!newPassword.equals(newPassword2)) {
            startToast("비밀번호가 다릅니다.");
        }else{
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startToast("비밀번호를 변경하였습니다.");
                                FirebaseAuth.getInstance().signOut();
                                startMainActivity();
                                startToast("다시 로그인해주세요.");
                            }
                        }
                    });
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