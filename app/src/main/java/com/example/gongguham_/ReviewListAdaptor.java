package com.example.gongguham_;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewListAdaptor extends RecyclerView.Adapter<ReviewListAdaptor.ViewHolder> {

    private ArrayList<ReviewUserName> userName;
    private Context mContext;

    public ReviewListAdaptor(Context context, ArrayList<ReviewUserName> list){
        this.userName = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ReviewListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent, false);
        return new ReviewListAdaptor.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ReviewListAdaptor.ViewHolder holder, int position) {
        holder.reviewUserName.setText(userName.get(position).getUserName());
//        여기서 위랑 같은 방식으로 review_item 에 있는 컨포넌트들 가져다가 setText 해주면 됨
    }

    @Override
    public int getItemCount() {
        return userName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

//        여기서 review_item 에 있는 컨포넌트들 findViewById 해주면 됨
        TextView reviewUserName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reviewUserName = (TextView) itemView.findViewById(R.id.review_user_name);
        }
    }
}
