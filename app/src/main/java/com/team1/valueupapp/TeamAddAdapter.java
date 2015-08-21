package com.team1.valueupapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by songmho on 15. 8. 20.
 */
public class TeamAddAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Teamadd_item> items;
    Context context;
    public TeamAddAdapter(Context context, ArrayList<Teamadd_item> items) {
        this.context=context;
        this.items=items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teamadd,parent,false);
            return new holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Teamadd_item item=items.get(position);
        if(item.getProfile()==null)
            ((holder)holder).profile.setImageResource(R.drawable.ic_user);
        else{
            Bitmap bitmap = BitmapFactory.decodeByteArray(item.getProfile(), 0, item.getProfile().length);
            ((holder)holder).profile.setImageBitmap(bitmap);
        }
        ((holder) holder).name.setText(item.getName());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        CircleImageView profile;
        TextView name;

        public holder(View itemView) {
            super(itemView);
            profile=(CircleImageView)itemView.findViewById(R.id.profile);
            name=(TextView)itemView.findViewById(R.id.name);
        }
    }
}
