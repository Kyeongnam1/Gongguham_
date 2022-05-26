package com.example.gongguham_;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class AddPostItem extends AppCompatActivity {

    private static final String TAG = "AddPostItemActivity";
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Button btnClose;
    private EditText postTitle,postContent, postMeetingArea, postCloseTime, postMaxPerson, deliveryFee;
    private static String userEmail;
    private DocumentReference mDatabase;
    private static String userLocation;

    // 추가
    private String closeTime_hour, closeTime_minute;




    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference chatRef;

    // 채팅방 명 입력창 추가
    private EditText post_chatCreate;
    private EditText post_chatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post_item);

        Spinner categorySpinner = (Spinner)findViewById(R.id.spinner_add_post_category);
        ArrayAdapter categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_category, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

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

        // 푸쉬알림 선언
        createNotificationChannel();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost();
                // 채팅방 자동 생성
                chatCreate();

                // 알림 계산하고 알람 설정
                Intent intent = new Intent(AddPostItem.this,ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddPostItem.this,0,intent,0);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                //long timeAtButtonClick = System.currentTimeMillis();
                // 1밀리초 -> 1초 = 1000 * 1밀초 -> 1분 = 60 * 1초 -> 1시간 = 60 * 1분
                // 마감시간 밀리초로 closeTime_hour, closeTime_minute 사용
                Calendar calendar= Calendar.getInstance();

                long leftsecondmillis = Long.parseLong(closeTime_hour) * 60 * 60 * 1000 +
                        Long.parseLong(closeTime_minute) * 60 * 1000 - (calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000 +
                        calendar.get(Calendar.MINUTE) * 60 * 1000);
                Toast.makeText(getApplicationContext(),"남시"+leftsecondmillis,Toast.LENGTH_SHORT).show();

                // 1초 = 1000 * 10

                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + leftsecondmillis ,pendingIntent);

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
        deliveryFee = (EditText) findViewById(R.id.deliveryFeeEditText);
        String s_fee = deliveryFee.getText().toString();
        int fee = Integer.parseInt(s_fee);

        Spinner categoryS = (Spinner) findViewById(R.id.spinner_add_post_category);
        String category = categoryS.getSelectedItem().toString();

        Spinner hourS = (Spinner) findViewById(R.id.spinner_add_post_close_time_hour);
        closeTime_hour = hourS.getSelectedItem().toString();

        Spinner minuteS = (Spinner) findViewById(R.id.spinner_add_post_close_time_minute);
        closeTime_minute = minuteS.getSelectedItem().toString();

        Spinner personS = (Spinner) findViewById(R.id.spinner_add_post_max_person);
        int maxPerson = Integer.parseInt(personS.getSelectedItem().toString());
        int curPerson = 1;
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
                                PostInfo postInfo = new PostInfo(title, category, content, meetingArea, closeTime_hour, closeTime_minute, maxPerson, fee, userLocation, chatCreate, userEmail, curPerson);
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
        DocumentReference documentUserReference = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        documentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            int curPerson=1;
                            String name = document.getData().get("name").toString();
                            String account = document.getData().get("account").toString();
                            String accountValue = document.getData().get("accountValue").toString();
                            String name1 = "name"+Integer.toString(curPerson);
                            String account1 = "account"+Integer.toString(curPerson);
                            String accountValue1 = "accountValue"+Integer.toString(curPerson);
                            UserInfo userInfo = new UserInfo(name, accountValue, account, curPerson);
                            db.collection("posts").document(postInfo.getPostTitle()+postInfo.getPostContent()+postInfo.getMeetingArea())
                                    .update("email1", user.getEmail(),account1,userInfo.getAccount(),accountValue1, userInfo.getAccountValue(),"curPerson",userInfo.getCurPerson(),"curSituation", "모집중", "chatPass", post_chatPassword.getText().toString())
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
                    }
                }
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

    // 푸쉬 알림 채널 설정
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "LemubitReminderChannel";
            String description = "Channel for Lemubit Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyLemubit",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}