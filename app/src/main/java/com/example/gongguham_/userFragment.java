package com.example.gongguham_;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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


public class userFragment extends Fragment {
    private static final String TAG = "fragmentUser";
    String f_email;
    String f_name;
    String myemail;
    String collection;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Button btn;


    public userFragment() {
        // Required empty public constructor
    }



    public static userFragment newInstance(String param1, String param2) {
        userFragment fragment = new userFragment();
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
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        String email = getArguments().getString("EMAIL");
        myemail = user.getEmail();
        collection = myemail+"friends";

        TextView userEmailTextView = view.findViewById(R.id.userEmail);
        TextView userNameTextView = view.findViewById(R.id.userName);
        TextView userRateTextView = view.findViewById(R.id.userRate);

        btn = (Button) view.findViewById(R.id.addFriends);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(email);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            f_email = email;
                            f_name = document.getData().get("name").toString();
                            userEmailTextView.setText(email);
                            userNameTextView.setText(document.getData().get("name").toString());
                            userRateTextView.setText(document.getData().get("reviewAvScore").toString());



                        } else {
                            startToast("등록된 이메일이 없습니다.");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                FriendsInfo friendsInfo = new FriendsInfo(f_email,f_name);

                if (user != null) {
                    db.collection(collection).document(f_email).set(friendsInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    startToast("친구 추가 완료.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    startToast("친구 추가 실패.");
                                }
                            });
                }
            }
        });
        return view;
    }

    void startToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


}