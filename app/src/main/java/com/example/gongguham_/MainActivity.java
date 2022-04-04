package com.example.gongguham_;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(user == null) {
            startLoginActivity();
        }else{
            for (UserInfo profile : user.getProviderData()) {
                String name = profile.getDisplayName();
                if (name != null) {
                    if (name.length() == 0) {
                        startmemberInitActivity();
                    }
                }
            }
        }
        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
        findViewById(R.id.chatButton).setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    startLoginActivity();
                    break;
                case R.id.chatButton:
                    startChat();
                    break;
            }
        }
    };

    private  void startLoginActivity(){
        Intent intent=new Intent(this,loginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private  void startmemberInitActivity(){
        Intent intent=new Intent(this,memberInitActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private  void startChat(){
        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}