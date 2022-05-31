package com.Gongguham.gongguham_;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StateSelectAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEADER = 0;
    public static final int CHILD = 1;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    private Context mContext;
    private List<StateItem> data;

    public StateSelectAdaptor(Context context,List<StateItem> data){
        this.mContext = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        Context context = parent.getContext();
        switch (viewType){
            case HEADER:
                LayoutInflater inflaterHeader = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflaterHeader.inflate(R.layout.state_list_header,parent,false);
                ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                return header;

            case CHILD:
                LayoutInflater inflaterChild = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflaterChild.inflate(R.layout.list_child, parent, false);
                ListChildViewHolder child = new ListChildViewHolder(view);
                return child;

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final StateItem item = data.get(position);
        final StateItem preitem = data.get(position);
        switch (item.type){
            case HEADER:

                final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                itemController.referralItem = item;
                itemController.header_title.setText(item.text);
                itemController.btn_expand_toggle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(item.invisibleChild == null){
                            item.invisibleChild = new ArrayList<StateItem>();
                            int count = 0;
                            int pos = data.indexOf(itemController.referralItem);
                            Log.i("position과 preposition", pos +"/" + prePosition);
                            while(data.size() > pos + 1 && data.get(pos+1).type == CHILD){
                                item.invisibleChild.add(data.remove(pos+1));
                                count++;
                            }
                            Log.i("데이터값 확인", pos+1 + "/" + count);
                            notifyItemRangeRemoved(pos+1, count);

                            Log.i("position과 preposition", pos +"/" + prePosition);

                        }else{
                            int count = 0;

                            int pos = data.indexOf(itemController.referralItem);
                            int index = pos+1;
                            int preindex = prePosition +1;
                            int predataSize = 0;

                            for(StateItem i : item.invisibleChild){
                                Log.i("데이터값 확인 index", String.valueOf(index));
                                data.add(index,i);
                                index ++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            Log.i("생성 주기", "클릭 지역 생성 종료");
                            Log.i("position과 preposition", pos +"/" + prePosition);
                            prePosition = pos;
                            Log.i("position과 preposition", pos +"/" + prePosition);
//                            notifyItemRemoved(prePosition);
                            item.invisibleChild = null;



                        }

                    }
                });
                break;

            case CHILD:

                final ListChildViewHolder itemController1 = (ListChildViewHolder) holder;
                int pos = itemController1.getAdapterPosition();

                itemController1.referralItem = item;
                itemController1.child_title.setText(item.text);
                String state = (String) itemController1.child_title.getText();
                itemController1.child_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.putExtra("STATE",state);
                        ((Activity) mContext).setResult(Activity.RESULT_OK, intent);
                        Log.i("OnChildItem CLick", pos+"번째 클릭!" + state);
                        ((Activity) mContext).finish();
                    }
                });
                break;


        }
    }

    @Override
    public int getItemViewType(int position){
        return data.get(position).type;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ListHeaderViewHolder extends RecyclerView.ViewHolder{
        public TextView header_title;
        public ImageView btn_expand_toggle;
        public StateItem referralItem;

        public ListHeaderViewHolder(View itemView){
            super(itemView);
            header_title = (TextView) itemView.findViewById(R.id.header_title);
            btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
        }
    }

    public static class ListChildViewHolder extends RecyclerView.ViewHolder{
        public TextView child_title;
        public StateItem referralItem;

        public ListChildViewHolder(View itemView){
            super(itemView);
            child_title = (TextView) itemView.findViewById(R.id.child_title);

        }
    }
    public static class StateItem {
        public int type;
        public String text;

        public List<StateItem> invisibleChild;


        public StateItem(){

        }
        public StateItem(int type, String text){
            this.type = type;
            this.text = text;
        }
    }
}
