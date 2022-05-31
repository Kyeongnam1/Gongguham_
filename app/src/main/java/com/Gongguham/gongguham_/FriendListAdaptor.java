package com.Gongguham.gongguham_;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendListAdaptor extends RecyclerView.Adapter<FriendListAdaptor.ViewHolder> {

    private ArrayList<FriendsInfo> friendsInfo;
    private Context mContext;

    public FriendListAdaptor(Context context, ArrayList<FriendsInfo> list){
        this.friendsInfo = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public FriendListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item,parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FriendListAdaptor.ViewHolder holder, int position) {
        holder.friendEmail.setText(friendsInfo.get(position).getEmail());
        holder.friendName.setText(friendsInfo.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return friendsInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView friendName, friendEmail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            friendName = (TextView) itemView.findViewById(R.id.friends_name);
            friendEmail = (TextView) itemView.findViewById(R.id.friends_email);
        }
    }
}
