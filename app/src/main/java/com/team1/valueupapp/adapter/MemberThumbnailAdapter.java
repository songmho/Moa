package com.team1.valueupapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.team1.valueupapp.R;
import com.team1.valueupapp.activity.UserDetailActivity;
import com.team1.valueupapp.item.UserItem;

import java.util.List;

/**
 * Created by knulps on 16. 4. 20..
 */
public class MemberThumbnailAdapter extends RecyclerView.Adapter {
    Context context;
    List<UserItem> itemList;

    public static final String TAG = "MemberThumbnailAdapter";

    public MemberThumbnailAdapter(Context context, List<UserItem> items) {
        this.context = context;
        this.itemList = items;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_thumbnail, parent, false);
        return new holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final UserItem item = itemList.get(position);
        ((holder) holder).name.setText(item.getName());
        ((holder) holder).layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 16. 4. 20. 클릭시 멤버 상세 페이지로 이동
                Intent intent = new Intent(context.getApplicationContext(), UserDetailActivity.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("username", item.getUserName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class holder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView profile;
        LinearLayout layout;

        public holder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_member);
            profile = (ImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
        }//ViewHolder
    }
}


