package com.example.gongguham_;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;

public class HostFragment extends Fragment {

    private ProgressBar mProgressBar;
    private TextView state1, state2, state3, pstate1, pstate2, pstate3;
    private Button finish_Button;
    public HostFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_host, container, false);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mProgressBar.setProgress(50);
        state1 = (TextView) rootView.findViewById(R.id.state1_textView);
        state2 = (TextView) rootView.findViewById(R.id.state2_textView);
        state3 = (TextView) rootView.findViewById(R.id.state3_textView);
        pstate1 = (TextView) rootView.findViewById(R.id.progress_text1);
        pstate2 = (TextView) rootView.findViewById(R.id.progress_text2);
        pstate3 = (TextView) rootView.findViewById(R.id.progress_text3);
        finish_Button = (Button) rootView.findViewById(R.id.finish_button);
        state1.setTextColor(Color.parseColor("#D3D3D3"));
        state2.setTextColor(Color.parseColor("#000000"));
        state3.setTextColor(Color.parseColor("#D3D3D3"));
        pstate1.setTextColor(Color.parseColor("#D3D3D3"));
        pstate2.setTextColor(Color.parseColor("#000000"));
        pstate3.setTextColor(Color.parseColor("#D3D3D3"));


        finish_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();

                getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });


        return rootView;
    }

}
