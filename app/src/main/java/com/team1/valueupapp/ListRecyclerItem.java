package com.team1.valueupapp;

/**
 * Created by songmho on 2015-06-30.
 */
public class ListRecyclerItem {
    String app_name;
    String name;
    boolean star;
    String detail;
    int profile;

    int getProfile(){return  this.profile;}
    String getApp_name(){
        return this.app_name;
    }
    String getName(){
        return this.name;
    }
    boolean getStar(){
        return this.star;
    }
    void setStar(boolean star){
        this.star=star;
    }

    String getDetail(){
        return this.detail;
    }
    public ListRecyclerItem(int profile,String app_name, String name, boolean star, String detail) {
        this.profile=profile;
        this.app_name=app_name;
        this.name=name;
        this.star=star;
        this.detail=detail;
    }
}
