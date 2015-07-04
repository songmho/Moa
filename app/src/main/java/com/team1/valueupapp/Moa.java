package com.team1.valueupapp;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

/**
 * Created by songmho on 2015-07-03.
 */
public class Moa extends Application {
    String APPLICATION_ID="1meD1nBM07SCIDJ5cGdIWLXT4ASzvtF4QFprTbuD";
    String CLIENT_KEY="tLtCFKlQ4SZKmCwOgrkUdAhmkZvCyLe7h5tCvl7E";

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseACL defaultACL=new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL,true);
    }
}
