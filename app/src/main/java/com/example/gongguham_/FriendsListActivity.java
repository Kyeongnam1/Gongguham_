package com.example.gongguham_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FriendsListActivity extends AppCompatActivity {


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String userEmail;
    private RecyclerView recyclerView;
    private FriendListAdaptor friendListAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        userEmail = user.getEmail();

        db.collection(userEmail + "friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<FriendsInfo> friendsInfo = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()){
                                friendsInfo.add(new FriendsInfo(
                                        document.getData().get("email").toString(),
                                        document.getData().get("name").toString()
                                ));
                            }
                            recyclerView = (RecyclerView) findViewById(R.id.recycler_friend_list);
                            friendListAdaptor = new FriendListAdaptor(getApplicationContext(),friendsInfo);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(friendListAdaptor);

                        }else{
                            Log.e("Error", "task Error!");
                        }
                    }
                });

    }
}