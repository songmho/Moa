package com.team1.valueupapp.item;

/**
 * Created by knulps on 2016-03-26.
 */
public class UserItem {
    String name;
    String info;
    String userName;

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getInfo() {
        return info;
    }

    public UserItem(String userName, String name, String info) {
        this.userName = userName;
        this.name = name;
        this.info = info;
    }
}
