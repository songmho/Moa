package com.team1.valueupapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team1.valueupapp.R;
import com.team1.valueupapp.item.TeamItem;
import com.team1.valueupapp.activity.TeamDetailActivity;

import java.util.List;


/**
 * Created by hyemi on 2015-08-16.
 */
public class TeamRecyclerAdapter extends RecyclerView.Adapter {
    Context context;
    List<TeamItem> items_list;
    int itemLayout;
    int HOLDER = 0;
    int FOOTER = 1;

    public static final String TAG = "TeamRecyclerAdapter";

    public TeamRecyclerAdapter(Context context, List<TeamItem> items, int itemLayout) {
        this.context = context;
        this.items_list = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public int getItemViewType(int position) {
//        if (position < items_list.size())
            return HOLDER;
//        else if (position == items_list.size())
//            return FOOTER;
//        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HOLDER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
            return new holder(v);
        } else if (viewType == FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fab_footer, parent, false);
            return new footer(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof holder) {
            final TeamItem item = items_list.get(position);

            ((holder) holder).title.setText(item.getTitle());
            ((holder) holder).detail.setText(item.getDetail());

            ((holder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), TeamDetailActivity.class);
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("username", item.getUsername());
                    intent.putExtra("detail", item.getDetail());
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof footer) {

        }
    }

    @Override
    public int getItemCount() {
        return items_list.size() /*+ 1*/;
    }


    public class holder extends RecyclerView.ViewHolder {
        TextView title;
        TextView name;
        TextView detail;
        CardView cardView;

        public holder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            name = (TextView) itemView.findViewById(R.id.name);
            detail = (TextView) itemView.findViewById(R.id.detail);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }//ViewHolder
    }

    public class footer extends RecyclerView.ViewHolder {

        public footer(View itemView) {
            super(itemView);
        }
    }
}
