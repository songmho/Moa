package com.team1.valueupapp;

/**
 * Created by songmho on 15. 8. 20.
 */
public class Teamadd_item {
    byte[] profile;
    String name;
    String objectId;

    public String getObjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public byte[] getProfile() {
        return profile;
    }
    public Teamadd_item(byte[] profile,String name, String objectId){
        this.profile=profile;
        this.name=name;
        this.objectId = objectId;
    }
}
