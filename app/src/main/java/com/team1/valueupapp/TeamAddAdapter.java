package com.team1.valueupapp;

import android.content.Context;
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
    private static final int TYPE_FOOTER = Integer.MIN_VALUE + 1;
    private static final int TYPE_ITEM = 1;

    public TeamAddAdapter(Context context, ArrayList<Teamadd_item> items) {
        this.context=context;
        this.items=items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType == TYPE_ITEM){
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teamadd,parent,false);
            return new holder(v);
        }
        else if(viewType==TYPE_FOOTER){
            v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teamadd_footer,parent,false);
            return new footer(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==TYPE_ITEM){
            Teamadd_item item=items.get(position);
            if(item.getProfile()==null)
                ((holder)holder).profile.setImageResource(R.drawable.ic_user);
            ((holder) holder).name.setText(item.getName());
        }
        else if(getItemViewType(position)==TYPE_FOOTER){
                    ((footer) holder).addteam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context.getApplicationContext(),"dfdfd",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return items.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position<items.size())
            return TYPE_ITEM;
        else if (position==items.size())
            return TYPE_FOOTER;
        return 0;
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

    public class footer extends RecyclerView.ViewHolder{
        Button addteam;

        public footer(View itemView) {
            super(itemView);
            addteam=(Button)itemView.findViewById(R.id.addteam);
        }
    }
}
