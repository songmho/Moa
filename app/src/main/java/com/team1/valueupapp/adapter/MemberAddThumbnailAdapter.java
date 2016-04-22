package com.team1.valueupapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.team1.valueupapp.R;
import com.team1.valueupapp.activity.TeamDetailActivity;
import com.team1.valueupapp.activity.UserDetailActivity;
import com.team1.valueupapp.item.UserItem;

import java.util.List;

/**
 * Created by knulps on 16. 4. 20..
 */
public class MemberAddThumbnailAdapter extends RecyclerView.Adapter {
    Context context;
    List<UserItem> itemList;

    public static final String TAG = "MemberThumbnailAdapter";

    public MemberAddThumbnailAdapter(Context context, List<UserItem> items) {
        this.context = context;
        this.itemList = items;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_add_thumbnail, parent, false);
        return new holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final UserItem item = itemList.get(position);
        ((holder) holder).name.setText(item.getName());
        ((holder) holder).layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), UserDetailActivity.class);
                intent.putExtra("name", item.getName());
                intent.putExtra("username", item.getUserName());
                intent.putExtra("info", item.getInfo());
                context.startActivity(intent);
            }
        });
        ((holder) holder).btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereEqualTo("username", item.getUserName());
                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        if (list != null) {
                            final ParseUser user = list.get(0);
                            String teamName = ((TeamDetailActivity) context).getTeamName();
                            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Team");
                            parseQuery.whereEqualTo("intro", teamName);
                            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list, ParseException e) {
                                    if (list != null) {
                                        ParseObject o = list.get(0);      //조건에 맞는 오브젝트 찾음
                                        ParseRelation<ParseUser> member = o.getRelation("member");        // 신청자 현황 릴레이션 불러옴
                                        member.add(user);
                                        ParseRelation<ParseUser> memberWaiting = o.getRelation("member_doing");
                                        memberWaiting.remove(user);
                                        o.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                Log.e(TAG, "멤버 변동사항 저장 완료");
                                                Toast.makeText(context, "참여 수락되었습니다.", Toast.LENGTH_SHORT).show();
                                                ((TeamDetailActivity) context).initDataAndView();
                                            }
                                        });

                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
        ((holder) holder).btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
                parseQuery.whereEqualTo("username", item.getUserName());
                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> list, ParseException e) {
                        if (list != null) {
                            final ParseUser user = list.get(0);
                            String teamName = ((TeamDetailActivity) context).getTeamName();
                            ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("Team");
                            parseQuery.whereEqualTo("intro", teamName);
                            parseQuery.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> list, ParseException e) {
                                    if (list != null) {
                                        ParseObject o = list.get(0);      //조건에 맞는 오브젝트 찾음
                                        ParseRelation<ParseUser> memberWaiting = o.getRelation("member_doing");
                                        memberWaiting.remove(user);
                                        o.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                Log.e(TAG, "멤버 변동사항 저장 완료");
                                                Toast.makeText(context, "참여 거절되었습니다.", Toast.LENGTH_SHORT).show();
                                                ((TeamDetailActivity) context).initDataAndView();
                                            }
                                        });

                                    }
                                }
                            });
                        }
                    }
                });
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
        RelativeLayout layout;
        Button btnAccept, btnDeny;

        public holder(View itemView) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.layout_member);
            profile = (ImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
            btnAccept = (Button) itemView.findViewById(R.id.accept);
            btnDeny = (Button) itemView.findViewById(R.id.deny);
        }//ViewHolder
    }
}


