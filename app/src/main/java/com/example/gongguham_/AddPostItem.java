package com.example.gongguham_;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AddPostItem extends AppCompatActivity {

    private static final String TAG = "AddPostItemActivity";
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Button btnClose;
    private EditText postTitle,postContent, postMeetingArea, postCloseTime, postMaxPerson;
    private static String userEmail;
    private DocumentReference mDatabase;
    private static String userLocation;


    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference chatRef;

    // 채팅방 명 입력창 추가
    private EditText post_chatCreate;
    private EditText post_chatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_item);

        Spinner hourSpinner = (Spinner)findViewById(R.id.spinner_add_post_close_time_hour);
        ArrayAdapter hourAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_hour, android.R.layout.simple_spinner_item);
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hourSpinner.setAdapter(hourAdapter);

        Spinner minuteSpinner = (Spinner)findViewById(R.id.spinner_add_post_close_time_minute);
        ArrayAdapter minuteAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_minute, android.R.layout.simple_spinner_item);
        minuteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minuteSpinner.setAdapter(minuteAdapter);

        Spinner personSpinner = (Spinner)findViewById(R.id.spinner_add_post_max_person);
        ArrayAdapter personAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_person, android.R.layout.simple_spinner_item);
        personAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personSpinner.setAdapter(personAdapter);

        btnClose = (Button) findViewById(R.id.btn_close);

        // add_post_chatCreate 등록
        post_chatCreate = (EditText) findViewById(R.id.add_post_chatCreate);
        post_chatPassword = (EditText) findViewById(R.id.add_post_chatPassword);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost();
                // 채팅방 자동 생성
                chatCreate();
                startMainActivity();
            }
        });
    }


    private void addPost(){
        postTitle = (EditText) findViewById(R.id.add_post_title);
        String title = postTitle.getText().toString();
        postContent = (EditText) findViewById(R.id.add_post_content);
        String content = postContent.getText().toString();
        postMeetingArea = (EditText) findViewById(R.id.add_post_meeting_area);
        String meetingArea = postMeetingArea.getText().toString();
        // postCloseTime = (EditText) findViewById(R.id.add_post_close_time);
        // String closeTime = postCloseTime.getText().toString();
        //  postMaxPerson = (EditText) findViewById(R.id.add_post_max_person);
        //    int maxPerson = Integer.parseInt(postMaxPerson.getText().toString());

        Spinner hourS = (Spinner) findViewById(R.id.spinner_add_post_close_time_hour);
        String closeTime_hour = hourS.getSelectedItem().toString();

        Spinner minuteS = (Spinner) findViewById(R.id.spinner_add_post_close_time_minute);
        String closeTime_minute = minuteS.getSelectedItem().toString();

        Spinner personS = (Spinner) findViewById(R.id.spinner_add_post_max_person);
        int maxPerson = Integer.parseInt(personS.getSelectedItem().toString());

        // 데이터에 입력위한 chatTitle
        String chatCreate = post_chatCreate.getText().toString();

//        userEmail 얻어오기

        userEmail = user.getEmail();

        mDatabase = FirebaseFirestore.getInstance().collection("locations").document(userEmail);
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.e(TAG, "DocumentSnapshot data: " + document.getData());

                            userLocation = document.getData().get("curLoc").toString();
                            if(title.length()>0 && content.length()>0 && meetingArea.length()>0 && closeTime_hour.length()>0 && closeTime_minute.length()>0 && chatCreate.length()>0){
                                PostInfo postInfo = new PostInfo(title, content, meetingArea, closeTime_hour, closeTime_minute, maxPerson, userLocation, chatCreate);
                                uploader(postInfo);
                            }
                            Log.i("TAG", userLocation);

                        } else {
                            Log.e(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });





    }

    private void uploader(PostInfo postInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        db.collection("posts").document(postInfo.getPostTitle()+postInfo.getPostContent()+postInfo.getMeetingArea()).set(postInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("AddPost Activity", "DocumentSnapShot" + documentReference);
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("AddPost Activity", "Error adding post" + e);

                    }
                });
    }


    private  void startMainActivity(){
        Intent intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

//    private void getUserLocation(){
//    }

    // 채팅방 추가 테스트
    private void chatCreate(){
        chatRef = firebaseDatabase.getReference("chat");

        // 채팅방 이름, 채팅방 비밀번호, 사용자이름(초기 이름으로 default로 채팅 봇, 시간
        String chatName = post_chatCreate.getText().toString();
        String chatPass = post_chatPassword.getText().toString();
        String userName = "Welcome";
        Calendar calendar= Calendar.getInstance(); //현재 시간을 가지고 있는 객체
        String time=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);

        ArrayList<ChatDTO> chatDTOS = new ArrayList<>();
        ChatAdapter adapter;

        ChatDTO chat = new ChatDTO(userName, "Welcome to Gongguham", time, chatPass); //ChatDTO를 이용하여 데이터를 묶는다.
        chatRef.child(chatName).push().setValue(chat); // 데이터 푸쉬


    }

}