package com.team1.valueupapp.item;

import android.support.v7.widget.RecyclerView;

/**
 * Created by songmho on 2015-06-30.
 */
public class ListRecyclerItem {
    String app_name;
    String name;
    byte[] profile;
    int job;
    String memo;
    RecyclerView recyclerView;

    public byte[] getProfile() {
        return this.profile;
    }

    public String getApp_name() {
        return this.app_name;
    }

    public String getName() {
        return this.name;
    }

    public String getMemo() {
        return this.memo;
    }

    public int getJob() {
        return this.job;
    }

    public RecyclerView getRecyclerView() {
        return this.recyclerView;
    }

    public ListRecyclerItem(byte[] profile, String app_name, String name, int job, RecyclerView recyclerView) {
        this.profile = profile;
        this.app_name = app_name;
        this.name = name;
        this.job = job;
        this.recyclerView = recyclerView;
    }

    public ListRecyclerItem(byte[] profile, String app_name, String name, int job, String memo, RecyclerView recyclerView) {
        this.profile = profile;
        this.app_name = app_name;
        this.name = name;
        this.job = job;
        this.memo = memo;
        this.recyclerView = recyclerView;
    }
}
