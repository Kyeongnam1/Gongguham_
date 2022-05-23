package com.example.gongguham_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DocumentReference mDatabase;

    private String userEmail;
    private RecyclerView recyclerView;
    private ReviewListAdaptor reviewListAdaptor;

    Button btn;

    @Override public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        Intent intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);


        btn = findViewById(R.id.btn_end);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        String dbTitle = intent.getStringExtra("dbTitle");

        userEmail = user.getEmail();

        mDatabase = db.collection("posts").document(dbTitle);
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    ArrayList<ReviewUserName> userName = new ArrayList<>();
                    DocumentSnapshot document = task.getResult();
                    if(document != null){
                        if(document.exists()){
                            for(int i=1; i<=Integer.parseInt(document.getData().get("curPerson").toString()); i++){
                                if(userEmail.equals(document.getData().get("email"+i).toString())){
                                    continue;
                                }else{
                                    userName.add(new ReviewUserName(document.getData().get("email"+i).toString()));
                                }

                            }

                            recyclerView = (RecyclerView) findViewById(R.id.recycler_review_list);
                            reviewListAdaptor = new ReviewListAdaptor(getApplicationContext(), userName);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(reviewListAdaptor);
                        }
                    }
                }

            }
        });
    }

}