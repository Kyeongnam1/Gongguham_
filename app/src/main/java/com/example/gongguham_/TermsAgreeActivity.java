package com.example.gongguham_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TermsAgreeActivity extends AppCompatActivity {
    CheckBox c1;
    CheckBox c2;
    CheckBox c3;
    CheckBox c4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_agree);

        findViewById(R.id.allCheckBtn).setOnClickListener(onClickListener);
        findViewById(R.id.phone).setOnClickListener(onClickListener);
        c1 = (CheckBox) findViewById(R.id.firstCheckBtn);
        c2 = (CheckBox) findViewById(R.id.secondCheckBtn);
        c3 = (CheckBox) findViewById(R.id.thirdCheckBtn);
        c4 = (CheckBox) findViewById(R.id.fourthCheckBtn);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.allCheckBtn:
                    c1.setChecked(true);
                    c2.setChecked(true);
                    c3.setChecked(true);
                    c4.setChecked(true);
                    break;
                case R.id.phone:
                    if(c1.isChecked() && c2.isChecked() && c3.isChecked()){
                        startPhoneAuthentication();
                    }else{
                        startToast("약관에 동의해주세요.");
                    }

                    break;
            }
        }
    };

    public void TermsDetail(View v){ startTermsDetail(); }

    private  void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //전화번호 인증
    private  void startPhoneAuthentication(){
        Intent intent = new Intent(this,PhoneAuthenticationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private  void startTermsDetail(){
        Intent intent = new Intent(this,TermsDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}