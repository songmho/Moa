package com.team1.valueupapp;

import android.support.v7.widget.RecyclerView;

/**
 * Created by songmho on 2015-06-30.
 */
public class ListRecyclerItem {
    String app_name;
    String name;
    boolean star;
    byte[] profile;
    int job;
    String memo;
    RecyclerView recyclerView;

    byte[] getProfile(){return  this.profile;}
    String getApp_name(){
        return this.app_name;
    }
    String getName(){
        return this.name;
    }
    String getMemo(){
        return this.memo;
    }
    boolean getStar(){
        return this.star;
    }
    void setStar(boolean star){
        this.star=star;
    }
    int getJob(){return this.job;}
    RecyclerView getRecyclerView(){return this.recyclerView;}

    public ListRecyclerItem(byte[] profile, String app_name, String name, boolean star,int job, RecyclerView recyclerView) {
        this.profile=profile;
        this.app_name=app_name;
        this.name=name;
        this.star=star;
        this.job=job;
        this.recyclerView=recyclerView;
    }

    public ListRecyclerItem(byte[] profile, String app_name, String name, boolean star,int job, String memo, RecyclerView recyclerView) {
        this.profile=profile;
        this.app_name=app_name;
        this.name=name;
        this.star=star;
        this.job=job;
        this.memo=memo;
        this.recyclerView=recyclerView;
    }
}
