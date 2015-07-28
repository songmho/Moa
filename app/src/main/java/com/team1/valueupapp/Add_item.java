package com.team1.valueupapp;

import android.support.v7.widget.RecyclerView;

/**
 * Created by hyemi on 2015-07-25.
 */
public class Add_item {
    private String name;
    private boolean check;
    private int profile;
    private RecyclerView recyclerView;

    public String getName() {
        return name;
    }

    public int getProfile() {
        return profile;
    }

    public boolean getCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }



    public Add_item(String name, int profile, boolean check) {
        this.name = name;
        this.profile = profile;
        this.check = check;
    }

}//class
