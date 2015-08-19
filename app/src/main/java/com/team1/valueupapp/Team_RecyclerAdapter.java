package com.team1.valueupapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


/**
 * Created by hyemi on 2015-08-16.
 */
public class Team_RecyclerAdapter extends RecyclerView.Adapter<Team_RecyclerAdapter.ViewHolder>{
    Context context;
    List<Team_item> items_list;
    int itemLayout;

    Team_RecyclerAdapter(Context context, List<Team_item> items, int itemLayout) {
        this.context=context;
        this.items_list=items;
        this.itemLayout=itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Team_item item=items_list.get(position);
/*
        if(item.getState()==true) {
            holder.state.setText("팀빌딩 완료");
        } else {
            holder.state.setText("팀빌딩 중");
        }//end else
        holder.idea.setText(item.getIdea());
        holder.idea_info.setText(item.getIdea_info());
        holder.name1.setText(item.getName1());
        holder.name2.setText(item.getName2());
        holder.name3.setText(item.getName3());
        holder.name4.setText(item.getName4());
        holder.name5.setText(item.getName5());
        holder.name6.setText(item.getName6());
        holder.profile.setBackgroundResource(item.getProfile());

*/

    }

    @Override
    public int getItemCount() {
        return items_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView state;
        TextView idea;
        TextView idea_info;
        TextView name1;
        TextView name2;
        TextView name3;
        TextView name4;
        TextView name5;
        TextView name6;
        ImageView profile;
        CardView cardview;

        TextView myteam_num;
        TextView myteam_state;

        public ViewHolder(View itemView) {
            super(itemView);/*
            profile = (ImageView) itemView.findViewById(R.id.profile);
            state = (TextView) itemView.findViewById(R.id.state);
            idea = (TextView) itemView.findViewById(R.id.idea);
            idea_info = (TextView) itemView.findViewById(R.id.idea_info);
            name1 = (TextView) itemView.findViewById(R.id.name1);
            name2 = (TextView) itemView.findViewById(R.id.name2);
            name3 = (TextView) itemView.findViewById(R.id.name3);
            name4 = (TextView) itemView.findViewById(R.id.name4);
            name5 = (TextView) itemView.findViewById(R.id.name5);
            name6 = (TextView) itemView.findViewById(R.id.name6);
            myteam_num = (TextView) itemView.findViewById(R.id.myteam_num);
            myteam_state = (TextView) itemView.findViewById(R.id.myteam_state);
            cardview = (CardView) itemView.findViewById(R.id.cardview);*/
        }//ViewHolder
    }
}
