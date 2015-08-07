package com.team1.valueupapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by eugene on 2015-08-07.
 */
public class Mentor_Adapter extends RecyclerView.Adapter<Mentor_Adapter.ViewHolder> {
        Context context;
        List<Mentor_item> items;
        int rayout;

        public Mentor_Adapter(Context context, List<Mentor_item> items, int rayout) {
            this.context=context;
            this.items=items;
            this.rayout=rayout;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mentor, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Mentor_item item=items.get(position);


            holder.mentor_name.setText(item.getMentor_name());
            holder.mentor_field.setText(item.getMentor_filed());

        }


        @Override
        public int getItemCount() {
            return items.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView mentor_name;
            TextView mentor_field;
            TextView company;
            TextView email;



            public ViewHolder(View itemView) {
                super(itemView);
                mentor_name=(TextView)itemView.findViewById(R.id.mentor_name);
                mentor_field=(TextView)itemView.findViewById(R.id.mentor_field);

            }
        }
    }

