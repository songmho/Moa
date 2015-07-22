package com.team1.valueupapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by songmho on 2015-06-30.
 */
public class RecyclerAdpater extends RecyclerView.Adapter<RecyclerAdpater.ViewHolder> {
    Context context;
    List<ListRecyclerItem> items_list;
    List<Grid_Item> items_grid;
    List<TeamlistItem> items_team;
    int itemLayout;
    int frag;

    RecyclerAdpater(Context context, List<Grid_Item> items, int itemLayout) {
        this.context=context;
        this.items_grid=items;
        this.itemLayout=itemLayout;
    }
    RecyclerAdpater(Context context, List<ListRecyclerItem> items, int itemLayout, int frag) {
        this.context=context;
        this.items_list=items;
        this.itemLayout=itemLayout;
        this.frag=frag;
    }

    public RecyclerAdpater(Context context, List<TeamlistItem> items, int item_teamlist, int i, int i1) {
        this.context=context;
        this.items_team=items;
        this.itemLayout=item_teamlist;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        switch (itemLayout) {
            case R.layout.item_listrecycler:
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_listrecycler, viewGroup, false);
            return new ViewHolder(v, itemLayout);
            case R.layout.item_grid:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_grid, viewGroup, false);
                return new ViewHolder(v, itemLayout);
            case R.layout.item_teamlist:
                v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_teamlist,viewGroup, false);
                return new ViewHolder(v,itemLayout);

        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        switch (itemLayout) {
            case R.layout.item_listrecycler:                                 //소개 및 관심멤버페이지의 경우
                final ListRecyclerItem item_list = items_list.get(i);
                viewHolder.itemView.setTag(item_list);
                viewHolder.profile.setImageResource(item_list.getProfile());
                viewHolder.name.setText(item_list.getName());
                viewHolder.app_name.setText(item_list.getApp_name());
                viewHolder.star.setSelected(item_list.getStar());

                viewHolder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent goto_info = new Intent(context.getApplicationContext(), InfoActivity.class);
                        goto_info.putExtra("cur_job", item_list.getJob());
                        goto_info.putExtra("name", item_list.getName());
                        goto_info.putExtra("profile", item_list.getProfile());
                        goto_info.putExtra("idea", item_list.getApp_name());
                        goto_info.putExtra("star", item_list.getStar());
                        context.startActivity(goto_info);
                    }
                });

                viewHolder.star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkStar(item_list, viewHolder);
                    }
                });
                break;

            case R.layout.item_grid:                                 //팀빌딩페이지의 경우
                final Grid_Item item_grid = items_grid.get(i);
                viewHolder.state.setText(item_grid.getState());
                viewHolder.idea.setText(item_grid.getIdea());
                viewHolder.plan.setText(String.valueOf(item_grid.getPlan()));
                viewHolder.max_plan.setText(String.valueOf(item_grid.getMax_plan()));
                viewHolder.develop.setText(String.valueOf(item_grid.getDevelop()));
                viewHolder.max_dev.setText(String.valueOf(item_grid.getMax_dev()));
                viewHolder.design.setText(String.valueOf(item_grid.getDesign()));
                viewHolder.max_dis.setText(String.valueOf(item_grid.getMax_dis()));
                viewHolder.idea_info.setText(item_grid.getIdea_info());

                viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent goto_team=new Intent(context.getApplicationContext(),TeamActivity.class);
                        goto_team.putExtra("idea",item_grid.getIdea());
                        context.startActivity(goto_team);

                    }
                });
                break;
            case R.layout.item_teamlist:
                final TeamlistItem item_team=items_team.get(i);
                viewHolder.name.setText(item_team.getName());
                viewHolder.job.setText(item_team.getJob());
                viewHolder.container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent goto_info = new Intent(context.getApplicationContext(), InfoActivity.class);
                        goto_info.putExtra("cur_job", item_team.getJob());
                        goto_info.putExtra("name", item_team.getName());
                        goto_info.putExtra("profile", item_team.getProfile());
                        context.startActivity(goto_info);

                    }
                });
                Log.d("heee", "" + items_team.size());
                break;
        }
    }


    private void checkStar(final ListRecyclerItem item_list, final ViewHolder viewHolder) {
        ParseQuery<ParseObject> query=ParseQuery.getQuery("ValueUp_people");
        query.whereEqualTo("name",item_list.getName());
        query.whereEqualTo("info",item_list.getApp_name());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for(int i=0;i<list.size();i++){
                    final ParseObject parseObject=list.get(i);

                    Snackbar snackbar;

                    if(parseObject.getList("pick").contains(ParseUser.getCurrentUser().get("name"))){
                       item_list.setStar(false);
                        viewHolder.star.setSelected(false);
                        snackbar=Snackbar.make(item_list.getRecyclerView(),"관심멤버에서 제외합니다.",Snackbar.LENGTH_LONG);
                        parseObject.getList("pick").remove(ParseUser.getCurrentUser().get("name"));
                        parseObject.saveInBackground();
                        snackbar.setAction("실행취소", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                item_list.setStar(true);
                                viewHolder.star.setSelected(true);
                                parseObject.getList("pick").add(ParseUser.getCurrentUser().get("name"));
                                parseObject.saveInBackground();
                            }
                        });
                        snackbar.show();

                    }
                    else{
                        item_list.setStar(true);
                        viewHolder.star.setSelected(true);
                        snackbar=Snackbar.make(item_list.getRecyclerView(),"관심멤버에 추가합니다.",Snackbar.LENGTH_LONG);
                        parseObject.getList("pick").add(ParseUser.getCurrentUser().get("name"));
                        parseObject.saveInBackground();
                        snackbar.setAction("실행취소", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                item_list.setStar(false);
                                viewHolder.star.setSelected(false);
                                parseObject.getList("pick").remove(ParseUser.getCurrentUser().get("name"));
                                parseObject.saveInBackground();
                            }
                        });
                        snackbar.show();

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        switch (itemLayout) {
            case R.layout.item_listrecycler:
            return items_list.size();
            case R.layout.item_grid:                                //팀빌딩페이지의 경우
                return  items_grid.size();
            case R.layout.item_teamlist:
                return 1;
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView app_name;
        TextView name;
        TextView detail;
        ImageButton star;
        CircleImageView profile;
        LinearLayout container;

        CardView cardView;

        TextView idea;
        TextView plan;
        TextView develop;
        TextView design;
        TextView state;
        TextView max_plan;
        TextView max_dev;
        TextView max_dis;
        TextView idea_info;

        TextView job;

        public ViewHolder(View itemView,int itemLayout) {
            super(itemView);
            switch (itemLayout) {
                case R.layout.item_listrecycler:                                //소개 및 관심멤버페이지의 경우
                    profile=(CircleImageView)itemView.findViewById(R.id.profile);
                    app_name = (TextView) itemView.findViewById(R.id.app_name);
                    name = (TextView) itemView.findViewById(R.id.name);
                    star = (ImageButton) itemView.findViewById(R.id.star);
                    detail = (TextView) itemView.findViewById(R.id.detail);
                    container =(LinearLayout)itemView.findViewById(R.id.container);
                    star.setSelected(false);
                    break;
                case R.layout.item_grid:                                //팀빌딩페이지의 경우
                    idea=(TextView)itemView.findViewById(R.id.idea);
                    state=(TextView)itemView.findViewById(R.id.state);
                    plan=(TextView)itemView.findViewById(R.id.plan);
                    max_plan=(TextView)itemView.findViewById(R.id.max_plan);
                    develop=(TextView)itemView.findViewById(R.id.develop);
                    max_dev=(TextView)itemView.findViewById(R.id.max_develop);
                    design=(TextView)itemView.findViewById(R.id.design);
                    max_dis=(TextView)itemView.findViewById(R.id.max_design);
                    idea_info=(TextView)itemView.findViewById(R.id.idea_info);
                    cardView=(CardView)itemView.findViewById(R.id.cardview);
                    break;
                case R.layout.item_teamlist:
                    container=(LinearLayout)itemView.findViewById(R.id.container);
                    profile=(CircleImageView)itemView.findViewById(R.id.profile);
                    name=(TextView)itemView.findViewById(R.id.name);
                    job=(TextView)itemView.findViewById(R.id.job);
                    break;
            }
        }
    }
}
