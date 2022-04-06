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

public class Menu1Fragment extends Fragment {

    //    RecyclerView 생성
    private RecyclerView mRecyclerView;
    private PostAdaptor postAdaptor;
    private ArrayList<PostItem> postItems;
    private AppCompatButton btn_add;
    private AppCompatButton btn_state;
    private Spinner sort_spinner;

    String[] sort_by = {"option1", "option2", "option3"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_menu1, container, false);

//        Add Post Button onClickListener
        btn_add = (AppCompatButton) rootView.findViewById(R.id.btn_add_post);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), AddPostItem.class));
            }
        });
//        State Select Button onClickListener
        btn_state = (AppCompatButton) rootView.findViewById(R.id.btn_select);
        btn_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivityForResult(new Intent(getActivity(), StateSelectActivity.class),0);
            }
        });
        //        Spinner 생성
        sort_spinner = (Spinner) rootView.findViewById(R.id.sort_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item,sort_by);
        sort_spinner.setAdapter(arrayAdapter);
        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("spinner Event", "선택됨");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        RecyclerView 생성
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclePostList);
        postAdaptor = new PostAdaptor(getActivity(), postItems);
        mRecyclerView.setAdapter(postAdaptor);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postItems = new ArrayList<>();

        for(int i = 0; i<30; i++){
            postItems.add(new PostItem("교촌치킨 드실 분!!", "명덕관 1층", "18:30", "5명", i));
        }
        postAdaptor.setPostlist(postItems);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0){
            String state = data.getStringExtra("STATE");
            Log.i("StateSelectResult",state);
        }
    }
}
