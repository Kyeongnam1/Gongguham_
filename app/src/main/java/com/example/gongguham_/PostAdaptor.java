package com.example.gongguham_;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PostAdaptor extends RecyclerView.Adapter<PostAdaptor.ViewHolder> {

    private ArrayList<PostItem> postlist;
    private Context mContext;

    public PostAdaptor(Context context, ArrayList<PostItem> list){
        this.mContext = context;
        this.postlist = list;
    }
    @NonNull
    @Override
    public PostAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdaptor.ViewHolder holder, int position) {
        holder.onBind(postlist.get(position));
    }

    public void setPostlist(ArrayList<PostItem> list){
        this.postlist = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return postlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView postImg;
        TextView postTitle, meetingArea, closeTime, maxPerson;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postImg = (ImageView) itemView.findViewById(R.id.post_img);
            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            meetingArea = (TextView) itemView.findViewById(R.id.meeting_area);
            closeTime = (TextView) itemView.findViewById(R.id.close_time);
            maxPerson = (TextView) itemView.findViewById(R.id.max_person);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(mContext, PostDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("post_indx", (Parcelable) postlist.get(pos));

                        mContext.startActivity(intent);
                    }
                }
            });


        }

        void onBind(PostItem item){
            postImg.setImageResource(item.getPostImg());
            postTitle.setText(item.postTitle);
            meetingArea.setText(item.meetingArea);
            closeTime.setText(item.closeTime);
            maxPerson.setText(item.maxPerson);
        }
    }
}
