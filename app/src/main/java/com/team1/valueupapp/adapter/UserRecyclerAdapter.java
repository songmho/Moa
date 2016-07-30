package com.team1.valueupapp.adapter;

import android.app.Activity;
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
import com.team1.valueupapp.activity.MemberActivity;
import com.team1.valueupapp.activity.SendMsgActivity;
import com.team1.valueupapp.activity.UserDetailActivity;
import com.team1.valueupapp.item.UserItem;

import java.util.List;


/**
 * Created by hyemi on 2015-08-16.
 */
public class UserRecyclerAdapter extends RecyclerView.Adapter {
    Context context;
    List<UserItem> itemList;
    int itemLayout;
    int HOLDER = 0;
    int FOOTER = 1;

    public static final String TAG = "UserRecyclerAdapter";

    public UserRecyclerAdapter(Context context, List<UserItem> items, int itemLayout) {
        this.context = context;
        this.itemList = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < itemList.size())
            return HOLDER;
        else if (position == itemList.size())
            return FOOTER;
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HOLDER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
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
            final UserItem item = itemList.get(position);
            ((holder) holder).name.setText(item.getName());
            ((holder) holder).info.setText(item.getInfo());
            ((holder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent;
                    if(itemLayout==R.layout.activity_member){
                        intent = new Intent(context,SendMsgActivity.class);
                        intent.putExtra("objectId",item.getObjId()); //objectId
                        intent.putExtra("name",item.getName());  //name
                        intent.putExtra("eMail",item.getInfo());  //email
                        ((MemberActivity)context).setResult(Activity.RESULT_OK,intent);
                        ((MemberActivity)context).finish();

                    }
                    else{
                        Log.d("testest","d");
                        intent = new Intent(context.getApplicationContext(), UserDetailActivity.class);
                        intent.putExtra("objectId",item.getObjId()); //objectId
                        intent.putExtra("info", item.getInfo());
                        intent.putExtra("name", item.getName());
                        intent.putExtra("username", item.getUserName());
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class holder extends RecyclerView.ViewHolder {
        TextView info;
        TextView name;
        CardView cardView;

        public holder(View itemView) {
            super(itemView);
            info = (TextView) itemView.findViewById(R.id.info);
            name = (TextView) itemView.findViewById(R.id.name);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }//ViewHolder
    }

    public class footer extends RecyclerView.ViewHolder {

        public footer(View itemView) {
            super(itemView);
        }
    }
}
