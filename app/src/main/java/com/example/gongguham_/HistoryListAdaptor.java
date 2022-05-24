package com.example.gongguham_;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryListAdaptor extends RecyclerView.Adapter<HistoryListAdaptor.ViewHolder> {

    private ArrayList<HistoryInfo> historyInfo;
    private Context mContext;

    public HistoryListAdaptor(Context context, ArrayList<HistoryInfo> list) {
        this.historyInfo = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public HistoryListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.histroy_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryListAdaptor.ViewHolder holder, int position) {
        holder.historyTime.setText(historyInfo.get(position).getTime());
        holder.historySender.setText(historyInfo.get(position).getTrader());
        if(historyInfo.get(position).getMinus_point() == 0) {
            holder.historyType.setText("입금");
            holder.historyAmount.setText(historyInfo.get(position).getPlus_point()+"");
            holder.historyAmount.setTextColor(Color.parseColor("#0000FF"));
        }else{
            holder.historyType.setText("출금");
            holder.historyAmount.setText(historyInfo.get(position).getMinus_point()+"");
            holder.historyAmount.setTextColor(Color.parseColor("#FF0000"));
        }
        holder.historyCurrentAmount.setText(historyInfo.get(position).getCurrent_point()+"");

    }

    @Override
    public int getItemCount() {
        return historyInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView historyTime,historySender, historyType, historyAmount, historyCurrentAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            historyTime = (TextView) itemView.findViewById(R.id.history_time);
            historySender = (TextView) itemView.findViewById(R.id.history_sender);
            historyType = (TextView) itemView.findViewById(R.id.history_transfer_type);
            historyAmount = (TextView) itemView.findViewById(R.id.history_trans_amount);
            historyCurrentAmount = (TextView) itemView.findViewById(R.id.history_current_amount);


        }
    }
}
