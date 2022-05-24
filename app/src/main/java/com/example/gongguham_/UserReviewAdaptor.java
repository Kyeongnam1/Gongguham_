package com.example.gongguham_;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserReviewAdaptor extends RecyclerView.Adapter<UserReviewAdaptor.ViewHolder> {

    private ArrayList<UserReviewInfo> userReviewInfo;
    private Context mContext;

    public UserReviewAdaptor(Context context, ArrayList<UserReviewInfo> list) {
        this.userReviewInfo = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public UserReviewAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_result_item, parent, false);
        return new UserReviewAdaptor.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UserReviewAdaptor.ViewHolder holder, int position) {
        holder.reviewResultUser.setText(userReviewInfo.get(position).getEmail());
        holder.reviewRating.setText(userReviewInfo.get(position).getScore()+"");
        holder.reviewComment.setText(userReviewInfo.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return userReviewInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView reviewResultUser,reviewRating, reviewComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reviewResultUser= (TextView) itemView.findViewById(R.id.review_result_user);
            reviewRating = (TextView) itemView.findViewById(R.id.review_rating);
            reviewComment = (TextView) itemView.findViewById(R.id.review_comment);
        }
    }
}