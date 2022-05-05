package com.example.gongguham_;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class ChatChattingActivity extends AppCompatActivity {

    EditText chat_edit;
    ListView chat_view;

    ArrayList<ChatDTO> chatDTOS = new ArrayList<>();
    ChatAdapter adapter;

    // starter 에서 get위해(intent)
    private String CHAT_NAME;
    private String USER_NAME;

    //Firebase Database 관리 객체참조변수
    FirebaseDatabase firebaseDatabase;
    //'chat'노드의 참조객체 참조변수
    DatabaseReference chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_chatting);



        chat_edit =findViewById(R.id.chat_edit);
        chat_view =findViewById(R.id.chat_view);
        adapter=new ChatAdapter(chatDTOS,getLayoutInflater());
        chat_view.setAdapter(adapter);

        //Firebase DB관리 객체와 'caht'노드 참조객체 얻어오기
        firebaseDatabase= FirebaseDatabase.getInstance();
        chatRef= firebaseDatabase.getReference("chat"); //

        Intent intent = getIntent();
        CHAT_NAME = intent.getStringExtra("chatName");
        USER_NAME = intent.getStringExtra("userName");


        //firebaseDB에서 채팅 메세지들 실시간 읽어오기..
        //'chat'노드에 저장되어 있는 데이터들을 읽어오기
        //chatRef에 데이터가 변경되는 것으 듣는 리스너 추가
        chatRef.child(CHAT_NAME).addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //새로 추가된 데이터(값 : MessageItem객체) 가져오기
                ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);

                //새로운 메세지를 리스뷰에 추가하기 위해 ArrayList에 추가
                chatDTOS.add(chatDTO);

                adapter.notifyDataSetChanged();
                chat_view.setSelection(chatDTOS.size()-1);
            }

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {
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

    }

    public void clickSend(View view) {

        String username = G.username;
        // firebase DB에 저장할 값들( 닉네임, 메세지, 시간 )
        String message= chat_edit.getText().toString();

        // 공백의 메시지 보내지 못하게
        if(message.equals("")){
            Toast.makeText(this,"메세지를 입력해 주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        //메세지 작성 시간 문자열로..
        Calendar calendar= Calendar.getInstance(); //현재 시간을 가지고 있는 객체
        String time=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE); // hour : minute

        //firebase DB에 저장할 값(MessageItem객체) 설정
        ChatDTO chat = new ChatDTO(username, chat_edit.getText().toString(), time); //ChatDTO를 이용하여 데이터를 묶는다.
        chatRef.child(CHAT_NAME).push().setValue(chat); // 데이터 푸쉬
        chat_edit.setText("");

        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

    }
}