package com.team1.valueupapp;

/**
 * Created by songmho on 15. 8. 20.
 */
public class Team_Member_add_item {
    byte[] profile;
    String name;
    boolean check;
    String objId;

    public byte[] getProfile() {
        return profile;
    }

    public String getName() {
        return name;
    }

    public boolean isCheck() {
        return check;
    }

    public String getObjId() {
        return objId;
    }

    public Team_Member_add_item(byte[] profile, String name, boolean check, String objectId){
        this.profile=profile;
        this.name=name;
        this.check=check;
        this.objId=objectId;
    }
}
