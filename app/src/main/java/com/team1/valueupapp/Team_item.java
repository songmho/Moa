package com.team1.valueupapp;

/**
 * Created by hyemi on 2015-08-16.
 */
public class Team_item {
    int profile;
    String state;
    String idea;
    String idea_info;
    String name1;
    String name2;
    String name3;
    String name4;
    String name5;
    String name6;

    public int getProfile() {
        return profile;
    }

    public String getIdea() {
        return idea;
    }

    public String getIdea_info() {
        return idea_info;
    }

    public String getName1() {
        return name1;
    }

    public String getName2() {
        return name2;
    }

    public String getName3() {
        return name3;
    }

    public String getName4() {
        return name4;
    }

    public String getName5() {
        return name5;
    }

    public String getName6() {
        return name6;
    }

    public String getState() {
        return state;
    }

    public Team_item(int profile, String idea, String idea_info, String name1, String name2,
                     String name3, String name4, String name5, String name6, String state) {
        this.profile = profile;
        this.idea = idea;
        this.idea_info = idea_info;
        this.name1 = name1;
        this.name2 = name2;
        this.name3 = name3;
        this.name4 = name4;
        this.name5 = name5;
        this.name6 = name6;
        this.state = state;
    }
}
