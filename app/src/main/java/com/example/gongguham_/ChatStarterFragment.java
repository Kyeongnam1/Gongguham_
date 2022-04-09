package com.example.gongguham_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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



public class ChatStarterFragment extends Fragment {
    private FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static final String TAG = "ChartStartFragment";

    private EditText chat_name;
    private TextView user_name;
    private Button user_next;
    private Button boom_button;
    private ListView chat_list;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();



    public ChatStarterFragment() {
        // Required empty public constructor
    }



    public static ChatStarterFragment newInstance(String param1, String param2) {
        ChatStarterFragment fragment = new ChatStarterFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_starter, container, false);

        chat_name = (EditText) view.findViewById(R.id.chat_name);

        final TextView name = view.findViewById(R.id.user_name);

        user_next = (Button) view.findViewById(R.id.user_next);
        boom_button = (Button) view.findViewById(R.id.boom_button);

        chat_list = (ListView) view.findViewById(R.id.chat_list);


        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            name.setText(document.getData().get("name").toString());
                            //name.setText(MemberInfo.class.getName());
                        } else {
                            Log.e(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        user_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 공백 입력시
                if(name.getText().toString().equals("") || chat_name.getText().toString().equals("")){
                    Toast.makeText(getContext(),"채팅방 이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                } else{ //방 입장
                    Toast.makeText(getContext(),"채팅방에 입장하였습니다.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), ChatChattingActivity.class);
                    intent.putExtra("chatName", chat_name.getText().toString());
                    intent.putExtra("userName", name.getText().toString());
                    startActivity(intent);
                }
            }
        });
        boom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals("") || chat_name.getText().toString().equals("")){
                    Toast.makeText(getContext(),"채팅방 이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else{ //방폭ㅅ
                    Toast.makeText(getContext(), "채팅방이 터졌습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        showChatList();

        return view;
    }


    private void showChatList() {
        //final String[] clicked_chat_room = new String[1];
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter

                = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_list.setAdapter(adapter);
        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
                adapter.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
                adapter.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

        chat_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chat_name.setText(adapter.getItem(i));
                Intent intent = new Intent(getContext(), ChatChattingActivity.class);
                intent.putExtra("chatName", chat_name.getText().toString());
                startActivity(intent);
            }
        });
    }
    private void boomRoom(){

    }


}