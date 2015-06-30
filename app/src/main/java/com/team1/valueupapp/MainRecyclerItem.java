package com.team1.valueupapp;

/**
 * Created by songmho on 2015-06-30.
 */
public class MainRecyclerItem {
    String app_name;
    String name;
    int star;
    String detail;

    String getApp_name(){
        return this.app_name;
    }
    String getName(){
        return this.name;
    }
    int getStar(){
        return this.star;
    }
    String getDetail(){
        return this.detail;
    }
    public MainRecyclerItem(String app_name, String name, int star, String detail) {
        this.app_name=app_name;
        this.name=name;
        this.star=star;
        this.detail=detail;
    }
}
