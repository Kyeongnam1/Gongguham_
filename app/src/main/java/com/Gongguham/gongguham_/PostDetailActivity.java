package com.Gongguham.gongguham_;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

import java.util.Calendar;

public class PostDetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Button tmBtn, deleteBtn;

    // 채팅방 관련 선언
    private DatabaseReference chatRef;
    private ImageButton back, enterChat;
    SwipeRefreshLayout swipeRefreshLayout;
    private String chatTitle;
    String cr_pass = null, ck_pass = null;
    String name;
    String accountValue;
    String account;
    String username;
    String doc;
    int curPerson;
    UserInfo userInfo;
    String user_Name;
    String pwdDB;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    // 추가
    private String closeTime_hour, closeTime_minute;

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
        deleteBtn = findViewById(R.id.postDeleteButton);

        enterChat = findViewById(R.id.enterChat);
        back = findViewById(R.id.backButton);

        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        Log.i("layout check", String.valueOf(swipeRefreshLayout));
        swipeRefreshLayout.setOnRefreshListener(this);


        Intent intent = getIntent();
        String key = intent.getStringExtra("KEY");
        pwdDB = key;

      // 푸쉬알림 선언

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

                            // 추가
                            // 푸쉬 알림 위해 추가
                            closeTime_hour = document.getData().get("closeTime_hour").toString();
                            closeTime_minute = document.getData().get("closeTime_minute").toString();
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
                            String state = document.getData().get("curSituation").toString();

                            if(sCurTime>=pTime)
                            {
                                if(state.equals("공구완료"))
                                {
                                    contentTextView.setText("공동구매가 완료된 글입니다.");
                                    enterChat.setVisibility(View.INVISIBLE);
                                    Toast.makeText(PostDetailActivity.this,"공동구매가 완료된 글입니다.",Toast.LENGTH_SHORT).show();
                                }
                                else if(state.equals("계좌중계페이지")) //계좌중계페이지까지 진행됐을경우
                                {
                                    if(user.getEmail().toString().equals(document.getData().get("postEmail").toString()))
                                    {
                                        Intent intent = new Intent(PostDetailActivity.this, TransmissionActivity.class);
                                        intent.putExtra("dbTitle", titleTextView.getText().toString()+contentTextView.getText().toString()+placeTextView.getText().toString());
                                        startActivity(intent);
                                    }
                                    else if(check(document))
                                    {
                                        Intent intent = new Intent(PostDetailActivity.this, TransmissionGuestActivity.class);
                                        intent.putExtra("dbTitle", titleTextView.getText().toString()+contentTextView.getText().toString()+placeTextView.getText().toString());
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        tmBtn.setVisibility(View.INVISIBLE);
                                        Toast.makeText(PostDetailActivity.this,"신청이 마감된 글입니다.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else if(state.equals("배달진행상황페이지") || state.equals("주문접수")|| state.equals("배달시작") || state.equals("배달완료")) //배달진행상황페이지까지 진행된 경우
                                {
                                    if(user.getEmail().toString().equals(document.getData().get("postEmail").toString()))
                                    {
                                        Intent intent = new Intent(PostDetailActivity.this, DeliveryProgressActivity.class);
                                        intent.putExtra("dbTitle", key);
                                        intent.putExtra("role", "글쓴이");
                                        startActivity(intent);
                                    }
                                    else if(check(document))
                                    {
                                        Intent intent = new Intent(PostDetailActivity.this, DeliveryProgressActivity.class);
                                        intent.putExtra("dbTitle", key);
                                        intent.putExtra("role", "참여자");
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        tmBtn.setVisibility(View.INVISIBLE);
                                        Toast.makeText(PostDetailActivity.this,"신청이 마감된 글입니다.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    if(user.getEmail().toString().equals(document.getData().get("postEmail").toString()))
                                    {
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("posts").document(key)
                                                .update("curSituation", "계좌중계페이지")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("pda", "pda");
                                                        //Toast.makeText(view.getContext(),"신청이 완료됐습니다.", Toast.LENGTH_SHORT).show();
                                                    }

                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("pda", "Error update curSituation" + e);

                                                    }
                                                });
                                        Intent intent = new Intent(PostDetailActivity.this, TransmissionActivity.class);
                                        intent.putExtra("dbTitle", titleTextView.getText().toString()+contentTextView.getText().toString()+placeTextView.getText().toString());
                                        startActivity(intent);
                                    }
                                    else if(check(document))
                                    {
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        db.collection("posts").document(key)
                                                .update("curSituation", "계좌중계페이지")
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("pda", "pda");
                                                        //Toast.makeText(view.getContext(),"신청이 완료됐습니다.", Toast.LENGTH_SHORT).show();
                                                    }

                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e("pda", "Error update curSituation" + e);

                                                    }
                                                });
                                        Intent intent = new Intent(PostDetailActivity.this, TransmissionGuestActivity.class);
                                        intent.putExtra("dbTitle", titleTextView.getText().toString()+contentTextView.getText().toString()+placeTextView.getText().toString());
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        tmBtn.setVisibility(View.INVISIBLE);
                                        Toast.makeText(PostDetailActivity.this,"신청이 마감된 글입니다.",Toast.LENGTH_SHORT).show();
                                    }
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

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // deleteVerification();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                DocumentReference documentUserReference = FirebaseFirestore.getInstance().collection("posts").document(key);
                documentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                if (document.exists()) {
                                    if(user.getEmail().equals(document.getData().get("email1").toString())){
                                        FirebaseFirestore.getInstance().collection("posts").document(key)
                                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                startMainActivity();
                                                Toast.makeText(view.getContext(),"게시글을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                                                chatRef = firebaseDatabase.getReference("chat");
                                                chatRef.child(chatTitle).removeValue();
                                                for(int i=1;i<=Integer.parseInt(document.getData().get("curPerson").toString());i++)
                                                {
                                                    String userEmail = document.getData().get("email"+Integer.toString(i)).toString();
                                                    DocumentReference documentUserReference1 = FirebaseFirestore.getInstance().collection("users").document(userEmail);
                                                    documentUserReference1.update("curPost", "null").
                                                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d("pdd", "pdd");
                                                                }

                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.e("pdd", "Error update curPost" + e);

                                                                }
                                                            });
                                                }
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });
                                    }else{
                                        Toast.makeText(view.getContext(),"글 게시자가 아닙니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    }
                });



            }
        });

        //신청하기 버튼 클릭시
        tmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                int curPerson = userInfo.getCurPerson();
                String email = "email"+Integer.toString(curPerson);
                String account = "account"+Integer.toString(curPerson);
                String accountValue = "accountValue"+Integer.toString(curPerson);

                if(curPerson<=Integer.parseInt(maxPersonTextView.getText().toString()))
                {
                    doc = titleTextView.getText().toString()+contentTextView.getText().toString()+placeTextView.getText().toString();
                    db.collection("posts").document(doc)
                            .update(email, user.getEmail(),account,userInfo.getAccount(),accountValue, userInfo.getAccountValue(),"curPerson",userInfo.getCurPerson(), userInfo.getName(), curPerson, curPerson+"tmCheck", "false")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    menuReceive(titleTextView.getText().toString()+contentTextView.getText().toString()+placeTextView.getText().toString(), curPerson);
                                    Log.d("AddPost Activity", "DocumentSnapShot" + documentReference);
                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("AddPost Activity", "Error adding post" + e);

                                }
                            });
                    // 알림 계산하고 알람 설정
                    Intent intent = new Intent(PostDetailActivity.this,ReminderBroadcast.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(PostDetailActivity.this,0,intent,PendingIntent.FLAG_IMMUTABLE);

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    // 1밀리초 -> 1초 = 1000 * 1밀초 -> 1분 = 60 * 1초 -> 1시간 = 60 * 1분
                    // 마감시간 밀리초로 closeTime_hour, closeTime_minute 사용
                    Calendar calendar= Calendar.getInstance();

                    long leftsecondmillis = Long.parseLong(closeTime_hour) * 60 * 60 * 1000 +
                            Long.parseLong(closeTime_minute) * 60 * 1000 - (calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 * 1000 +
                            calendar.get(Calendar.MINUTE) * 60 * 1000);

                    // 1초 = 1000 * 10

                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + leftsecondmillis ,pendingIntent);
                    DocumentReference documentUserReference = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
                    documentUserReference.update("curPost", key).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("hf", "pda");
                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("hf", "Error update curPost" + e);

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
    //이름 가져오는 함수
    private void get_user_name(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentUserReference = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        documentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            user_Name = document.getData().get("name").toString();
                        }
                    }
                }
            }
        });
    }
    //이름 바탕으로 신청여부 검사하는 함수

    private boolean check(DocumentSnapshot document){
        int curNum = Integer.parseInt(document.getData().get("curPerson").toString());
        for(int i=2;i<=curNum;i++)
        {
            String emailTemp = "email"+Integer.toString(i);
            String temp = document.getData().get(emailTemp).toString();

            if(temp.equals(user.getEmail().toString())) {
                return true;
            }
            else{
                continue;
            }

        }
        return false;

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


    @Override
    public void onRefresh() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 500);

    }

    private  void startMainActivity(){
        Intent intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}