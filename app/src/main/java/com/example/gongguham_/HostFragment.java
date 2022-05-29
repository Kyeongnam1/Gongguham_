package com.example.gongguham_;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HostFragment extends Fragment {

    private ProgressBar mProgressBar;
    private TextView state1, state2, state3, pstate1, pstate2, pstate3;
    private Button finish_Button;
    String state;
    public HostFragment(){}

    // 채팅방 완료시 삭제
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference chatRef;
    String chat_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String dbTitle = getActivity().getIntent().getStringExtra("dbTitle");
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_host, container, false);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mProgressBar.setProgress(0);
        state1 = (TextView) rootView.findViewById(R.id.state1_textView);
        state2 = (TextView) rootView.findViewById(R.id.state2_textView);
        state3 = (TextView) rootView.findViewById(R.id.state3_textView);
        pstate1 = (TextView) rootView.findViewById(R.id.progress_text1);
        pstate2 = (TextView) rootView.findViewById(R.id.progress_text2);
        pstate3 = (TextView) rootView.findViewById(R.id.progress_text3);
        finish_Button = (Button) rootView.findViewById(R.id.finish_button);
        state1.setTextColor(Color.parseColor("#D3D3D3"));
        state2.setTextColor(Color.parseColor("#D3D3D3"));
        state3.setTextColor(Color.parseColor("#D3D3D3"));
        pstate1.setTextColor(Color.parseColor("#D3D3D3"));
        pstate2.setTextColor(Color.parseColor("#D3D3D3"));
        pstate3.setTextColor(Color.parseColor("#D3D3D3"));



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").document(dbTitle)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                       state = document.getData().get("curSituation").toString();
                       // 연관 채팅방 이름 가져오
                       chat_name = document.getData().get("chatTitle").toString();
                    }
                    else {
                        Log.d(TAG, "No such document");
                    }
                }else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        pstate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("posts").document(dbTitle)
                        .update("curSituation", "주문접수")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mProgressBar.setProgress(0);
                                state1.setTextColor(Color.parseColor("#000000"));
                                state2.setTextColor(Color.parseColor("#D3D3D3"));
                                state3.setTextColor(Color.parseColor("#D3D3D3"));
                                pstate1.setTextColor(Color.parseColor("#000000"));
                                pstate2.setTextColor(Color.parseColor("#D3D3D3"));
                                pstate3.setTextColor(Color.parseColor("#D3D3D3"));
                                Log.d("HostFragment", "Success");
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("HostFragment", "push menu in db fail" + e);
                            }
                        });
            }
        });

        pstate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("posts").document(dbTitle)
                        .update("curSituation", "배달시작")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mProgressBar.setProgress(50);
                                state1.setTextColor(Color.parseColor("#D3D3D3"));
                                state2.setTextColor(Color.parseColor("#000000"));
                                state3.setTextColor(Color.parseColor("#D3D3D3"));
                                pstate1.setTextColor(Color.parseColor("#D3D3D3"));
                                pstate2.setTextColor(Color.parseColor("#000000"));
                                pstate3.setTextColor(Color.parseColor("#D3D3D3"));
                                Log.d("HostFragment", "Success");
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("HostFragment", "push menu in db fail" + e);
                            }
                        });
            }
        });
        pstate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("posts").document(dbTitle)
                        .update("curSituation", "배달완료")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                mProgressBar.setProgress(100);
                                state1.setTextColor(Color.parseColor("#D3D3D3"));
                                state2.setTextColor(Color.parseColor("#D3D3D3"));
                                state3.setTextColor(Color.parseColor("#000000"));
                                pstate1.setTextColor(Color.parseColor("#D3D3D3"));
                                pstate2.setTextColor(Color.parseColor("#D3D3D3"));
                                pstate3.setTextColor(Color.parseColor("#000000"));
                                Log.d("HostFragment", "Success");
                                finish_Button.setVisibility(View.VISIBLE);
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("HostFragment", "push menu in db fail" + e);
                            }
                        });
            }
        });

        finish_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("posts").document(dbTitle)
                        .update("curSituation", "공구완료")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(view.getContext(),"이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(),ReviewActivity.class);
                                intent.putExtra("dbTitle", dbTitle);

                                // 채팅방 삭제
                                chatRef = firebaseDatabase.getReference("chat");
                                chatRef.child(chat_name).removeValue();
                                getActivity().startActivity(intent);
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("HostFragment", "push menu in db fail" + e);
                            }
                        });


            }
        });


        return rootView;
    }

}
