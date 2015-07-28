package com.team1.valueupapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hyemi on 2015-07-25.
 */
public class Add_RecyclerAdapter extends RecyclerView.Adapter<Add_RecyclerAdapter.ViewHolder> {
    Context context;
    List<Add_item> items_list;
    int itemLayout;

    Add_RecyclerAdapter(Context context, List<Add_item> items, int itemLayout) {
        this.context=context;
        this.items_list=items;
        this.itemLayout=itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_recycler, viewGroup, false);

        return new ViewHolder(v);

    }//onCreateViewHolder

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Add_item item_list = items_list.get(i);


        viewHolder.itemView.setTag(item_list);
        viewHolder.profile.setImageResource(item_list.getProfile());
        viewHolder.name.setText(item_list.getName());
        viewHolder.check.setSelected(item_list.getCheck());
        if(item_list.getCheck())
            ((AddActivity)context).addMembers(item_list.getName());
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMember(item_list, viewHolder);
            }
        });
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }//onBindViewHolder

    private void selectMember(Add_item item_list, final ViewHolder viewHolder) {
        if (!viewHolder.check.isSelected()) {
            viewHolder.check.setSelected(true);
            ((AddActivity)context).addMembers(item_list.getName());
        } else {
            viewHolder.check.setSelected(false);
            ((AddActivity)context).delMembers(item_list.getName());
        }//end else
    }//selectMember

    @Override
    public int getItemCount() {
        return items_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        ImageButton check;
        CircleImageView profile;
        LinearLayout container;

        TextView category;
        LinearLayout categ_header;

        public ViewHolder(View itemView) {
            super(itemView);
                    profile = (CircleImageView) itemView.findViewById(R.id.profile);
                    name = (TextView) itemView.findViewById(R.id.name);
                    check = (ImageButton) itemView.findViewById(R.id.check);
                    container = (LinearLayout) itemView.findViewById(R.id.container);
                    check.setSelected(false);
        }//ViewHolder

    }//ViewHolder class

}//class