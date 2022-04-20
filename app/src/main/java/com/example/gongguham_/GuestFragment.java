package com.example.gongguham_;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GuestFragment extends Fragment {

    private ProgressBar mProgressBar;
    private TextView mTextView;
    private TextView state1;
    private TextView state2;
    private TextView state3;
    public GuestFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_guest, container, false);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mProgressBar.setProgress(50);
        mTextView = (TextView) rootView.findViewById(R.id.progress_text);
        mTextView.setText("배달시작");
        state1 = (TextView) rootView.findViewById(R.id.state1_textView);
        state2 = (TextView) rootView.findViewById(R.id.state2_textView);
        state3 = (TextView) rootView.findViewById(R.id.state3_textView);
        state1.setTextColor(Color.parseColor("#D3D3D3"));
        state2.setTextColor(Color.parseColor("#000000"));
        state3.setTextColor(Color.parseColor("#D3D3D3"));



        return rootView;
    }

}
