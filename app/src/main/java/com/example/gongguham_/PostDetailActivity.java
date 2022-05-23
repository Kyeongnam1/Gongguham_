package com.example.gongguham_;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PostDetailActivity extends AppCompatActivity {

    Button tmBtn;

    // 채팅방 관련 선언
    private DatabaseReference chatRef;
    private Button enterChat;
    private String chatTitle;
    String cr_pass = null, ck_pass = null;
    String name;
    String accountValue;
    String account;
    String username;
    int curPerson;
    UserInfo userInfo;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_detail);
        final TextView titleTextView = findViewById(R.id.post_detail_title);
        final TextView emailTextView = findViewById(R.id.post_detail_email);
        final TextView deliveryFeeTextView = findViewById(R.id.post_detail_delivery_fee);
        final TextView contentTextView = findViewById(R.id.post_detail_content);
        final TextView placeTextView = findViewById(R.id.post_detail_place);
        final TextView hourTextView = findViewById(R.id.post_detail_time_hour);
        final TextView minuteTextView = findViewById(R.id.post_detail_time_minute);
        final TextView maxPersonTextView = findViewById(R.id.post_detail_maxperson);
        final TextView curPersonTextView = findViewById(R.id.post_detail_curperson);
        tmBtn = findViewById(R.id.tmbtn);

        enterChat = findViewById(R.id.enterChat);


        Intent intent = getIntent();
        String key = intent.getStringExtra("KEY");
        // post 정보
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("posts").document(key);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            int fee = Integer.parseInt(document.getData().get("deliveryFee").toString()) / Integer.parseInt(document.getData().get("curPerson").toString());

                            titleTextView.setText(document.getData().get("postTitle").toString());
                            emailTextView.setText(document.getData().get("postEmail").toString());
                            deliveryFeeTextView.setText(Integer.toString(fee));
                            contentTextView.setText(document.getData().get("postContent").toString());
                            placeTextView.setText(document.getData().get("meetingArea").toString());
                            hourTextView.setText(document.getData().get("closeTime_hour").toString());
                            minuteTextView.setText(document.getData().get("closeTime_minute").toString());
                            maxPersonTextView.setText(document.getData().get("maxPerson").toString());
                            curPersonTextView.setText(document.getData().get("curPerson").toString());
                            if(maxPersonTextView.getText().equals(curPersonTextView.getText()))
                            {
                                tmBtn.setVisibility(View.INVISIBLE);
                            }

                            // 채팅방이름 찾기
                            chatTitle = document.getData().get("chatTitle").toString();
                            //마감시간일시 transmissionactivity 실행

                            String sCurTime_Hour = null;
                            String sCurTime_minute = null;
                            String pTime_Hour = null;
                            String pTime_minute = null;
                            int sCurTime;
                            int pTime;
                            sCurTime_Hour = new java.text.SimpleDateFormat("HH",java.util.Locale.KOREA).format(new java.util.Date());
                            sCurTime_minute = new java.text.SimpleDateFormat("mm",java.util.Locale.KOREA).format(new java.util.Date());
                            sCurTime = Integer.parseInt(sCurTime_Hour)*60+Integer.parseInt(sCurTime_minute);
                            pTime_Hour = hourTextView.getText().toString();
                            pTime_minute = minuteTextView.getText().toString();
                            pTime = Integer.parseInt(pTime_Hour)*60+Integer.parseInt(pTime_minute);
                            if(sCurTime>=pTime)
                            {
                                if(user.getEmail().toString().equals(document.getData().get("postEmail").toString()))
                                {
                                    Intent intent = new Intent(PostDetailActivity.this, TransmissionActivity.class);
                                    intent.putExtra("dbTitle", titleTextView.getText().toString()+contentTextView.getText().toString()+placeTextView.getText().toString());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Intent intent = new Intent(PostDetailActivity.this, TransmissionGuestActivity.class);
                                    intent.putExtra("dbTitle", titleTextView.getText().toString()+contentTextView.getText().toString()+placeTextView.getText().toString());
                                    startActivity(intent);
                                }


                            }
                        }

                    }
                }
            }

        });
        try {
            Thread.sleep(100); // 추가적인 딜레이(복잡한 로직이 추가된다고 가정)
        } catch (InterruptedException e) {
        }
        // user 정보(current)
        DocumentReference documentUserReference = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        documentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            username = document.getData().get("name").toString();
                            name = document.getData().get("name").toString();
                            account = document.getData().get("account").toString();
                            accountValue = document.getData().get("accountValue").toString();
                            curPerson = Integer.parseInt(curPersonTextView.getText().toString())+1;

                            userInfo = new UserInfo(name, accountValue, account, curPerson);
                        }
                    }
                }
            }
        });


        //신청하기 버튼 클릭시
        tmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                int curPerson = userInfo.getCurPerson();
                String name = "name"+Integer.toString(curPerson);
                String account = "account"+Integer.toString(curPerson);
                String accountValue = "accountValue"+Integer.toString(curPerson);

                if(curPerson<=Integer.parseInt(maxPersonTextView.getText().toString()))
                {
                    db.collection("posts").document(titleTextView.getText().toString()+contentTextView.getText().toString()+placeTextView.getText().toString())
                            .update(name, userInfo.getName(),account,userInfo.getAccount(),accountValue, userInfo.getAccountValue(),"curPerson",userInfo.getCurPerson(), userInfo.getName(), curPerson)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    menuReceive(titleTextView.getText().toString()+contentTextView.getText().toString()+placeTextView.getText().toString(), curPerson);
                                    Log.d("AddPost Activity", "DocumentSnapShot" + documentReference);
                                    //Toast.makeText(view.getContext(),"신청이 완료됐습니다.", Toast.LENGTH_SHORT).show();
                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("AddPost Activity", "Error adding post" + e);

                                }
                            });
                }
                else
                {
                    Toast.makeText(view.getContext(),"인원이 다 찼습니다.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        enterChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPassword();
            }
        });



    }
    //배달메뉴 이름, 가격 받아서 디비에 넣기
    private void menuReceive(String key, int curPerson){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        View dialogView = getLayoutInflater().inflate(R.layout.food_send, null);
        EditText food_send_name = (EditText) dialogView.findViewById(R.id.food_send_name);
        EditText food_send_price = (EditText) dialogView.findViewById(R.id.food_send_price);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String foodName = "foodName"+Integer.toString(curPerson);
        String foodPrice = "foodPrice"+Integer.toString(curPerson);
        builder.setView(dialogView);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.collection("posts").document(key)
                        .update(foodName, food_send_name.getText().toString(), foodPrice, food_send_price.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                showPw(key);
                                Log.d("PostDetailActivity", "Success");
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("PostDetailActivity", "push menu in db fail" + e);
                            }
                        });

            }
        });
        builder.show();
    }
    private void showPw(String key){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        View dialogView = getLayoutInflater().inflate(R.layout.pw_show, null);
        TextView id = (TextView) dialogView.findViewById(R.id.chat_id_show_text);
        TextView pw = (TextView) dialogView.findViewById(R.id.chat_pw_show_text);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        db.collection("posts").document(key)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot document = task.getResult();
                            if (document != null)
                            {
                                if (document.exists())
                                {
                                    id.setText(document.getData().get("chatTitle").toString());
                                    pw.setText(document.getData().get("chatPass").toString());
                                }
                            }
                        }
                    }
                });
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(PostDetailActivity.this,"신청이 성공적으로 완료되었습니다.",Toast.LENGTH_SHORT).show();
                refresh();
            }
        });
        builder.show();
    }

    private void checkPassword(){
        View dialogView = getLayoutInflater().inflate(R.layout.chat_password_check, null);
        TextView chat_name_ck = (TextView) dialogView.findViewById(R.id.chat_name_ck);
        EditText check_password = (EditText) dialogView.findViewById(R.id.check_password);
        chat_name_ck.setText(chatTitle);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ck_pass = check_password.getText().toString();
                            // 채팅방 비밀번호 가져오기
                            chatRef = firebaseDatabase.getReference("chat");
                            chatRef.child(chatTitle).addChildEventListener(new ChildEventListener() {
                                //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                    ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
                                    cr_pass = chatDTO.getPassword();

                                }

                                @Override
                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            if(ck_pass.equals(cr_pass)){
                                Toast.makeText(PostDetailActivity.this,"채팅방에 입장하였습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PostDetailActivity.this, ChatChattingActivity.class);
                                intent.putExtra("chatName", chatTitle);
                                intent.putExtra("userName", username);
                                intent.putExtra("password",ck_pass);
                                G.username = username;
                                startActivity(intent);
                                ck_pass = null;
                            }else{
                                Toast.makeText(PostDetailActivity.this,"비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
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

        private void refresh() //새로고침
        {
            finish();//인텐트 종료
            overridePendingTransition(0, 0);//인텐트 효과 없애기
            Intent intent = getIntent(); //인텐트
            startActivity(intent); //액티비티 열기
            overridePendingTransition(0, 0);//인텐트 효과 없애기
        }





}