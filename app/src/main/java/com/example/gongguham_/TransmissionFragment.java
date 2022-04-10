package com.example.gongguham_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransmissionFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ApplicantAdapter applicantAdapter;
    private ArrayList<Applicant> applicant;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_transmission, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.RecyleApplicantList);
        applicantAdapter = new ApplicantAdapter(getActivity(), applicant);
        mRecyclerView.setAdapter(applicantAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        applicant = new ArrayList<>();

//        Sample Data
        applicant.add(new Applicant("테스트", "글쓴이")); //추가안됨

        applicant.add(new Applicant("테스트", "글쓴이"));
        applicant.add(new Applicant("테스트", "참여자"));
        applicant.add(new Applicant("테스트", "참여자"));
        applicant.add(new Applicant("테스트", "참여자"));
        applicant.add(new Applicant("테스트", "참여자"));


        applicantAdapter.setApplicantList(applicant);

        return rootView;
    }
}
