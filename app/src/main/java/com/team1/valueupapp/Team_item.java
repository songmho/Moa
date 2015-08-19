package com.team1.valueupapp;

/**
 * Created by hyemi on 2015-08-16.
 */
public class Team_item {
    String title;
    String name;
    String detail;
    String pick;
    int team;

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public String getPick() {
        return pick;
    }

    public int getTeam() {
        return team;
    }

    public String getTitle() {
        return title;
    }

    public Team_item(String title,String name, String detail, String pick,int team){
        this.title=title;
        this.name=name;
        this.detail=detail;
        this.pick=pick;
        this.team=team;

    }
}
