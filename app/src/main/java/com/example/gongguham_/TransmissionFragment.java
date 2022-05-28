package com.example.gongguham_;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TransmissionFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private CheckBox checkBox;
    private ApplicantAdapter applicantAdapter;
    private TextView text;
    private ArrayList<Applicant> applicantList;
    String userName;
    String check;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_transmission, container, false);
        String dbTitle = getActivity().getIntent().getStringExtra("dbTitle");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference documentUserReference = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        documentUserReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            userName = document.getData().get("name").toString();
                        }
                    }
                }
            }
        });

        db.collection("posts").document(dbTitle)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   DocumentSnapshot document = task.getResult();
                                                   if (document.exists()) {
                                                       ArrayList<Applicant> applicants = new ArrayList<>();
                                                       for (int i = 1; i <= Integer.parseInt(document.getData().get("curPerson").toString()); i++) {
                                                           String t_email= "email" + Integer.toString(i);
                                                           String t_account = "account" + Integer.toString(i);
                                                           String t_accountValue = "accountValue" + Integer.toString(i);
                                                           String email = document.getData().get(t_email).toString();
                                                           String account = document.getData().get(t_account).toString();
                                                           String accountValue = document.getData().get(t_accountValue).toString();
                                                           check = document.getData().get(userName+"tmCheck").toString();
                                                           String role;
                                                           if (i == 1)
                                                               role = "글쓴이";
                                                           else
                                                               role = "참여자";
                                                           applicants.add(new Applicant(
                                                                   email, role, account, accountValue));
                                                       }
                                                       mRecyclerView = (RecyclerView) rootView.findViewById(R.id.RecyleApplicantList);
                                                       applicantAdapter = new ApplicantAdapter(getActivity(), applicants, dbTitle, check);
                                                       mRecyclerView.setHasFixedSize(true); //리사이클러뷰의 크기가 변할 일이 없다는걸 알려주는 것
                                                       mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                                       applicantAdapter.setApplicantList(applicants);
                                                       mRecyclerView.setAdapter(applicantAdapter);

                                                   } else {
                                                       Log.d(TAG, "No such document");
                                                   }
                                               } else {
                                                   Log.d(TAG, "get failed with ", task.getException());
                                               }
                                           }
                                       });



        return rootView;
    }
}
