package com.Gongguham.gongguham_;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";
    SwipeRefreshLayout swipeRefreshLayout;
    private static ViewGroup viewGroup;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String userEmail;
    private DocumentReference mDatabase;
    private DocumentReference pDatabase;
    private String curUserLocation;

    //    RecyclerView 생성
    private RecyclerView mRecyclerView;
    private PostAdaptor postAdaptor;
    final private ArrayList<PostItem> postItems = new ArrayList<>();
    private AppCompatButton btn_add;
    private AppCompatButton btn_state;
    private Spinner sort_spinner;

    TextView textView;

    String[] sort_by = {"돈까스, 회, 일식", "중식", "치킨", "백반, 죽, 국수", "카페, 디저트", "분식", "찜, 탕, 찌개", "피자", "양식", "고기, 구이", "족발, 보쌈", "아시안", "패스트푸드", "야식", "도시락"};


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        viewGroup = rootView;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        swipeRefreshLayout =rootView.findViewById(R.id.swipe_layout);
        Log.i("layout check", String.valueOf(swipeRefreshLayout));
        swipeRefreshLayout.setOnRefreshListener(this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        Add Post Button onClickListener
        btn_add = (AppCompatButton) rootView.findViewById(R.id.btn_add_post);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = db.collection("locations").document(user.getEmail());
                mDatabase.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                if (document.exists()) {
                                    getActivity().startActivity(new Intent(getActivity(), AddPostItem.class));
                                } else{
                                    Toast.makeText(getContext(),"현재 위치를 설정해주세요.", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });
            }
        });
//        State Select Button onClickListener
        btn_state = (AppCompatButton) rootView.findViewById(R.id.btn_select);
        btn_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivityForResult(new Intent(getActivity(), gpsActivity.class),0);
            }
        });
        //        Spinner 생성
        pDatabase = db.collection("posts").document(user.getEmail());


        sort_spinner = (Spinner) rootView.findViewById(R.id.sort_spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item,sort_by);
        sort_spinner.setAdapter(arrayAdapter);
        sort_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedOption = sort_spinner.getSelectedItem().toString();
                db.collection("posts")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    ArrayList<PostInfo> postInfo = new ArrayList<>();

                                    if(curUserLocation != null){
                                        for(QueryDocumentSnapshot document : task.getResult()) {
                                            Log.e(TAG, "테스트: " + document.getData().get("userLocation").toString());
                                            Log.e(TAG, "테스트: " + document.getData().get("postCategory").toString());
                                            if(curUserLocation.equals(slicingLocation(document.getData().get("userLocation").toString()))) {
                                                if(selectedOption.equals(document.getData().get("postCategory").toString())) {
                                                    String hour = document.getData().get("closeTime_hour").toString() + "시 ";
                                                    String minute = document.getData().get("closeTime_minute").toString() + "분";
                                                    String time = hour + minute;

                                                    postInfo.add(new PostInfo(
                                                            document.getData().get("postTitle").toString(),
                                                            document.getData().get("postCategory").toString(),
                                                            document.getData().get("postContent").toString(),
                                                            document.getData().get("meetingArea").toString(),
                                                            document.getData().get("closeTime_hour").toString(),
                                                            time,
                                                            Integer.parseInt(document.getData().get("maxPerson").toString()),
                                                            Integer.parseInt(document.getData().get("deliveryFee").toString()),
                                                            document.getData().get("userLocation").toString(),
                                                            document.getData().get("chatTitle").toString(),
                                                            document.getData().get("postEmail").toString(),
                                                            Integer.parseInt(document.getData().get("curPerson").toString())
                                                    ));
                                                }
                                            }
                                        }
                                    }
                                    mRecyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclePostList);
                                    postAdaptor = new PostAdaptor(getActivity(), postInfo);
                                    mRecyclerView.setHasFixedSize(true);
                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    postAdaptor.setPostlist(postInfo);
                                    mRecyclerView.setAdapter(postAdaptor);
                                }else{
                                    Log.e("Error", "task Error!");
                                }
                            }
                        });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                textView.setText("음식 종류");
            }
        });




//        RecyclerView 생성


        userEmail = user.getEmail();

        mDatabase = FirebaseFirestore.getInstance().collection("locations").document(userEmail);
        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            curUserLocation = slicingLocation(document.getData().get("curLoc").toString());
                            Log.e(TAG, "제발" + curUserLocation );

                        }
                    }
                }
            }

        });



        return rootView;
    }

    private  void startLoginActivity(){
        Intent intent=new Intent(getContext(),loginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private  void startmemberInitActivity(){
        Intent intent=new Intent(getContext(),memberInitActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @Override
    public void onRefresh() {
        String selectedOption = sort_spinner.getSelectedItem().toString();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                db.collection("posts")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    ArrayList<PostInfo> postInfo = new ArrayList<>();

                                    if(curUserLocation != null){
                                        for(QueryDocumentSnapshot document : task.getResult()) {
                                            Log.e(TAG, "테스트: " + document.getData().get("userLocation").toString());
                                            Log.e(TAG, "테스트: " + document.getData().get("postCategory").toString());
                                            if(curUserLocation.equals(slicingLocation(document.getData().get("userLocation").toString()))) {
                                                if(selectedOption.equals(document.getData().get("postCategory").toString())) {
                                                    String hour = document.getData().get("closeTime_hour").toString()+"시 ";
                                                    String minute = document.getData().get("closeTime_minute").toString()+"분";
                                                    String time = hour + minute;

                                                    postInfo.add(new PostInfo(
                                                            document.getData().get("postTitle").toString(),
                                                            document.getData().get("postCategory").toString(),
                                                            document.getData().get("postContent").toString(),
                                                            document.getData().get("meetingArea").toString(),
                                                            document.getData().get("closeTime_hour").toString(),
                                                            time,
                                                            Integer.parseInt(document.getData().get("maxPerson").toString()),
                                                            Integer.parseInt(document.getData().get("deliveryFee").toString()),
                                                            document.getData().get("userLocation").toString(),
                                                            document.getData().get("chatTitle").toString(),
                                                            document.getData().get("postEmail").toString(),
                                                            Integer.parseInt(document.getData().get("curPerson").toString())
                                                    ));
                                                }
                                            }
                                        }
                                    }

                                    mRecyclerView = (RecyclerView) viewGroup.findViewById(R.id.RecyclePostList);
                                    postAdaptor = new PostAdaptor(getActivity(), postInfo);
                                    mRecyclerView.setHasFixedSize(true);
                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    postAdaptor.setPostlist(postInfo);
                                    mRecyclerView.setAdapter(postAdaptor);
                                }else{
                                    Log.e("Error", "task Error!");
                                }
                            }
                        });
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 500);
    }

    private String slicingLocation(String curUserLocation){

        String temp = "";

        String[] slicedLocation = curUserLocation.split(" ");
        slicedLocation[slicedLocation.length-1] = null;
        for(int i = 0 ; i < slicedLocation.length-1; i++){
            temp += slicedLocation[i];
        }

        return temp;
    }


}