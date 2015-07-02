package com.team1.valueupapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

/**
 * Created by songmho on 2015-07-03.
 */
public class Moa extends Application {
    private static final String APPLICATION_ID="fq7iAT1SAXB2GUDtbgJnA11fJGAuCw8LsT6ifF4m";
    private static final String CLIENT_KEY="1vfQkCpcB5hNQiovnVZdpgojHx8Yv3Fg5wNDnUn0";

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseACL defaultACL=new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL,true);
    }
}
