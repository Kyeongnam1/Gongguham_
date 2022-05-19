package com.example.gongguham_;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
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
    String username;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        final TextView titleTextView = findViewById(R.id.post_detail_title);
        final TextView emailTextView = findViewById(R.id.post_detail_email);
        final TextView contentTextView = findViewById(R.id.post_detail_content);
        final TextView placeTextView = findViewById(R.id.post_detail_place);
        final TextView hourTextView = findViewById(R.id.post_detail_time_hour);
        final TextView minuteTextView = findViewById(R.id.post_detail_time_minute);
        final TextView maxPersonTextView = findViewById(R.id.post_detail_maxperson);
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
                            titleTextView.setText(document.getData().get("postTitle").toString());
                            emailTextView.setText(document.getData().get("postEmail").toString());
                            contentTextView.setText(document.getData().get("postContent").toString());
                            placeTextView.setText(document.getData().get("meetingArea").toString());
                            hourTextView.setText(document.getData().get("closeTime_hour").toString());
                            minuteTextView.setText(document.getData().get("closeTime_minute").toString());
                            maxPersonTextView.setText(document.getData().get("maxPerson").toString());

                            // 채팅방이름 찾기
                            chatTitle = document.getData().get("chatTitle").toString();
                        }
                    }
                }
            }
        });
        // user 정보(current)
        DocumentReference documentUserReference = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        documentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            username = document.getData().get("name").toString();
                        }
                    }
                }
            }
        });



        tmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        enterChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPassword();
            }
        });



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


}