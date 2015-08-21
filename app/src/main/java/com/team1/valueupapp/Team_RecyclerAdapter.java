package com.team1.valueupapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;


/**
 * Created by hyemi on 2015-08-16.
 */
public class Team_RecyclerAdapter extends RecyclerView.Adapter{
    Context context;
    List<Team_item> items_list;
    int itemLayout;
    int HOLDER=0;
    int FOOTER=1;

    Team_RecyclerAdapter(Context context, List<Team_item> items, int itemLayout) {
        this.context=context;
        this.items_list=items;
        this.itemLayout=itemLayout;
    }

    @Override
    public int getItemViewType(int position) {
        if(position<items_list.size())
            return HOLDER;
        else if(position==items_list.size())
            return FOOTER;
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==HOLDER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
            return new holder(v);
        }
        else if(viewType==FOOTER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fab_footer, parent, false);
            return new footer(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof holder) {
            final Team_item item = items_list.get(position);

            ((holder) holder).title.setText(item.getTitle());
            ((holder) holder).name.setText(item.getName());
            ((holder) holder).detail.setText(item.getDetail());
            ((holder) holder).pick.setText(item.getPick() + " 소속");
            ((holder) holder).team.setText(item.getTeam() + " 명");
            if (item.getPick().length() > 0) {
                ((holder) holder).pick_icon.setVisibility(View.VISIBLE);
                ((holder) holder).pick.setVisibility(View.VISIBLE);
            } else {
                ((holder) holder).pick_icon.setVisibility(View.GONE);
                ((holder) holder).pick.setVisibility(View.GONE);
            }
            ((holder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), TeamDetailActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("detail", item.getDetail());
                    intent.putExtra("pick", item.getPick());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        else if(holder instanceof footer){

        }
    }

    @Override
    public int getItemCount() {
        return items_list.size()+1;
    }


    public class holder extends RecyclerView.ViewHolder {
        TextView title;
        TextView name;
        TextView detail;
        TextView pick;
        TextView team;
        CardView cardView;
        ImageView pick_icon;

        public holder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            name=(TextView)itemView.findViewById(R.id.name);
            detail=(TextView)itemView.findViewById(R.id.detail);
            pick=(TextView)itemView.findViewById(R.id.pick);
            team=(TextView)itemView.findViewById(R.id.team);
            pick_icon=(ImageView)itemView.findViewById(R.id.pick_icon);
            cardView=(CardView)itemView.findViewById(R.id.cardview);
        }//ViewHolder
    }

    public class footer extends RecyclerView.ViewHolder{

        public footer(View itemView) {
            super(itemView);
        }
    }
}
