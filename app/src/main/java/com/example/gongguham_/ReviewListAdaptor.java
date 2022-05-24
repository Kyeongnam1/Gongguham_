package com.example.gongguham_;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewListAdaptor extends RecyclerView.Adapter<ReviewListAdaptor.ViewHolder> {

    private ArrayList<ReviewUserName> userName;
    private Context mContext;
    Button review_btn;
    int pos;

    public ReviewListAdaptor(Context context, ArrayList<ReviewUserName> list){
        this.userName = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ReviewListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent, false);
        review_btn = (Button) view.findViewById(R.id.review_btn);

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
        Button review_btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            review_btn = (Button) itemView.findViewById(R.id.review_btn);
            reviewUserName = (TextView) itemView.findViewById(R.id.review_user_name);

            review_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,RatingActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("EMAIL",reviewUserName.getText().toString());
                    mContext.startActivity(intent);
                    review_btn.setVisibility(View.INVISIBLE);
                }
            });
        }
    }
}
