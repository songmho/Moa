package com.team1.valueupapp;

/**
 * Created by songmho on 15. 8. 18.
 */
public class MainListitem {
    String name;
    String job;

    public String getJob() {
        return this.job;
    }

    public String getName() {
        return this.name;
    }

    public MainListitem(String job,String name){
        this.name=name;
        this.job=job;
    }
}
