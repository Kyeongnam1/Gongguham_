package com.example.gongguham_;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MyInfoFragment extends Fragment {
    private static final String TAG = "fragmentMyInfo";
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public MyInfoFragment() {
        // Required empty public constructor
    }



    public static MyInfoFragment newInstance(String param1, String param2) {
        MyInfoFragment fragment = new MyInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_myinfo, container, false);


        view.findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.userDeleteButton).setOnClickListener(onClickListener);


        final TextView nameTextView = view.findViewById(R.id.nameText);
        final TextView phoneNumberTextView = view.findViewById(R.id.phoneNumberText);
        final TextView genderTextView = view.findViewById(R.id.genderText);
        final TextView accountValueTextView = view.findViewById(R.id.accountValueText);
        final TextView accountTextView = view.findViewById(R.id.accountText);
        final TextView emailTextView = view.findViewById(R.id.emailText);
        final TextView birthDayTextView = view.findViewById(R.id.birthdayText);
        final TextView addressTextView = view.findViewById(R.id.addressText);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.e(TAG, "DocumentSnapshot data: " + document.getData());

                            nameTextView.setText(document.getData().get("name").toString());
                            phoneNumberTextView.setText(document.getData().get("phoneNumber").toString());
                            genderTextView.setText(document.getData().get("gender").toString());
                            accountValueTextView.setText(document.getData().get("accountValue").toString());
                            accountTextView.setText(document.getData().get("account").toString());
                            emailTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            birthDayTextView.setText(document.getData().get("birthday").toString());
                            addressTextView.setText(document.getData().get("address").toString());
                        } else {
                            Log.e(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    startMainActivity();
                    // sharedPreferences 비우기
                    SharedPreferences preferences = getContext().getSharedPreferences("account",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.clear();
                    editor.commit();
                    break;
                case R.id.userDeleteButton:
                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error deleting document", e);
                                }
                            });
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                    FirebaseAuth.getInstance().signOut();

                    startMainActivity();
                    break;
            }
        }
    };

    private  void startMainActivity(){
        Intent intent=new Intent(getContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}