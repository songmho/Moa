package com.team1.valueupapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by songmho on 15. 8. 4.
 */
public class Mentoring_adapter extends RecyclerView.Adapter<Mentoring_adapter.ViewHolder> {
    Context context;
    List <Mentoring_item> items;
    int rayout;

    public Mentoring_adapter(Context context, List<Mentoring_item> items, int rayout) {
        this.context=context;
        this.items=items;
        this.rayout=rayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mentoring, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mentoring_item item=items.get(position);

        holder.job.setText("["+item.getJob()+"]");
        holder.date.setText(""+ item.getYear()+"."+item.getMonth()+"."+item.getDay());
        holder.title.setText(item.getTitle());
        holder.mentor.setText(item.getMentor());
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView job;
        TextView date;
        TextView title;
        TextView mentor;

        public ViewHolder(View itemView) {
            super(itemView);
            job=(TextView)itemView.findViewById(R.id.job);
            date=(TextView)itemView.findViewById(R.id.date);
            title=(TextView)itemView.findViewById(R.id.title);
            mentor=(TextView)itemView.findViewById(R.id.mentor);
        }
    }
}
