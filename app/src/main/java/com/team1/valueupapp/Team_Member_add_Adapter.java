package com.team1.valueupapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by songmho on 15. 8. 20.
 */
public class Team_Member_add_Adapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<Team_Member_add_item> items;
    ArrayList<Team_Member_add_item> pick_items=new ArrayList<>();
    public Team_Member_add_Adapter(Context context, ArrayList<Team_Member_add_item> items) {
        this.context=context;
        this.items=items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_add,parent,false);
        return new holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Team_Member_add_item item=items.get(position);
                ((holder) holder).profile.setImageResource(R.drawable.ic_user);
        ((holder)holder).name.setText(item.getName());
        ((holder)holder).checkBox.setChecked(item.isCheck());
        ((holder)holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    ParseQuery<ParseObject> query=ParseQuery.getQuery("ValueUp_team");
                    query.whereEqualTo("admin_member", ParseUser.getCurrentUser().getString("name"));
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            list.get(0).getList("member").add(item.getName());
                            list.get(0).saveInBackground();
                        }
                    });
                    Toast.makeText(context.getApplicationContext(),item.getName(),Toast.LENGTH_SHORT).show();
                }
                else{
//                    pick_items.remove(item);
                    ParseQuery<ParseObject> query=ParseQuery.getQuery("ValueUp_team");
                    query.whereEqualTo("admin_member", ParseUser.getCurrentUser().getString("name"));
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            list.get(0).getList("member").remove(item.getName());
                            list.get(0).saveInBackground();
                        }
                    });
                    Toast.makeText(context.getApplicationContext(),item.getName(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public ArrayList<Team_Member_add_item> getPick_items() {
        return pick_items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        CircleImageView profile;
        TextView name;
        CheckBox checkBox;
        public holder(View itemView) {
            super(itemView);
            profile=(CircleImageView)itemView.findViewById(R.id.profile);
            name=(TextView)itemView.findViewById(R.id.name);
            checkBox=(CheckBox)itemView.findViewById(R.id.check);
        }
    }
}
