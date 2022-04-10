package com.example.gongguham_;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class memberInitActivity extends AppCompatActivity {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memeber_init);
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
        EditText phoneNumberE = (EditText) findViewById(R.id.phoneNumberEditText);
        String phoneNumber = phoneNumberE.getText().toString();
        EditText accountE = (EditText) findViewById(R.id.accountEditText);
        String account = accountE.getText().toString();
        EditText birthdayE = (EditText) findViewById(R.id.birthdayEditText);
        String birthday = birthdayE.getText().toString();
        EditText addressE = (EditText) findViewById(R.id.addressEditText);
        String address = addressE.getText().toString();

        Spinner genderS = (Spinner) findViewById(R.id.spinner_gender);
        String gender = genderS.getSelectedItem().toString();
        Spinner accountS = (Spinner) findViewById(R.id.spinner_account);
        String accountValue = accountS.getSelectedItem().toString();

        if (name.length() > 0 && phoneNumber.length() > 9 && gender.length() > 0 && accountValue.length() >0 && account.length()>0  && birthday.length() > 5 && address.length()>0) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            MemberInfo memberInfo = new MemberInfo(name, phoneNumber,gender, accountValue, account, birthday, address);

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