package com.team1.valueupapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyemi on 2015-07-25.
 */
public class Add_RecyclerAdapter extends RecyclerView.Adapter<Add_RecyclerAdapter.ViewHolder> {
    Context context;
    List<Add_item> items_list;
    int itemLayout;
    int frag;

    Add_RecyclerAdapter(Context context, List<Add_item> items, int itemLayout) {
        this.context=context;
        this.items_list=items;
        this.itemLayout=itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        switch (itemLayout) {
            case R.layout.add_category:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_category, viewGroup, false);
                return new ViewHolder(v);
            case R.layout.add_recycler:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_recycler, viewGroup, false);
                return new ViewHolder(v);
        }
        return null;
    }////onCreateViewHolder

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView category;
        LinearLayout categ_header;

        TextView name;
        ImageButton check;
        CircleImageView profile;
        LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            switch (itemLayout) {
                case R.layout.add_category:
                    category = (TextView) itemView.findViewById(R.id.category);
                    categ_header = (LinearLayout) itemView.findViewById(R.id.categ_header);
                case R.layout.add_recycler:
                    profile = (CircleImageView) itemView.findViewById(R.id.profile);
                    name = (TextView) itemView.findViewById(R.id.name);
                    check = (ImageButton) itemView.findViewById(R.id.check);
                    container = (LinearLayout) itemView.findViewById(R.id.container);
                    check.setSelected(false);
            }
        }//ViewHolder

    }//ViewHolder class

}//class