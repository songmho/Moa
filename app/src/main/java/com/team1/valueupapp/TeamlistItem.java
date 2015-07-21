package com.team1.valueupapp;

/**
 * Created by songmho on 2015-07-21.
 */
public class TeamlistItem {
    String name;
    int job;
    int profile;

    String getName(){return this.name;}
    int getJob(){return this.job;}
    int getProfile(){return this.profile;}

    TeamlistItem(int profile,String name,int job){
        this.profile=profile;
        this.name=name;
        this.job=job;
    }

}
