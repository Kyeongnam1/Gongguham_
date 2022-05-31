package com.Gongguham.gongguham_;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class StateSelectActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    String[] state,seoul,busan, deagu, incheon, gawngju, deajeon, ulsan, sejong, gyeonggi, gangwon, chongbuk, chungnam , jeonnam, jeonbuk, gyeongnam,gyeongbuk, jeju;
    StateSelectAdaptor stateSelectAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_select);

        state = getResources().getStringArray(R.array.state);
        seoul = getResources().getStringArray(R.array.seoul);
        busan = getResources().getStringArray(R.array.busan);
        deagu = getResources().getStringArray(R.array.deagu);
        incheon = getResources().getStringArray(R.array.inchen);
        gawngju = getResources().getStringArray(R.array.gawngju);
        deajeon = getResources().getStringArray(R.array.deajeon);
        ulsan = getResources().getStringArray(R.array.ulsan);
        sejong = getResources().getStringArray(R.array.sejong);
        gyeonggi = getResources().getStringArray(R.array.geonggi);
        gangwon = getResources().getStringArray(R.array.gangwon);
        chongbuk = getResources().getStringArray(R.array.chungbuk);
        chungnam = getResources().getStringArray(R.array.chungnam);
        jeonnam = getResources().getStringArray(R.array.jeonnam);
        jeonbuk = getResources().getStringArray(R.array.jeonbuk);
        gyeongnam = getResources().getStringArray(R.array.gyeongnam);
        gyeongbuk = getResources().getStringArray(R.array.gyeongbuk);
        jeju = getResources().getStringArray(R.array.jeju);



        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        List<StateSelectAdaptor.StateItem> data = new ArrayList<>();


        StateSelectAdaptor.StateItem seoul = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[0]);
        seoul.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.seoul.length; i++){
            seoul.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.seoul[i]));
        }
        StateSelectAdaptor.StateItem incheon = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[1]);
        incheon.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.incheon.length; i++){
            incheon.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.incheon[i]));
        }
        StateSelectAdaptor.StateItem gyeonggi = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[2]);
        gyeonggi.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.gyeonggi.length; i++){
            gyeonggi.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.gyeonggi[i]));
        }
        StateSelectAdaptor.StateItem busan = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[3]);
        busan.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.busan.length; i++){
            busan.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.busan[i]));
        }
        StateSelectAdaptor.StateItem deagu = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[4]);
        deagu.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.deagu.length; i++){
            deagu.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.deagu[i]));
        }
        StateSelectAdaptor.StateItem gwangju = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[5]);
        gwangju.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.gawngju.length; i++){
            gwangju.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.gawngju[i]));
        }
        StateSelectAdaptor.StateItem deajeon = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[6]);
        deajeon.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.deajeon.length; i++){
            deajeon.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.deajeon[i]));
        }
        StateSelectAdaptor.StateItem ulsan = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[7]);
        ulsan.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.ulsan.length; i++){
            ulsan.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.ulsan[i]));
        }
        StateSelectAdaptor.StateItem sejong = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[8]);
        sejong.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.sejong.length; i++){
            sejong.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.sejong[i]));
        }
        StateSelectAdaptor.StateItem gangwon = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[9]);
        gangwon.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.gangwon.length; i++){
            gangwon.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.gangwon[i]));
        }
        StateSelectAdaptor.StateItem jeonbuk = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[10]);
        jeonbuk.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.jeonbuk.length; i++){
            jeonbuk.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.jeonbuk[i]));
        }
        StateSelectAdaptor.StateItem jeonnam = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[11]);
        jeonnam.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.jeonnam.length; i++){
            jeonnam.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.jeonnam[i]));
        }
        StateSelectAdaptor.StateItem chongbuk = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[12]);
        chongbuk.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.chongbuk.length; i++){
            chongbuk.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.chongbuk[i]));
        }
        StateSelectAdaptor.StateItem chongnam = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[13]);
        chongnam.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.chungnam.length; i++){
            chongnam.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.chungnam[i]));
        }
        StateSelectAdaptor.StateItem gyeongbuk = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[14]);
        gyeongbuk.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.gyeongbuk.length; i++){
            gyeongbuk.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.gyeongbuk[i]));
        }
        StateSelectAdaptor.StateItem gyeongnam = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[15]);
        gyeongnam.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.gyeongnam.length; i++){
            gyeongnam.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.gyeongnam[i]));
        }
        StateSelectAdaptor.StateItem jeju = new StateSelectAdaptor.StateItem(StateSelectAdaptor.HEADER,state[16]);
        jeju.invisibleChild = new ArrayList<>();
        for(int i = 0; i< this.jeju.length; i++){
            jeju.invisibleChild.add(new StateSelectAdaptor.StateItem(StateSelectAdaptor.CHILD, this.jeju[i]));
        }


        data.add(seoul);
        data.add(incheon);
        data.add(gyeonggi);
        data.add(busan);
        data.add(deagu);
        data.add(gwangju);
        data.add(deajeon);
        data.add(ulsan);
        data.add(sejong);
        data.add(gangwon);
        data.add(jeonbuk);
        data.add(jeonnam);
        data.add(chongbuk);
        data.add(chongnam);
        data.add(gyeongbuk);
        data.add(gyeongnam);
        data.add(jeju);

        stateSelectAdaptor = new StateSelectAdaptor(this, data);

        recyclerView.setAdapter(stateSelectAdaptor);
    }
}