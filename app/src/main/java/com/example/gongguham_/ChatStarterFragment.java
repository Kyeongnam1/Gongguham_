package com.example.gongguham_;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;


public class ChatStarterFragment extends Fragment {
    private FirebaseAuth mAuth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private EditText chat_name;
    private TextView user_name;
    private Button user_next;
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

        String database_name = user.getDisplayName();
        user_name = (TextView) view.findViewById(R.id.user_name);
        user_name.setText(database_name);

        user_next = (Button) view.findViewById(R.id.user_next);
        chat_list = (ListView) view.findViewById(R.id.chat_list);

        user_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // username??
                if (user_name.getText().toString().equals("") || chat_name.getText().toString().equals(""))
                    return;

                Intent intent = new Intent(getContext(), ChatChattingActivity.class);
                intent.putExtra("chatName", chat_name.getText().toString());
                intent.putExtra("userName", user_name.getText().toString());
                Log.e("LOG", "test : ");
                startActivity(intent);
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
                Toast.makeText(getContext(),i+1 +"번째 톡방",Toast.LENGTH_LONG).show();
                chat_name.setText(adapter.getItem(i));
            }
        });
    }
    private  void startMainActivity(){
        Intent intent=new Intent(getContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}