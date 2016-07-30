package com.team1.valueupapp.item;

/**
 * Created by knulps on 2016-03-26.
 */
public class UserItem {
    String objId;
    String name;
    String info;
    String userName;

    public String getObjId() {
        return objId;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getInfo() {
        return info;
    }

    public UserItem(String objectId, String userName, String name, String info) {
        this.objId=objectId;
        this.userName = userName;
        this.name = name;
        this.info = info;
    }
}
