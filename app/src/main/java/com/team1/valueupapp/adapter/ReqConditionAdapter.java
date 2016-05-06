package com.team1.valueupapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team1.valueupapp.R;
import com.team1.valueupapp.item.ReqConditionItem;

import java.util.ArrayList;

/**
 * Created by knulps on 16. 4. 20..
 * 신청 현황 화면
 */

public class ReqConditionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<ReqConditionItem> items;
    Context context;

    public ReqConditionAdapter(Context context, ArrayList<ReqConditionItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_req_condition, parent, false);
        return new holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ReqConditionItem item = items.get(position);
        ((holder) holder).title.setText(item.getTitle());
        ((holder) holder).detail.setText(item.getDetail());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        TextView title, detail;

        public holder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            detail = (TextView) itemView.findViewById(R.id.detail);
        }
    }
}


