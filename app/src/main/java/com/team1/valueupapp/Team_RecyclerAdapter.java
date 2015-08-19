package com.team1.valueupapp;

import android.content.Context;
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

        holder.title.setText(item.getTitle());
        holder.name.setText(item.getName());
        holder.detail.setText(item.getDetail());
        holder.pick.setText(item.getName() + " 소속");
        holder.team.setText(item.getTeam() + " 명");
        if(item.getPick().length()>0) {
            holder.pick_icon.setVisibility(View.VISIBLE);
            holder.pick.setVisibility(View.VISIBLE);
        }
        else {
            holder.pick_icon.setVisibility(View.GONE);
            holder.pick.setVisibility(View.GONE);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items_list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView name;
        TextView detail;
        TextView pick;
        TextView team;
        CardView cardView;
        ImageView pick_icon;

        public ViewHolder(View itemView) {
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
}
