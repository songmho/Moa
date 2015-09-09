package com.team1.valueupapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by songmho on 2015-06-30.
 */
public class RecyclerAdpater extends RecyclerView.Adapter<RecyclerAdpater.ViewHolder> {
    Context context;
    List<ListRecyclerItem> items_list;
    int itemLayout;
    int frag;

    RecyclerAdpater(Context context, List<ListRecyclerItem> items, int itemLayout, int frag) {
        this.context=context;
        this.items_list=items;
        this.itemLayout=itemLayout;
        this.frag=frag;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        switch (itemLayout) {
            case R.layout.item_listrecycler:                                 //소개페이지의 경우
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_listrecycler, viewGroup, false);
                return new ViewHolder(v, itemLayout);
            case R.layout.item_interest:                                 //관심멤버페이지의 경우
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_interest, viewGroup, false);
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
            case R.layout.item_listrecycler:                                 //소개페이지의 경우
                final ListRecyclerItem item_list = items_list.get(i);

                if(item_list.getProfile()==null) {
                    viewHolder.profile.setImageResource(R.drawable.ic_user);
                }
                else{
                    Bitmap bitmap = BitmapFactory.decodeByteArray(item_list.getProfile(), 0, item_list.getProfile().length);
                    viewHolder.profile.setImageBitmap(bitmap);
                }
                viewHolder.itemView.setTag(item_list);
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
            case R.layout.item_interest:                                 //관심멤버페이지의 경우
                final ListRecyclerItem interest_list = items_list.get(i);

                if(interest_list.getProfile()==null) {
                    viewHolder.profile.setImageResource(R.drawable.ic_user);
                }
                else{
                    Bitmap bitmap = BitmapFactory.decodeByteArray(interest_list.getProfile(), 0, interest_list.getProfile().length);
                    viewHolder.profile.setImageBitmap(bitmap);
                }
                viewHolder.itemView.setTag(interest_list);
                viewHolder.name.setText(interest_list.getName());
                viewHolder.app_name.setText(interest_list.getApp_name());
                viewHolder.memo.setText(interest_list.getMemo());

                viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent goto_info = new Intent(context.getApplicationContext(), InfoActivity.class);
                        goto_info.putExtra("cur_job", interest_list.getJob());
                        goto_info.putExtra("name", interest_list.getName());
                        goto_info.putExtra("profile", interest_list.getProfile());
                        goto_info.putExtra("idea", interest_list.getApp_name());
                        context.startActivity(goto_info);
                    }
                });

                break;
        }
    }


    private void checkStar(final ListRecyclerItem item_list, final ViewHolder viewHolder) {

        if(ParseUser.getCurrentUser()!=null) {

            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereEqualTo("name", item_list.getName());
            parseQuery.whereEqualTo("info", item_list.getApp_name());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                final ParseRelation<ParseUser> relation = ParseUser.getCurrentUser().getRelation("my_pick");

                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    for (int i = 0; i < list.size(); i++) {
                        final ParseUser user = list.get(i);

                        ParseQuery<ParseUser> query = relation.getQuery();
                        query.whereContains("objectId", user.getObjectId());
                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> list, ParseException e) {
                                Snackbar snackbar;

                                if (!list.isEmpty()) {
                                    item_list.setStar(false);
                                    viewHolder.star.setSelected(false);
                                    snackbar = Snackbar.make(item_list.getRecyclerView(), "관심멤버에서 제외합니다.", Snackbar.LENGTH_LONG);
                                    relation.remove(user);
                                    ParseUser.getCurrentUser().saveInBackground();

                                    ParseQuery<ParseObject> picked_query = ParseQuery.getQuery("Picked");
                                    picked_query.whereEqualTo("user", user);
                                    picked_query.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> list, ParseException e) {
                                            if (!list.isEmpty()) {
//                                Log.d("sss",list.size()+"");
                                                ParseRelation<ParseUser> picked_relation = list.get(0).getRelation("picked");
                                                picked_relation.remove(ParseUser.getCurrentUser());
                                                list.get(0).saveInBackground();
                                            }
                                        }
                                    });

                                    snackbar.setAction("실행취소", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            item_list.setStar(true);
                                            viewHolder.star.setSelected(true);
                                            relation.add(user);
                                            ParseUser.getCurrentUser().saveInBackground();

                                            ParseQuery<ParseObject> picked_query = ParseQuery.getQuery("Picked");
                                            picked_query.whereEqualTo("user", user);
                                            picked_query.findInBackground(new FindCallback<ParseObject>() {
                                                @Override
                                                public void done(List<ParseObject> list, ParseException e) {
                                                    if (!list.isEmpty()) {
//                                Log.d("sss",list.size()+"");
                                                        ParseRelation<ParseUser> picked_relation = list.get(0).getRelation("picked");
                                                        picked_relation.add(ParseUser.getCurrentUser());
                                                        list.get(0).saveInBackground();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                    snackbar.show();

                                } else {
                                    item_list.setStar(true);
                                    viewHolder.star.setSelected(true);
                                    snackbar = Snackbar.make(item_list.getRecyclerView(), "관심멤버에 추가합니다.", Snackbar.LENGTH_LONG);
                                    relation.add(user);
                                    ParseUser.getCurrentUser().saveInBackground();
                                    ParseQuery<ParseObject> picked_query = ParseQuery.getQuery("Picked");

                                    picked_query.whereEqualTo("user", user);
                                    picked_query.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> list, ParseException e) {
                                            if (!list.isEmpty()) {
//                                Log.d("sss",list.size()+"");
                                                ParseRelation<ParseUser> picked_relation = list.get(0).getRelation("picked");
                                                picked_relation.add(ParseUser.getCurrentUser());
                                                list.get(0).saveInBackground();
                                            }
                                        }
                                    });

                                    snackbar.setAction("실행취소", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            item_list.setStar(false);
                                            viewHolder.star.setSelected(false);
                                            relation.remove(user);
                                            ParseUser.getCurrentUser().saveInBackground();
                                            ParseQuery<ParseObject> picked_query = ParseQuery.getQuery("Picked");
                                            picked_query.whereEqualTo("user", user);
                                            picked_query.findInBackground(new FindCallback<ParseObject>() {
                                                @Override
                                                public void done(List<ParseObject> list, ParseException e) {
                                                    if (!list.isEmpty()) {
//                                Log.d("sss",list.size()+"");
                                                        ParseRelation<ParseUser> picked_relation = list.get(0).getRelation("picked");
                                                        picked_relation.remove(ParseUser.getCurrentUser());
                                                        list.get(0).saveInBackground();
                                                    }
                                                }
                                            });
                                        }
                                    });
                                    snackbar.show();

                                }//end else
                            }
                        });


//                    if(ParseUser.getCurrentUser()!=null) {
//
//                    }
//                    else{
//                        Toast.makeText(context.getApplicationContext(),"로그인이 필요합니다.",Toast.LENGTH_SHORT).show();
//                        context.startActivity(new Intent(context.getApplicationContext(),LoginActivity.class));
//                    }
                    }//end for
                }
            });
        }//end if


    }//checkStar

    @Override
    public int getItemCount() {
        switch (itemLayout) {
            case R.layout.item_listrecycler:
                return items_list.size();
            case R.layout.item_interest:
                return items_list.size();
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

        CardView cardview;
        TextView memo;

        public ViewHolder(View itemView,int itemLayout) {
            super(itemView);
            switch (itemLayout) {
                case R.layout.item_listrecycler:                                //소개페이지의 경우
                    profile=(CircleImageView)itemView.findViewById(R.id.profile);
                    app_name = (TextView) itemView.findViewById(R.id.app_name);
                    name = (TextView) itemView.findViewById(R.id.name);
                    star = (ImageButton) itemView.findViewById(R.id.star);
                    detail = (TextView) itemView.findViewById(R.id.detail);
                    container =(LinearLayout)itemView.findViewById(R.id.container);
                    star.setSelected(false);
                    break;
                case R.layout.item_interest:                                //관심멤버페이지의 경우
                    profile=(CircleImageView)itemView.findViewById(R.id.profile);
                    app_name = (TextView) itemView.findViewById(R.id.app_name);
                    name = (TextView) itemView.findViewById(R.id.name);
                    memo = (TextView) itemView.findViewById(R.id.memo);
                    cardview = (CardView)itemView.findViewById(R.id.cardview);
                    break;
            }
        }
    }
}
