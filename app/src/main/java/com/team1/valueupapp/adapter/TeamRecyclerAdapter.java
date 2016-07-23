package com.team1.valueupapp.adapter;

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
import com.team1.valueupapp.item.TeamItem;
import com.team1.valueupapp.activity.TeamDetailActivity;

import java.util.List;


/**
 * Created by hyemi on 2015-08-16.
 */
public class TeamRecyclerAdapter extends RecyclerView.Adapter {
    Context context;
    List<TeamItem> items_list;
    int myListSize;
    int itemLayout;
    int HEADER = 0;
    int HOLDER = 1;
    int FOOTER = 2;

    public static final String TAG = "TeamRecyclerAdapter";

    public TeamRecyclerAdapter(Context context, List<TeamItem> items, int itemLayout, int myListSize) {
        this.context = context;
        this.items_list = items;
        this.itemLayout = itemLayout;
        this.myListSize = myListSize;
        Log.d("dfadsf",""+items.size());
    }

    @Override
    public int getItemViewType(int position) {
        if(myListSize==-1)
            return HOLDER;
        if(myListSize>0){
            if(position ==0 || position==myListSize+1)
                return  HEADER;
            else if(position>0 && position<=items_list.size()+1)
                return HOLDER;
            else
                return FOOTER;
        }
        else if(myListSize==0){
            if(position==0)
                return HEADER;
            else if(position>0 && position<=items_list.size())
                return HOLDER;
            else
                return FOOTER;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(viewType == HEADER){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            Log.e("fdfdfd","1");
            return new header(v);
        }
        else if (viewType == HOLDER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_team, parent, false);
            Log.e("fdfdfd","2");
            return new holder(v);
        } else if (viewType == FOOTER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fab_footer, parent, false);
            return new footer(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof header){
                ((header)holder).header.setText("추천 그룹");
            if(myListSize>0 && position == 0)
                ((header)holder).header.setText("내 그룹");
            Log.e("fdfdfd","3");
        }
        else if (holder instanceof holder) {
            int curPosition = position-1;   //헤더가 있을 경우 생각하여 position을 -1 시킴
            if(myListSize==-1)      //헤더가 없는 경우(검색에서 사용한 경우)에는 연산 필요없음(+1시킴)
                curPosition+=1;
            if(myListSize>0 && position>myListSize+1)   //로그인 하고 내 그룹이 있는 경우 -1 더 시킴
                curPosition-=1;
            final TeamItem item = items_list.get(curPosition);
            Log.e("fdfdfd",item.getUsername());
            ((holder) holder).title.setText(item.getTitle());
            ((holder) holder).detail.setText(item.getDetail());

            ((holder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(), TeamDetailActivity.class);
                    intent.putExtra("objId",item.getObjId());
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("name", item.getName());
                    intent.putExtra("username", item.getUsername());
                    intent.putExtra("detail", item.getDetail());
                    context.startActivity(intent);
                }
            });
        } else if (holder instanceof footer) {

        }
    }

    @Override
    public int getItemCount() {
        if(myListSize>0)
            return items_list.size()+ 3;
        else if(myListSize==0)
            return items_list.size()+2;

        return items_list.size();
    }


    public class holder extends RecyclerView.ViewHolder {
        TextView title;
        TextView name;
        TextView detail;
        CardView cardView;

        public holder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            name = (TextView) itemView.findViewById(R.id.name);
            detail = (TextView) itemView.findViewById(R.id.detail);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
        }//ViewHolder
    }

    public class footer extends RecyclerView.ViewHolder {

        public footer(View itemView) {
            super(itemView);
        }
    }

    private class header extends RecyclerView.ViewHolder {
        TextView header;
        public header(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.header);
        }
    }
}
