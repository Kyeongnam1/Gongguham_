package com.example.gongguham_;

import static android.content.Context.MODE_PRIVATE;
import static com.example.gongguham_.R.*;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.Objects;


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

    // 클릭시 name null방지를 위한 firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        View view = inflater.inflate(layout.fragment_chat_starter, container, false);

        // shared preferences 테스트
        SharedPreferences preferences = this.getActivity().getSharedPreferences("account", MODE_PRIVATE);
        loadData();


        chat_name = (EditText) view.findViewById(id.chat_name);

        // 둘다 user_name
        TextView name = view.findViewById(id.user_name);
        user_name = view.findViewById(id.user_name);

        user_next = (Button) view.findViewById(id.user_next);
        boom_button = (Button) view.findViewById(id.boom_button);

        chat_list = (ListView) view.findViewById(id.chat_list);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            name.setText(document.getData().get("name").toString());
                            user_name.setText(document.getData().get("name").toString());
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
                    // sharedpreferences 테스트
                    saveData();
                    startActivity(intent);
                }
            }
        });
        boom_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().equals("") || chat_name.getText().toString().equals("")){
                    Toast.makeText(getContext(),"채팅방 이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else{ //방폭
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
        // shared preferences 테스트
        SharedPreferences preferences = this.getActivity().getSharedPreferences("account", MODE_PRIVATE);
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
                Toast.makeText(getContext(),"채팅방에 입장하였습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ChatChattingActivity.class);
                intent.putExtra("chatName", chat_name.getText().toString());
                intent.putExtra("userName", user_name.getText().toString());

                SharedPreferences.Editor editor = preferences.edit();
                G.username = user_name.getText().toString();
                editor.putString("username",G.username);
                editor.commit();

                startActivity(intent);
            }
        });


    }
    private void boomRoom(){

    }
    private void saveData(){

        SharedPreferences preferences= this.getActivity().getSharedPreferences("account",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();

        editor.putString("username",G.username);
        editor.commit();
    }
    private void loadData(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences("account",MODE_PRIVATE);
        G.username=preferences.getString("username", null);
    }


}