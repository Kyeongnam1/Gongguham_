package com.example.gongguham_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class TransmissionFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter adapter;
    private CheckBox checkBox;
    private ApplicantAdapter applicantAdapter;
    private TextView text;
    private ArrayList<Applicant> applicantList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_transmission, container, false);

        db.collection("temp")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            ArrayList<Applicant> applicants = new ArrayList<>();

                            for(QueryDocumentSnapshot document : task.getResult()) {

                                String name = document.getData().get("name").toString();
                                String role;
                                String account = document.getData().get("account").toString();
                                String accountValue = document.getData().get("accountValue").toString();
                                if(document.getData().get("role").equals(true))
                                    role = "글쓴이";
                                else
                                    role = "참여자";
                                applicants.add(new Applicant(
                                        name,role,account,accountValue));
                            }

                            mRecyclerView = (RecyclerView) rootView.findViewById(R.id.RecyleApplicantList);
                            applicantAdapter = new ApplicantAdapter(getActivity(), applicants);
                            mRecyclerView.setHasFixedSize(true); //리사이클러뷰의 크기가 변할 일이 없다는걸 알려주는 것
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            applicantAdapter.setApplicantList(applicants);
                            mRecyclerView.setAdapter(applicantAdapter);

                        }else{
                            Log.e("Error", "task Error!");
                        }
                    }
                });




        return rootView;
    }
}
