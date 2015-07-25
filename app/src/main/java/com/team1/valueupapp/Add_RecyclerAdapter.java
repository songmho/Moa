package com.team1.valueupapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyemi on 2015-07-25.
 */
public class Add_RecyclerAdapter extends RecyclerView.Adapter<Add_RecyclerAdapter.ViewHolder> {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_recycler, viewGroup, false);
                return new ViewHolder(v);

    }//onCreateViewHolder

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageButton check;
        CircleImageView profile;
        LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            profile=(CircleImageView)itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
            check = (ImageButton) itemView.findViewById(R.id.check);
            container =(LinearLayout)itemView.findViewById(R.id.container);
            check.setSelected(false);
        }
    }//ViewHolder class

}//class