package com.Gongguham.gongguham_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class TransmissionActivity extends AppCompatActivity {

    Button finishbtn;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private  TransmissionFragment transmissionFragment = new TransmissionFragment();

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
        Intent intent1 = getIntent();
        String dbTitle = intent1.getStringExtra("dbTitle");
        setContentView(R.layout.activity_transmission);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, transmissionFragment).commitAllowingStateLoss();

        finishbtn = findViewById(R.id.finishbutton);

        finishbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("posts").document(dbTitle)
                        .update("curSituation", "배달진행상황페이지")
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
                Intent intent = new Intent(TransmissionActivity.this, DeliveryProgressActivity.class);
                intent.putExtra("dbTitle", dbTitle);
                intent.putExtra("role", "글쓴이");
                startActivity(intent);
            }
        });
    }


}
