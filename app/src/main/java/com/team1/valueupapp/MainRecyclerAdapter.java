package com.team1.valueupapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by songmho on 15. 8. 19.
 */
public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<MainListitem> items;
    int layout;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public MainRecyclerAdapter(Context context, List<MainListitem> items, int layout) {
        this.context=context;
        this.items=items;
        this.layout=layout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mainlist_name, parent, false);
            return new holder(v);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mainlist_title, parent, false);
            return new header(v);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof holder) {
            //cast holder to VHItem and set data
            MainListitem item=items.get(position-1);
            ((holder) holder).name.setText(item.getName());
            switch (item.getJob()){
                case "plan":
                    ((holder) holder).color.setImageResource(R.color.planner);
                    break;
                case "dev":
                    ((holder) holder).color.setImageResource(R.color.developer);
                    break;
                case "dis":
                    ((holder) holder).color.setImageResource(R.color.designer);
                    break;
            }

        } else if (holder instanceof header) {
            //cast holder to VHHeader and set data for header.
        }
    }

    @Override
    public int getItemCount() {
        return items.size()+1;
    }
    @Override
    public int getItemViewType(int position) {
        if (position==0)
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    public class holder extends RecyclerView.ViewHolder{
        TextView name;
        de.hdodenhof.circleimageview.CircleImageView color;
        public holder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            color=(CircleImageView)itemView.findViewById(R.id.color);

        }
    }

    public class header extends RecyclerView.ViewHolder{
        TextView title;
        public header(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
        }
    }
}
