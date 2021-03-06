package com.team1.valueupapp.adapter;

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
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.team1.valueupapp.R;
import com.team1.valueupapp.item.TeamMemberAddItem;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by songmho on 15. 8. 20.
 */
public class TeamMemberAddAdapter extends RecyclerView.Adapter {
    Context context;
    ArrayList<TeamMemberAddItem> items;
    ArrayList<TeamMemberAddItem> pick_items = new ArrayList<>();

    public TeamMemberAddAdapter(Context context, ArrayList<TeamMemberAddItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member_add, parent, false);
        return new holder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TeamMemberAddItem item = items.get(position);
        ((holder) holder).profile.setImageResource(R.drawable.ic_user);
        ((holder) holder).name.setText(item.getName());
        ((holder) holder).checkBox.setChecked(item.isCheck());
        ((holder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    final ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            for (final ParseObject o : list) {
                                ParseUser user = o.getParseUser("admin_member");
                                if (user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                                    final ParseRelation<ParseUser> parseUsers = o.getRelation("member");
                                    ParseQuery<ParseUser> q = ParseUser.getQuery();
                                    q.whereEqualTo("objectId", item.getObjId());
                                    q.getFirstInBackground(new GetCallback<ParseUser>() {
                                        @Override
                                        public void done(ParseUser parseUser, ParseException e) {
                                            parseUsers.remove(parseUser);
                                            o.saveInBackground();
                                        }
                                    });
                                }
                            }
                        }
                    });
                    Toast.makeText(context.getApplicationContext(), item.getName(), Toast.LENGTH_SHORT).show();
                } else {
//                    pick_items.remove(item);
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> list, ParseException e) {
                            for (final ParseObject o : list) {
                                ParseUser user = o.getParseUser("admin_member");
                                if (user.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                                    final ParseRelation<ParseUser> parseUsers = o.getRelation("member");
                                    ParseQuery<ParseUser> q = ParseUser.getQuery();
                                    q.whereEqualTo("objectId", item.getObjId());
                                    q.getFirstInBackground(new GetCallback<ParseUser>() {
                                        @Override
                                        public void done(ParseUser parseUser, ParseException e) {
                                            parseUsers.add(parseUser);
                                            o.saveInBackground();
                                        }
                                    });
                                }
                            }
                        }
                    });
                    Toast.makeText(context.getApplicationContext(), item.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public ArrayList<TeamMemberAddItem> getPick_items() {
        return pick_items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class holder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView name;
        CheckBox checkBox;

        public holder(View itemView) {
            super(itemView);
            profile = (CircleImageView) itemView.findViewById(R.id.profile);
            name = (TextView) itemView.findViewById(R.id.name);
            checkBox = (CheckBox) itemView.findViewById(R.id.check);
        }
    }
}
