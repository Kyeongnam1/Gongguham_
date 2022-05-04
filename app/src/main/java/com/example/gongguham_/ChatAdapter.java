package com.example.gongguham_;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter {

    ArrayList<ChatDTO> chatDTOS;
    LayoutInflater layoutInflater;

    public ChatAdapter(ArrayList<ChatDTO> chatDTOS, LayoutInflater layoutInflater) {
        this.chatDTOS = chatDTOS;
        this.layoutInflater = layoutInflater;
    }


    @Override
    public int getCount() {
        return chatDTOS.size();
    }

    @Override
    public Object getItem(int position) {
        return chatDTOS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        //현재 보여줄 번째의(position)의 데이터로 뷰를 생성
        ChatDTO item= chatDTOS.get(position);

        //재활용할 뷰는 사용하지 않음!!
        View itemView = null;

        // 이거 시발 왜안되는거야??
        if(item.getUserName().equals(G.username)){
            itemView= layoutInflater.inflate(R.layout.my_chatbox,viewGroup,false);
        }else{
            itemView= layoutInflater.inflate(R.layout.other_chatbox,viewGroup,false);
        }

        //만들어진 itemView에 값들 설정
        TextView tvName= itemView.findViewById(R.id.tv_name);
        TextView tvMsg= itemView.findViewById(R.id.tv_msg);
        TextView tvTime= itemView.findViewById(R.id.tv_time);

        tvName.setText(item.getUserName());
        tvMsg.setText(item.getMessage());
        tvTime.setText(item.getTime());

        return itemView;
    }

}
