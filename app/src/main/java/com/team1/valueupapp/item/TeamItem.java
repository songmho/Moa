package com.team1.valueupapp.item;

/**
 * Created by hyemi on 2015-08-16.
 */
public class TeamItem {
    String title;
    String name;
    String detail;
    String username;

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public TeamItem(String title, String name, String username , String detail) {
        this.title = title;
        this.name = name;
        this.detail = detail;
        this.username = username;
    }
}
