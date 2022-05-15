package com.example.gongguham_;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class viewMoreFragment extends Fragment {
    private static final String TAG = "fragmentviewMore";
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    public viewMoreFragment() {
        // Required empty public constructor
    }



    public static viewMoreFragment newInstance(String param1, String param2) {
        viewMoreFragment fragment = new viewMoreFragment();
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
        View view = inflater.inflate(R.layout.fragment_viewmore, container, false);

        view.findViewById(R.id.myInfoButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.myDeliveryButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.changesButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.userDeleteButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.payButton).setOnClickListener(onClickListener);  // 페이먼트 시스템
        TextView nameTextView = view.findViewById(R.id.nameText);

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
                case R.id.myInfoButton:
                    startMyInfoActivity();
                    break;

                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    // sharedPreferences 비우기
                    SharedPreferences preferences = getContext().getSharedPreferences("account",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.clear();
                    editor.commit();
                    startMainActivity();
                    break;

                case R.id.changesButton:
                    startpasswordChangeActivity();
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

                case R.id.payButton:
                    startPaymentSystem();
                    break;
            }
        }
    };


    private  void startMainActivity(){
        Intent intent=new Intent(getContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private  void startMyInfoActivity(){
        Intent intent=new Intent(getContext(),MyInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private  void startpasswordChangeActivity(){
        Intent intent=new Intent(getContext(),passwordChangeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void startPaymentSystem(){
        Intent intent = new Intent(getContext(),PaymentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}