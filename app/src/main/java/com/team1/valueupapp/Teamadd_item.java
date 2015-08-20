package com.team1.valueupapp;

/**
 * Created by songmho on 15. 8. 20.
 */
public class Teamadd_item {
    byte[] profile;
    String name;

    public String getName() {
        return name;
    }

    public byte[] getProfile() {
        return profile;
    }
    public Teamadd_item(byte[] profile,String name){
        this.profile=profile;
        this.name=name;
    }
}
