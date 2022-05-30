package com.example.gongguham_;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.concurrent.Immutable;

public class GuestFragment extends Fragment {

    private ProgressBar mProgressBar;
    private TextView mTextView;
    private TextView state0;
    private TextView state1;
    private TextView state2;
    private TextView state3;
    private ImageButton refreshButton;
    private Button finishButton;
    String state;
    String userName;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String name_tmCheck;
    String tmCheck;
    String curNum;


    public GuestFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String dbTitle = getActivity().getIntent().getStringExtra("dbTitle");
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_guest, container, false);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        finishButton = (Button)rootView.findViewById(R.id.finish_button_guest);

        mTextView = (TextView) rootView.findViewById(R.id.progress_text);
        refreshButton = (ImageButton) rootView.findViewById(R.id.refresh_button);
        state0 = (TextView) rootView.findViewById(R.id.state0_textView);
        state1 = (TextView) rootView.findViewById(R.id.state1_textView);
        state2 = (TextView) rootView.findViewById(R.id.state2_textView);
        state3 = (TextView) rootView.findViewById(R.id.state3_textView);
        state0.setTextColor(Color.parseColor("#D3D3D3"));
        state1.setTextColor(Color.parseColor("#D3D3D3"));
        state2.setTextColor(Color.parseColor("#D3D3D3"));
        state3.setTextColor(Color.parseColor("#D3D3D3"));
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("posts").document(dbTitle)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        state = document.getData().get("curSituation").toString();
                        DocumentReference documentUserReference = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
                        documentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful())
                                {
                                    DocumentSnapshot document1 = task.getResult();
                                    if (document1 != null) {
                                        if (document1.exists()) {
                                            userName = document1.getData().get("name").toString();
                                            curNum = document.getData().get(userName).toString();
                                            name_tmCheck= curNum+"tmCheck";
                                            tmCheck = document.getData().get(name_tmCheck).toString();
                                            if(state.equals("주문접수") && tmCheck.equals("true"))
                                            {
                                                mTextView.setText("주문접수");
                                                state0.setTextColor(Color.parseColor("#D3D3D3"));
                                                state1.setTextColor(Color.parseColor("#000000"));
                                                state2.setTextColor(Color.parseColor("#D3D3D3"));
                                                state3.setTextColor(Color.parseColor("#D3D3D3"));
                                                mProgressBar.setProgress(34);
                                            }
                                            else if(state.equals("배달시작") && tmCheck.equals("true"))
                                            {
                                                mTextView.setText("배달시작");
                                                state0.setTextColor(Color.parseColor("#D3D3D3"));
                                                state1.setTextColor(Color.parseColor("#D3D3D3"));
                                                state2.setTextColor(Color.parseColor("#000000"));
                                                state3.setTextColor(Color.parseColor("#D3D3D3"));
                                                mProgressBar.setProgress(67);
                                            }
                                            else if(state.equals("배달완료") && tmCheck.equals("true"))
                                            {
                                                mTextView.setText("배달완료");
                                                state0.setTextColor(Color.parseColor("#D3D3D3"));
                                                state1.setTextColor(Color.parseColor("#D3D3D3"));
                                                state2.setTextColor(Color.parseColor("#D3D3D3"));
                                                state3.setTextColor(Color.parseColor("#000000"));
                                                mProgressBar.setProgress(100);
                                                finishButton.setVisibility(View.VISIBLE);
                                            }
                                            else if(tmCheck.equals("true"))
                                            {
                                                mTextView.setText("송금확인");
                                                state0.setTextColor(Color.parseColor("#000000"));
                                                state1.setTextColor(Color.parseColor("#D3D3D3"));
                                                state2.setTextColor(Color.parseColor("#D3D3D3"));
                                                state3.setTextColor(Color.parseColor("#D3D3D3"));
                                                mProgressBar.setProgress(1);
                                            }
                                            else if(tmCheck.equals("false"))
                                            {
                                                mTextView.setText("송금확인중");
                                                state0.setTextColor(Color.parseColor("#D3D3D3"));
                                                state1.setTextColor(Color.parseColor("#D3D3D3"));
                                                state2.setTextColor(Color.parseColor("#D3D3D3"));
                                                state3.setTextColor(Color.parseColor("#D3D3D3"));
                                                mProgressBar.setProgress(0);
                                            }
                                        }
                                    }
                                }
                            }
                        });




                    }
                    else {
                        Log.d(TAG, "No such document");
                    }
                }else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((DeliveryProgressActivity)getActivity()).refresh();

                Intent intent = new Intent(getContext(),ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),0,intent, PendingIntent.FLAG_IMMUTABLE);

                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),pendingIntent);

            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference documentUserReference = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
                documentUserReference.update("curPost", "null").
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("gf", "pda");
                            }

                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("gf", "Error update curPost" + e);

                            }
                        });
                Toast.makeText(view.getContext(),"이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(),ReviewActivity.class);
                intent.putExtra("dbTitle", dbTitle);
                getActivity().startActivity(intent);

            }
        });

        return rootView;
    }

    // 푸쉬 알림 채널 설정
    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "LemubitReminderChannel";
            String description = "Channel for Lemubit Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyLemubit",name,importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
