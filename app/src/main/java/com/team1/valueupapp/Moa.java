package com.team1.valueupapp;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.parse.Parse;
import com.parse.ParseACL;
import io.fabric.sdk.android.Fabric;

/**
 * Created by songmho on 2015-07-04.
 */
public class Moa extends Application {
    String APPLICATION_ID="1meD1nBM07SCIDJ5cGdIWLXT4ASzvtF4QFprTbuD";
    String CLIENT_KEY="tLtCFKlQ4SZKmCwOgrkUdAhmkZvCyLe7h5tCvl7E";

    @Override
    public void onCreate() {
        super.onCreate();

        // 개발 버전에서는 Crashlytics log가 발생하지 않도록 예외처리
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
        }

        //Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
        ParseACL defaultACL=new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}
