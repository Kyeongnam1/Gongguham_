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

public class CashHistory extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private String userEmail;
    private RecyclerView recyclerView;
    private HistoryListAdaptor historyListAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_history);

        userEmail = user.getEmail();

        db.collection(userEmail + "history")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<HistoryInfo> historyInfo = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()){
                                historyInfo.add(new HistoryInfo(
                                        document.getData().get("email").toString(),
                                        Integer.parseInt(document.getData().get("plus_point").toString()),
                                        Integer.parseInt(document.getData().get("minus_point").toString()),
                                        Integer.parseInt(document.getData().get("current_point").toString()),
                                        document.getData().get("trader").toString(),
                                        document.getData().get("time").toString()
                                ));
                            }
                            recyclerView = (RecyclerView) findViewById(R.id.recycler_history_list);
                            historyListAdaptor = new HistoryListAdaptor(getApplicationContext(),historyInfo);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                            recyclerView.setAdapter(historyListAdaptor);

                        }else{
                            Log.e("Error", "task Error!");
                        }
                    }
                });
    }
}